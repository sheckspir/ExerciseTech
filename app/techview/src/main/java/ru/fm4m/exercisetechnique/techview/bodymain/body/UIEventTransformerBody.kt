package ru.fm4m.exercisetechnique.techview.bodymain.body

import ru.fm4m.exercisetechnique.techview.core.UIEvent
import ru.fm4m.exercisetechnique.techview.bodymain.UIEventMainBody


class UIEventTransformerBody : (UIEvent) -> BodyFeature.Wish? {
    override fun invoke(p1: UIEvent): BodyFeature.Wish? = when(p1) {
        is UIEventMainBody.MuscleDownloadClicked -> BodyFeature.Wish.RedownloadMuscles(p1.sex)
        is UIEventMainBody.MuscleClicked -> BodyFeature.Wish.SelectMuscle(p1.muscle)
        else -> null
    }
}


