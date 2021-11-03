package ru.fm4m.exercisetechnique.bodymain.body

import BodyFragment
import com.badoo.binder.using
import ru.fm4m.exercisetechnique.core.AndroidFragmentBindings

class BodyScreenBinding(view: BodyFragment,
                        private val feature: BodyFeature
                        ) : AndroidFragmentBindings<BodyFragment>(view){

    override fun setup(view: BodyFragment) {
        binder.bind(view to feature using UIEventTransformerBody())
        binder.bind(feature to view)
    }
}