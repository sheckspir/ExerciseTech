package com.example.exercisetechnique.body

import com.example.exercisetechnique.UIEvent
import com.example.exercisetechnique.model.Muscle

sealed class UIEventBody : UIEvent() {
    data class MuscleClicked(val muscle: Muscle): UIEventBody()
}
class UIEventTransformerBody : (UIEvent) -> BodyFeature.Wish? {
    override fun invoke(p1: UIEvent): BodyFeature.Wish? = when(p1) {
        is UIEventBody.MuscleClicked -> BodyFeature.Wish.SelectMuscle(p1.muscle)
        else -> null
    }
}