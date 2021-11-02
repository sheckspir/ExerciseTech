package com.example.exercisetechnique.bodymain

import com.badoo.binder.using
import com.example.exercisetechnique.bodymain.body.BodyFeature
import com.example.exercisetechnique.core.AndroidFragmentBindings

class BodyMainScreenBinding(view: BodyMainFragment,
                            private val feature: BodyMainFeature
) : AndroidFragmentBindings<BodyMainFragment>(view) {

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