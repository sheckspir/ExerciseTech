package com.example.exercisetechnique.body

import com.badoo.binder.using
import com.badoo.mvicore.android.AndroidBindings
import io.reactivex.ObservableSource

class BodyScreenBinding(view: BodyFragment,
                        private val feature: BodyFeature
                        ) : AndroidBindings<BodyFragment>(view){

    override fun setup(view: BodyFragment) {
        binder.bind(view to feature using UIEventTransformerBody())
        binder.bind(feature to view)
    }
}