package ru.fm4m.exercisetechnique.bodymain.body

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.core.AndroidFragmentBinding
import javax.inject.Inject

@PerFragment
class BodyScreenBinding @Inject constructor(view: BodyFragment,
                        private val feature: BodyFeature
                        ) : AndroidFragmentBinding<BodyFragment>(view){

     override fun setup(view: BodyFragment) {
        binder.bind(view to feature using UIEventTransformerBody())
        binder.bind(feature to view)
    }
}