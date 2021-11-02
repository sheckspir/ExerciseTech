package com.example.exercisetechnique.bodymain

import com.example.exercisetechnique.UIEvent
import com.example.exercisetechnique.bodymain.body.BodyFeature
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Side

open class UIEventMainBody : UIEvent() {
    data class MuscleClicked(val muscle: Muscle): UIEventMainBody()
    object ChangeSideClicked : UIEventMainBody()
    object FocusedSide: UIEventMainBody()
}

class UIEventTransformerMainBody : (UIEvent) -> BodyMainFeature.Wish? {
    override fun invoke(p1: UIEvent): BodyMainFeature.Wish? = when(p1) {
        is UIEventMainBody.ChangeSideClicked -> BodyMainFeature.Wish.ChangeSide
        is UIEventMainBody.FocusedSide -> BodyMainFeature.Wish.FocusedSide
        else -> null
    }
}

class NewsToBodyiesNewsTransformer : (BodyMainFeature.News) -> BodyFeature.Wish? {
    override fun invoke(p1: BodyMainFeature.News): BodyFeature.Wish? = when(p1) {
        is BodyMainFeature.News.FocusOnFront -> BodyFeature.Wish.FocusedSide(Side.FRONT)
        is BodyMainFeature.News.FocusOnBack -> BodyFeature.Wish.FocusedSide(Side.BACK)
        is BodyMainFeature.News.InChangedFocusState -> BodyFeature.Wish.ChangeSide
    }
}