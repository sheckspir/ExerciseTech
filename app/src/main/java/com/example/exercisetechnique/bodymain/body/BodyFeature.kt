package com.example.exercisetechnique.bodymain.body

import android.util.Log
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.exercisetechnique.NavigationEvent
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.Side
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BodyFeature constructor(
    sex: Sex,
    side: Side,
    navigationPublisher : PublishSubject<NavigationEvent>
) : ActorReducerFeature<BodyFeature.Wish, BodyFeature.Effect, BodyFeature.State, Nothing>(
    State(side, sex, null),
    null,
    actor = ActorImpl(navigationPublisher),
    reducer = ReducerImpl()
) {

    sealed class Wish {
        data class SelectMuscle(val muscle: Muscle) : Wish()
        data class FocusedSide(val side: Side) : Wish()
        object ChangeSide : Wish()
    }

    sealed class Effect {
        data class ShowSelectedMuscle(val muscle: Muscle) : Effect()
        object HideTitle : Effect()
        object ShowTitle : Effect()
    }

    data class State(val side : Side,
                     val sex: Sex,
                     val selectedMuscle : Muscle?,
                    val showTitleMuscle : Boolean = false)

    class ActorImpl(private val navigationPublisher: PublishSubject<NavigationEvent>) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
            Log.d("TAG", "invoke new wish $action")
            val effect =  when(action) {
                is Wish.SelectMuscle -> {
                    if (state.selectedMuscle != null && action.muscle == state.selectedMuscle) {
                        navigationPublisher.onNext(NavigationEvent.ShowMuscleVideos(action.muscle, state.sex))
                        null
                    } else {
                        Effect.ShowSelectedMuscle(action.muscle)
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

    class ReducerImpl : Reducer<State, Effect>{
        override fun invoke(state: State, effect: Effect): State {
            return when(effect) {
                is Effect.ShowSelectedMuscle -> State(state.side, state.sex, effect.muscle, true)
                is Effect.HideTitle -> state.copy(showTitleMuscle = false)
                is Effect.ShowTitle -> state.copy(showTitleMuscle =  true)
            }
        }
    }

}