package ru.fm4m.exercisetechnique.videosearch

import android.os.Parcelable
import android.util.Log
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.server.ServerApi
import java.util.concurrent.TimeUnit

class SearchVideosFeature(
    timeCapsule: TimeCapsule<Parcelable>,
    serviceApi: ServerApi,
    navigationPublisher : PublishSubject<NavigationEvent>
) : ActorReducerFeature<SearchVideosFeature.Wish, SearchVideosFeature.Effect, SearchVideosFeature.State, SearchVideosFeature.News>(
    initialState = timeCapsule[SearchVideosFeature::class.java] ?: State(false),
    bootstrapper = null,
    actor = ActorImpl(serviceApi, navigationPublisher),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(val isLoading: Boolean,
                     val lastText : String = "",
                     val videoLists : List<VideoInfo>? = null)

    sealed class Wish {
        object RedownloadLast: Wish()
        class DownloadMore(val count: Int): Wish()
        class FindVideos(val text : String) : Wish()
        class ShowVideo(val videoInfo: VideoInfo): Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class LoadedVideosInfo(val loadedText : String, val videoLists: List<VideoInfo>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecuteRequest(val throwable: Throwable) : News()
    }

    class ActorImpl(private val serviceApi : ServerApi,
                    private val navigationPublisher : PublishSubject<NavigationEvent>
    ) : Actor<State, Wish, Effect> {

        private val interruptSignal = PublishSubject.create<Unit>()

        @Suppress("USELESS_CAST")
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            return when(action) {
                is Wish.RedownloadLast -> {
                    val downloadText = state.lastText
                    Observable.just(Effect.StartedLoading as Effect)
                        .observeOn(AndroidSchedulers.mainThread())
                        .mergeWith(serviceApi.getVideosForKey(downloadText)
                            .map { Effect.LoadedVideosInfo(downloadText, it) as Effect }
                            .onErrorReturn { Effect.ErrorLoading(it) }
                            .observeOn(AndroidSchedulers.mainThread()))
                }
                is Wish.FindVideos -> {
                    interruptSignal.onNext(Unit)
                    Log.d("TAG", "interrupt old start new ${action.text}")
                    if (state.lastText == action.text && state.videoLists != null) {
                        return Observable.just(Effect.LoadedVideosInfo(state.lastText, state.videoLists))
                    } else {
                        return Completable.timer(500, TimeUnit.MILLISECONDS)
                            .andThen(Observable.just(Effect.StartedLoading as Effect)
                                .observeOn(AndroidSchedulers.mainThread())
                                .mergeWith(serviceApi.getVideosForKey(action.text)
                                    .map {
                                        Log.d("TAG", "map ${action.text} ${it.firstOrNull()}")
                                        return@map Effect.LoadedVideosInfo(action.text, it) as Effect }
                                    .onErrorReturn { Effect.ErrorLoading(it) }
                                    .observeOn(AndroidSchedulers.mainThread()))
                                .doOnComplete {
                                    Log.d("TAG", "doOnComplete")
                                }
                                .doOnDispose {
                                    Log.d("TAG", "doOnDispose")
                                }
                                .doOnError {
                                    Log.d("TAG", "error happened $it")
                                })
                            .takeUntil(interruptSignal)

                    }
                }
                is Wish.ShowVideo -> {
                    navigationPublisher.onNext(NavigationEvent.ShowOneVideo(action.videoInfo))
                    empty()
                }
                else -> empty()
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State {
            return when(effect) {
                is Effect.StartedLoading -> {
                    state.copy(isLoading = true)
                }
                is Effect.LoadedVideosInfo -> {
                    state.copy(isLoading = false,
                        lastText = effect.loadedText,
                        videoLists = effect.videoLists)
                }
                is Effect.ErrorLoading -> {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(action: Wish, effect: Effect, state: State): News? {
            return when(effect) {
                is Effect.ErrorLoading -> News.ErrorExecuteRequest(effect.throwable)
                else -> null
            }
        }
    }
}