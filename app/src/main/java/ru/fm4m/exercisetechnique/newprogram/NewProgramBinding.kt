package ru.fm4m.exercisetechnique.newprogram

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.bodymain.body.PerFragment
import ru.fm4m.exercisetechnique.core.AndroidFragmentBinding
import javax.inject.Inject

@PerFragment
class NewProgramBinding @Inject constructor(view: NewProgramFragment,
                        private val feature: NewProgramFeature
                        ): AndroidFragmentBinding<NewProgramFragment>(view) {

    override fun setup(view: NewProgramFragment) {
        binder.bind(view to feature using UIEventTransformerNewProgram())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}