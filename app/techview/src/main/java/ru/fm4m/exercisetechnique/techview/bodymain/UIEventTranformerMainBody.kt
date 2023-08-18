package ru.fm4m.exercisetechnique.techview.bodymain

import ru.fm4m.exercisetechnique.techview.core.UIEvent
import ru.fm4m.exercisetechnique.techview.bodymain.body.BodyFeature
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side

open class UIEventMainBody : UIEvent() {
    data class MuscleClicked(val muscle: Muscle): UIEventMainBody()
    data class MuscleDownloadClicked(val sex: Sex) : UIEventMainBody()
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