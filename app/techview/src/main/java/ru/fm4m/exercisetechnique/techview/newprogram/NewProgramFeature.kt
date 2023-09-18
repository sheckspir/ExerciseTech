package ru.fm4m.exercisetechnique.techview.newprogram

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.newprogram.RedownloadNewProgramUseCase
import ru.fm4m.exercisetechnique.techview.core.PerFragment

import javax.inject.Inject
import javax.inject.Named

@PerFragment
class NewProgramFeature @Inject constructor(
    @Named("NewProgramState") state : Parcelable?,
    redownloadNewProgramUseCase: RedownloadNewProgramUseCase,
    navigationPublisher: PublishSubject<NavigationEvent>,
) : ActorReducerFeature<NewProgramFeature.Wish, NewProgramFeature.Effect, NewProgramFeature.State, NewProgramFeature.News>(
    initialState = if (state != null) state as State else State(false),
    bootstrapper = null,
    actor = ActorImpl(redownloadNewProgramUseCase, navigationPublisher),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State (
        val isLoading: Boolean,
        val videoLists: List<VideoInfo>? = null,
    )

    sealed class Wish {
        object RedownloadLast : Wish()
        class ShowVideo(val videoInfo: VideoInfo) : Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class LoadedVideosInfo(val videoLists: List<VideoInfo>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecuteRequest(val throwable: Throwable) : News()
    }

    class ActorImpl(
        private val redownloadNewProgramUseCase: RedownloadNewProgramUseCase,
        private val navigationPublisher: PublishSubject<NavigationEvent>,
    ) : Actor<State, Wish, Effect> {

        @Suppress("USELESS_CAST")
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            return when (action) {
                is Wish.RedownloadLast -> {
                    redownloadNewProgramUseCase.invoke()
                        .map {
                            when(it) {
                                is DownloadDataEffect.StartDownload -> Effect.StartedLoading
                                is DownloadDataEffect.ErrorDownload -> Effect.ErrorLoading(it.e)
                                is DownloadDataEffect.DownloadedData -> Effect.LoadedVideosInfo(it.result)
                            }
                        }
                }
                is Wish.ShowVideo -> {
                    navigationPublisher.onNext(NavigationEvent.ShowOneVideoFromNewProgram(action.videoInfo))
                    Observable.empty()
                }
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
                    state.copy(isLoading = false,
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
            return when (effect) {
                is Effect.ErrorLoading -> News.ErrorExecuteRequest(effect.throwable)
                else -> null
            }
        }
    }
}