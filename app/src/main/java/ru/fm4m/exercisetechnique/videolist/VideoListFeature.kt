package ru.fm4m.exercisetechnique.videolist

import android.os.Parcelable
import android.util.Log
import com.badoo.mvicore.element.*
import com.badoo.mvicore.feature.ActorReducerFeature
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import io.reactivex.Observable
import io.reactivex.Observable.empty
import ru.fm4m.exercisetechnique.PerFragment
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.IMuscleName
import ru.fm4m.exercisetechnique.techdomain.videolist.RedownloadVideoListBySexAndMuscle
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Named

@PerFragment
class VideoListFeature @Inject constructor(
    @Named("VideoListFeature") parcelableState : Parcelable?,
    redownloadVideosUseCase: RedownloadVideoListBySexAndMuscle,
    muscleNameProvider: IMuscleName,
    sex: Sex,
    muscle: Muscle
): ActorReducerFeature<VideoListFeature.Wish, VideoListFeature.Effect, VideoListFeature.State, VideoListFeature.News>(
    initialState = if (parcelableState != null) parcelableState as State else State(false, muscle, muscleNameProvider.getMuscleName(muscle)),
    bootstrapper = null,
    actor = ActorImpl(redownloadVideosUseCase),
    reducer = ReducerImpl(muscleNameProvider),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean,
        val muscle: Muscle,
        val muscleName: String,
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

    class ActorImpl(private val redownloadUseCase : RedownloadVideoListBySexAndMuscle) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            Log.d("TAG", "new action in videos $action")
            return when(action) {
                is Wish.RedownloadList -> {
                    redownloadUseCase.getData()
                        .map {
                            when(it){
                                is DownloadDataEffect.StartDownload -> Effect.StartedLoading
                                is DownloadDataEffect.DownloadedData -> Effect.LoadedVideosInfo(it.result)
                                is DownloadDataEffect.ErrorDownload -> Effect.ErrorLoading(it.e)
                                else -> Effect.ErrorLoading(IllegalArgumentException("Not recognized result $it"))
                            }
                        }
                }
                else -> empty() // TODO: 10/9/21 temp
            }
        }
    }

    class ReducerImpl(private val muscleNameProvider: IMuscleName) : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State {
            return when(effect) {
                is Effect.StartedLoading -> {
                    state.copy(true)
                }
                is Effect.ErrorLoading -> {
                    state.copy(false)
                }
                is Effect.LoadedVideosInfo -> {
                    state.copy(false, state.muscle, muscleNameProvider.getMuscleName(state.muscle), effect.videoLists)
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