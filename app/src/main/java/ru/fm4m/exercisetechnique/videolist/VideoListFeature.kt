package ru.fm4m.exercisetechnique.videolist

import android.os.Parcelable
import android.util.Log
import com.badoo.mvicore.element.*
import com.badoo.mvicore.feature.ActorReducerFeature
import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.server.ServerApi
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.fm4m.exercisetechnique.bodymain.body.PerFragment
import javax.inject.Inject
import javax.inject.Named

@PerFragment
class VideoListFeature @Inject constructor(
    @Named("VideoListFeature") parcelableState : Parcelable?,
    service: ServerApi,
    sex: Sex,
    muscle: Muscle
): ActorReducerFeature<VideoListFeature.Wish, VideoListFeature.Effect, VideoListFeature.State, VideoListFeature.News>(
    initialState = if (parcelableState != null) parcelableState as State else State(false, muscle),
    bootstrapper = null,
    actor = ActorImpl(service, sex, muscle),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean,
        val muscle: Muscle,
        val videoLists : List<VideoInfo>? = null
    )

    sealed class Wish {
        object RedownloadList : Wish()
        class DownloadMore(val count: Int): Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class LoadedVideosInfo(val videoLists: List<VideoInfo>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecuteRequest(val throwable: Throwable) : News()
    }

    class ActorImpl(private val service: ServerApi, private val sex: Sex, private val muscle: Muscle) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            Log.d("TAG", "new action in videos $action")
            return when(action) {
                is Wish.RedownloadList -> {
                    Observable.just(Effect.StartedLoading as Effect)
                        .observeOn(AndroidSchedulers.mainThread())
                        .mergeWith(service.getVideoList(sex == Sex.MALE, muscle)
                            .map { Effect.LoadedVideosInfo(it) as Effect}
                            .onErrorReturn { Effect.ErrorLoading(it) }
                            .observeOn(AndroidSchedulers.mainThread()))
                }
                else -> empty() // TODO: 10/9/21 temp
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State {
            return when(effect) {
                is Effect.StartedLoading -> {
                    state.copy(true)
                }
                is Effect.ErrorLoading -> {
                    state.copy(false)
                }
                is Effect.LoadedVideosInfo -> {
                    state.copy(false, state.muscle, effect.videoLists)
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