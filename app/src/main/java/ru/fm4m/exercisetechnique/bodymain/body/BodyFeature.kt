package ru.fm4m.exercisetechnique.bodymain.body

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.techdomain.system.Logger
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.PerFragment
import ru.fm4m.exercisetechnique.techdomain.core.IMuscleName
import javax.inject.Inject
import javax.inject.Named

@PerFragment
class BodyFeature @Inject constructor(
    @Named("sex") sex: Sex,
    @Named("side") side: Side,
    navigationPublisher : PublishSubject<NavigationEvent>,
    muscleNameProvider: IMuscleName,
    logger: Logger
) : ActorReducerFeature<BodyFeature.Wish, BodyFeature.Effect, BodyFeature.State, Nothing>(
    State(side, sex, null, ""),
    null,
    actor = ActorImpl(navigationPublisher, logger, muscleNameProvider),
    reducer = ReducerImpl(muscleNameProvider)
) {

    sealed class Wish {
        data class SelectMuscle(val muscle: Muscle) : Wish()
        data class FocusedSide(val side: Side) : Wish()
        object ChangeSide : Wish()
    }

    sealed class Effect {
        data class ShowSelectedMuscle(val muscle: Muscle, val muscleName: String) : Effect()
        object HideTitle : Effect()
        object ShowTitle : Effect()
    }

    data class State(val side : Side,
                     val sex: Sex,
                     val selectedMuscle : Muscle?,
                     val muscleName : String,
                     val showTitleMuscle : Boolean = false)

    class ActorImpl(private val navigationPublisher: PublishSubject<NavigationEvent>,
                    private val logger: Logger,
                    private val muscleNameProvider: IMuscleName
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            logger.d("TAG","invoke new wish $action" )
            val effect =  when(action) {
                is Wish.SelectMuscle -> {
                    if (state.selectedMuscle != null && action.muscle == state.selectedMuscle) {
                        navigationPublisher.onNext(NavigationEvent.ShowMuscleVideos(action.muscle, state.sex))
                        null
                    } else {
                        Effect.ShowSelectedMuscle(
                            action.muscle,
                            muscleNameProvider.getMuscleName(action.muscle)
                        )
                    }
                }
                is Wish.ChangeSide -> {
                    Effect.HideTitle
                }
                is Wish.FocusedSide -> {
                    if (action.side == state.side) {
                        Effect.ShowTitle
                    } else {
                        Effect.HideTitle
                    }
                }
                else -> null
            }
            return if (effect != null) {
                 Observable.just(effect)
            } else {
                Observable.empty()
            }

        }
    }

    class ReducerImpl(private val muscleNameProvider: IMuscleName) : Reducer<State, Effect>{
        override fun invoke(state: State, effect: Effect): State {
            return when(effect) {
                is Effect.ShowSelectedMuscle -> State(state.side, state.sex, effect.muscle, muscleNameProvider.getMuscleName(effect.muscle),true)
                is Effect.HideTitle -> state.copy(showTitleMuscle = false)
                is Effect.ShowTitle -> state.copy(showTitleMuscle =  true)
            }
        }
    }

}