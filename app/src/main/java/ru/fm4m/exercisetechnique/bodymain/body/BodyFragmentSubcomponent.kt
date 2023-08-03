package ru.fm4m.exercisetechnique.bodymain.body

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface BodyFragmentSubcomponent: AndroidInjector<BodyFragment> {

    @Subcomponent.Factory
    public interface Factory : AndroidInjector.Factory<BodyFragment>
}