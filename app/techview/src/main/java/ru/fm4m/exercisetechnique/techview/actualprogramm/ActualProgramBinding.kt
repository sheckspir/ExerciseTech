package ru.fm4m.exercisetechnique.techview.actualprogramm

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.techview.core.AndroidFragmentBinding
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import javax.inject.Inject

@PerFragment
class ActualProgramBinding @Inject constructor(
    view: ActualProgramFragment,
    private val feature: ActualProgramFeature,
) : AndroidFragmentBinding<ActualProgramFragment>(view) {

    override fun setup(view: ActualProgramFragment) {
        binder.bind(view to feature using UIEventTransformerActualProgram())
        binder.bind(feature to view)
    }
}