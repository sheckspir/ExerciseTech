package ru.fm4m.exercisetechnique.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.fm4m.exercisetechnique.ExerciseApplication
import ru.fm4m.exercisetechnique.bodymain.body.BodyDataModule
import ru.fm4m.exercisetechnique.bodymain.body.BodyFragment
import ru.fm4m.exercisetechnique.bodymain.body.BodyFragmentProvider
//import ru.fm4m.exercisetechnique.bodymain.body.BodyFragmentBindModule
import ru.fm4m.exercisetechnique.bodymain.body.BodyModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ApplicationModule::class,
//    BodyModule::class,
BodyFragmentProvider::class
])
interface ApplicationComponent {

    fun inject(application: ExerciseApplication)
//    fun inject(fragment: BodyFragment)
}