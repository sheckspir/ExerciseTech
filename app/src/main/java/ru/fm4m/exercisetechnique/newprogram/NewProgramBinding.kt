package ru.fm4m.exercisetechnique.newprogram

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.core.AndroidFragmentBindings

class NewProgramBinding(view: NewProgramFragment,
                        private val feature: NewProgramFeature
                        ): AndroidFragmentBindings<NewProgramFragment>(view) {

    override fun setup(view: NewProgramFragment) {
        binder.bind(view to feature using UIEventTransformerNewProgram())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}