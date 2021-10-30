package com.example.exercisetechnique.videolist

import android.media.effect.EffectFactory
import android.os.Parcelable
import android.util.Log
import com.badoo.mvicore.element.*
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.VideoInfo
import com.example.exercisetechnique.model.YouTubeVideoInfo
import com.example.exercisetechnique.server.ServerApi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VideoListFeature(
    timeCapsule: TimeCapsule<Parcelable>,
    service: ServerApi,
    sex: Sex,
    muscle: Muscle
): ActorReducerFeature<VideoListFeature.Wish, VideoListFeature.Effect, VideoListFeature.State, Nothing>(
    initialState = timeCapsule.get(VideoListFeature::class.java)?: State(false),
    bootstrapper = null,
    actor =  ActorImpl(service, sex, muscle),
    reducer = ReducerImpl()
) {

    data class State(
        val isLoading: Boolean,
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
            return when(action) {
                is Wish.RedownloadList -> {
                    Observable.just(Effect.StartedLoading as Effect)
                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.computation())
//                        .observeOn(Schedulers.computation())
                        .mergeWith(service.getVideoList(sex == Sex.MALE, muscle)
                            .map { Effect.LoadedVideosInfo(it) as Effect}
                            .onErrorReturn { Effect.ErrorLoading(it) }
                            .observeOn(AndroidSchedulers.mainThread()))
//                        .onErrorReturn { Effect.ErrorLoading(it) }

//                    service.getVideoList(sex == Sex.MALE, muscle)
//                        .map { Effect.LoadedVideosInfo(it) as Effect }

//                        .startWith{ Effect.StartedLoading}
//                        .onErrorReturn { Effect.ErrorLoading(it) }
//                    val array = ArrayList<VideoInfo>().apply{
//                        add(YouTubeVideoInfo("NvL6jeV05Wk"))
//                        add(YouTubeVideoInfo("NvL6jeV05Wk"))
//                        add(YouTubeVideoInfo("NvL6jeV05Wk"))
//                    }
//                    just(Effect.LoadedVideosInfo(array))
                }
                else -> empty() // TODO: 10/9/21 temp
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State {
            Log.d("TAG", "new effect $effect")
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