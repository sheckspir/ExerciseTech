package ru.fm4m.exercisetechnique.techview.actualprogramm

import android.util.Log
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.ProgramInfo
import ru.fm4m.exercisetechnique.techdomain.newprogram.DownloadActualProgram
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import javax.inject.Inject

@PerFragment
class ActualProgramFeature @Inject constructor(
    navigationPublisher: PublishSubject<NavigationEvent>,
    useCase: DownloadActualProgram,
) : ActorReducerFeature<ActualProgramFeature.Wish, ActualProgramFeature.Effect, ActualProgramFeature.State, Nothing>(
    initialState = State(),
    bootstrapper = null,
    actor = ActorImpl(useCase, navigationPublisher),
    reducer = ReducerImpl()
) {

    sealed class Wish {
        object DownloadProgram : Wish()
        class ShowDay(val dayId: Int) : Wish()
    }

    sealed class Effect {
        object StartedDownload : Effect()
        data class LoadedProgram(val program: ProgramInfo) : Effect()
        data class ErrorInLoading(val exception: Throwable) : Effect()
    }


    data class State(
        val program: ProgramInfo? = null,
        val loading: Boolean = false,
        val error: Throwable? = null,
    )


    class ActorImpl(
        private val useCase: DownloadActualProgram,
        private val navigationPublisher: PublishSubject<NavigationEvent>,
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            Log.d("TAG", "something arrived $action $state")
            return when (action) {
                is Wish.DownloadProgram ->
                    downloadAndMapProgram()

                is Wish.ShowDay -> {
                    val programId = state.program?.id
                    if (programId != null) {
                        navigationPublisher.onNext(
                            NavigationEvent.ShowProgramDay(
                                programId,
                                action.dayId
                            )
                        )
                    }
                    Observable.empty()
                }
            }
        }

        private fun downloadAndMapProgram(): Observable<out Effect> {
            return useCase()
                .map {
                    when (it) {
                        is DownloadDataEffect.StartDownload -> Effect.StartedDownload
                        is DownloadDataEffect.DownloadedData -> Effect.LoadedProgram(it.result)
                        is DownloadDataEffect.ErrorDownload -> Effect.ErrorInLoading(it.e)
                    }
                }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State {
            return when (effect) {
                is Effect.LoadedProgram -> state.copy(
                    program = effect.program,
                    loading = false,
                    error = null
                )

                is Effect.ErrorInLoading -> state.copy(
                    program = null,
                    loading = false,
                    error = effect.exception
                )

                is Effect.StartedDownload -> state.copy(
                    program = state.program,
                    loading = true,
                    error = null
                )
            }
        }
    }


}