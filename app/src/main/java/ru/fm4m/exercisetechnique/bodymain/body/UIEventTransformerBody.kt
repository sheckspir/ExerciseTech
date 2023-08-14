package ru.fm4m.exercisetechnique.bodymain.body

import ru.fm4m.exercisetechnique.UIEvent
import ru.fm4m.exercisetechnique.bodymain.UIEventMainBody


class UIEventTransformerBody : (UIEvent) -> BodyFeature.Wish? {
    override fun invoke(p1: UIEvent): BodyFeature.Wish? = when(p1) {
        is UIEventMainBody.MuscleDownloadClicked -> BodyFeature.Wish.RedownloadMuscles(p1.sex)
        is UIEventMainBody.MuscleClicked -> BodyFeature.Wish.SelectMuscle(p1.muscle)
        else -> null
    }
}


