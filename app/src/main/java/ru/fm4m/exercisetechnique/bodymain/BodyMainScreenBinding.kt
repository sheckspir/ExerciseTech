package ru.fm4m.exercisetechnique.bodymain

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.bodymain.body.BodyFeature
import ru.fm4m.exercisetechnique.core.AndroidFragmentBinding

class BodyMainScreenBinding(view: BodyMainFragment,
                            private val feature: BodyMainFeature
) : AndroidFragmentBinding<BodyMainFragment>(view) {

    override fun setup(view: BodyMainFragment, ) {
        binder.bind(view to feature using UIEventTransformerMainBody())
        binder.bind(feature to view)

    }

    fun setupInnerFeatures(feature1: BodyFeature, feature2: BodyFeature) {
        val newsToWish = NewsToBodyiesNewsTransformer()
        binder.bind(feature.news to feature1 using newsToWish)
        binder.bind(feature.news to feature2 using newsToWish)
    }


}