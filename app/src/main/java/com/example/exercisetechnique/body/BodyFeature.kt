package com.example.exercisetechnique.body

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

    init {
        Log.d("TAG", "BodyFeature inited $this")
    }
    sealed class Wish {
        data class SelectMuscle(val muscle: Muscle) : Wish()
    }

    sealed class Effect {
        data class ShowSelectedMuscle(val muscle: Muscle) : Effect()
    }

    data class State(val side : Side,
                     val sex: Sex,
                     val selectedMuscle : Muscle?)

    class ActorImpl(private val navigationPublisher: PublishSubject<NavigationEvent>) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, action: Wish): Observable<out Effect> {
//            for (one in Thread.currentThread().stackTrace){
//                Log.d("TAG", one.toString())
//            }
            Log.d("TAG", "$this ActorImpl newAction $action")
            //todo почему это вызывается дважды, после нажатия кнопки после восстановления. Возможно два фрагмента или две фичи
            val effect =  when(action) {
                is Wish.SelectMuscle -> {
                    if (state.selectedMuscle != null && action.muscle == state.selectedMuscle) {
                        navigationPublisher.onNext(NavigationEvent.ShowMuscleVideos(action.muscle, state.sex))
                        null
                    } else {
                        Effect.ShowSelectedMuscle(action.muscle)
                    }
                }
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
            Log.d("TAG", "invoke effect $effect")
            return when(effect) {
                is Effect.ShowSelectedMuscle -> State(state.side, state.sex, effect.muscle)
            }
        }
    }

}