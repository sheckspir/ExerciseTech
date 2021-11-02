package com.example.exercisetechnique.bodymain.body

import com.example.exercisetechnique.UIEvent
import com.example.exercisetechnique.bodymain.UIEventMainBody


class UIEventTransformerBody : (UIEvent) -> BodyFeature.Wish? {
    override fun invoke(p1: UIEvent): BodyFeature.Wish? = when(p1) {
        is UIEventMainBody.MuscleClicked -> BodyFeature.Wish.SelectMuscle(p1.muscle)
        else -> null
    }
}


