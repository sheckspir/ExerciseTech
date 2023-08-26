package ru.fm4m.exercisetechnique.techview.bodymain.body

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.fm4m.coredomain.system.Logger
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import ru.fm4m.exercisetechnique.techdomain.bodymain.body.DownloadMuscleUseCase
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import javax.inject.Inject
import javax.inject.Named

@PerFragment
class BodyFeature @Inject constructor(
    @Named("sex") sex: Sex,
    @Named("side") side: Side,
    navigationPublisher : PublishSubject<NavigationEvent>,
    downloadMuscleUseCase: DownloadMuscleUseCase,
    logger: Logger
) : ActorReducerFeature<BodyFeature.Wish, BodyFeature.Effect, BodyFeature.State, Nothing>(
    State(side, sex, null, null),
    null,
    actor = ActorImpl(navigationPublisher, logger, downloadMuscleUseCase),
    reducer = ReducerImpl()
) {

    sealed class Wish {
        data class RedownloadMuscles(val sex: Sex) : Wish()
        data class SelectMuscle(val muscle: Muscle) : Wish()
        data class FocusedSide(val side: Side) : Wish()
        object ChangeSide : Wish()
    }

    sealed class Effect {
        data class ShowAvailableMuscles(val muscles: Map<Muscle, MuscleInfo>): Effect()
        data class ShowSelectedMuscle(val muscle: MuscleInfo) : Effect()
        object HideTitle : Effect()
        object ShowTitle : Effect()
    }

    data class State(val side : Side,
                     val sex: Sex,
                     val showedMuscles: Map<Muscle, MuscleInfo>? = null,
                     val selectedMuscle : MuscleInfo?,
                     val showTitleMuscle : Boolean = false)

    class ActorImpl(private val navigationPublisher: PublishSubject<NavigationEvent>,
                    private val logger: Logger,
                    private val downloadMuscleUseCase: DownloadMuscleUseCase
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            logger.d("TAG","invoke new wish $action" )
            return when(action) {
                is Wish.RedownloadMuscles -> {
                    if (state.showedMuscles.isNullOrEmpty()) {
                        downloadMuscleUseCase.getData(action.sex)
                            .map {
                                if(it is DownloadDataEffect.DownloadedData<Map<Muscle, MuscleInfo>>){
                                    Effect.ShowAvailableMuscles(it.result)
                                } else {
                                    Effect.HideTitle
                                }}
                    } else {
                        Observable.just(Effect.ShowAvailableMuscles(state.showedMuscles))
                    }
                }
                is Wish.SelectMuscle -> {
                    if (state.selectedMuscle != null
                        && action.muscle == state.selectedMuscle.muscle
                    ) {
                        navigationPublisher.onNext(NavigationEvent.ShowMuscleVideos(action.muscle, state.sex))
                        Observable.empty()
                    } else {
                        Observable.fromCallable {
                            var muscleInfo = state.showedMuscles?.get(action.muscle)
                            if (muscleInfo == null) {
                                muscleInfo = MuscleInfo(action.muscle, "")
                            }
                            Effect.ShowSelectedMuscle(muscleInfo)
                        }
                    }
                }
                is Wish.ChangeSide -> {
                    Observable.just(Effect.HideTitle)
                }
                is Wish.FocusedSide -> {
                    if (action.side == state.side) {
                        Observable.just(Effect.ShowTitle)
                    } else {
                        Observable.just(Effect.HideTitle)
                    }
                }
            }

        }
    }

    class ReducerImpl() : Reducer<State, Effect>{
        override fun invoke(state: State, effect: Effect): State {
            return when(effect) {
                is Effect.ShowAvailableMuscles -> state.copy(showedMuscles = effect.muscles)
                is Effect.ShowSelectedMuscle -> state.copy(selectedMuscle = effect.muscle, showTitleMuscle = true)
                is Effect.HideTitle -> state.copy(showTitleMuscle = false)
                is Effect.ShowTitle -> state.copy(showTitleMuscle =  true)
            }
        }
    }

}