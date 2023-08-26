package ru.fm4m.exercisetechnique.techview.videosearch

import android.os.Parcelable
import android.util.Log
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.videosearch.FindVideosByKeyUseCase
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import javax.inject.Inject
import javax.inject.Named

@PerFragment
class SearchVideosFeature @Inject constructor(
    @Named("SearchVideosFeature") lastState: Parcelable?,
    findVideosUseCase: FindVideosByKeyUseCase,
    navigationPublisher: PublishSubject<NavigationEvent>,
) : ActorReducerFeature<SearchVideosFeature.Wish, SearchVideosFeature.Effect, SearchVideosFeature.State, SearchVideosFeature.News>(
    initialState = if (lastState != null) {
        lastState as State
    } else {
        State(false)
    },
    bootstrapper = null,
    actor = ActorImpl(findVideosUseCase, navigationPublisher),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean,
        val lastText: String = "",
        val videoLists: List<VideoInfo>? = null,
    )

    sealed class Wish {
        object RedownloadLast : Wish()
        class DownloadMore(val count: Int) : Wish()
        class FindVideos(val text: String) : Wish()
        class ShowVideo(val videoInfo: VideoInfo) : Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class LoadedVideosInfo(val loadedText: String, val videoLists: List<VideoInfo>) :
            Effect()

        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecuteRequest(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val findVideosByKeyUseCase: FindVideosByKeyUseCase,
        private val navigationPublisher: PublishSubject<NavigationEvent>,
    ) : Actor<State, Wish, Effect> {

        @Suppress("USELESS_CAST")
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            return when (action) {
                is Wish.RedownloadLast -> {
                    val downloadText = state.lastText
                    findVideosByKeyUseCase.getData(downloadText)
                        .map {
                            Log.d("TAG", "get new Effect $it")
                            when (it) {
                                is DownloadDataEffect.StartDownload -> Effect.StartedLoading
                                is DownloadDataEffect.DownloadedData -> Effect.LoadedVideosInfo(
                                    downloadText,
                                    it.result
                                )

                                is DownloadDataEffect.ErrorDownload -> Effect.ErrorLoading(it.e)
                                else -> Effect.ErrorLoading(IllegalArgumentException("Not recognized result $it"))
                            }
                        }.doOnError {
                            Log.d("TAG", "onError ", it)
                        }
                }

                is Wish.FindVideos -> {
                    Log.d("TAG", "interrupt old start new ${action.text}")
                    return findVideosByKeyUseCase.getData(action.text)
                        .map {
                            Log.d("TAG", "get new Effect $it")
                            when (it) {
                                is DownloadDataEffect.StartDownload -> Effect.StartedLoading
                                is DownloadDataEffect.DownloadedData -> Effect.LoadedVideosInfo(
                                    action.text,
                                    it.result
                                )

                                is DownloadDataEffect.ErrorDownload -> Effect.ErrorLoading(it.e)
                                else -> Effect.ErrorLoading(IllegalArgumentException("Not recognized result $it"))
                            }
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
            return when (effect) {
                is Effect.StartedLoading -> {
                    state.copy(isLoading = true)
                }

                is Effect.LoadedVideosInfo -> {
                    state.copy(
                        isLoading = false,
                        lastText = effect.loadedText,
                        videoLists = effect.videoLists
                    )
                }

                is Effect.ErrorLoading -> {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(action: Wish, effect: Effect, state: State): News? {
            return when (effect) {
                is Effect.ErrorLoading -> News.ErrorExecuteRequest(effect.throwable)
                else -> null
            }
        }
    }
}