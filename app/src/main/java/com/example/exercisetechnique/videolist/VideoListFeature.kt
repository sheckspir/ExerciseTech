package com.example.exercisetechnique.videolist

import android.os.Parcelable
import com.badoo.mvicore.element.*
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.VideoInfo
import com.example.exercisetechnique.server.ServerApi
import io.reactivex.Observable
import io.reactivex.Observable.just

class VideoListFeature(
    timeCapsule: TimeCapsule<Parcelable>,
    service: ServerApi,
    sex: Sex,
    muscle: Muscle
): ActorReducerFeature<VideoListFeature.Wish, VideoListFeature.Effect, VideoListFeature.State, VideoListFeature.News>(
    initialState = timeCapsule.get(VideoListFeature::class.java)?: State(false),
    bootstrapper = BootstrapperImpl(sex, muscle),
    actor =  ActorImpl(service),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val isLoading: Boolean,
        val videoLists : List<VideoInfo>? = null
    )

    sealed class Wish {
        class LoadNewList(val sex: Sex, val muscle: Muscle) : Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class LoadedVideosInfo(val videoLists: List<VideoInfo>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecuteRequest(val throwable: Throwable) : News()
    }

    class BootstrapperImpl(private val sex: Sex, private val muscle: Muscle) : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> {
            return just(Wish.LoadNewList(sex, muscle))
        }
    }

    class ActorImpl(private val service: ServerApi) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            return when(action) {
                is Wish.LoadNewList -> {
                    service.getVideoList(action.sex == Sex.MALE, action.muscle)
                        .map { Effect.LoadedVideosInfo(it) as Effect }
                        .startWith{ Effect.StartedLoading}
                        .onErrorReturn { Effect.ErrorLoading(it) }
                }
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
                    state.copy(false, effect.videoLists)
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