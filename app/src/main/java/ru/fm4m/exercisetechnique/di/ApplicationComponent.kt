package ru.fm4m.exercisetechnique.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import ru.fm4m.exercisetechnique.ExerciseApplication
import ru.fm4m.exercisetechnique.bodymain.BodyMainFragmentProvider
import ru.fm4m.exercisetechnique.bodymain.body.BodyFragmentProvider
import ru.fm4m.exercisetechnique.newprogram.NewProgramProvider
import ru.fm4m.exercisetechnique.videolist.VideoListFragmentProvider
import ru.fm4m.exercisetechnique.videosearch.SearchVideosFragmentProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ApplicationModule::class,
//    BodyModule::class,
    BodyFragmentProvider::class,
    NewProgramProvider::class,
    BodyMainFragmentProvider::class,
    VideoListFragmentProvider::class,
    SearchVideosFragmentProvider::class
])
interface ApplicationComponent {

    fun inject(application: ExerciseApplication)
//    fun inject(fragment: BodyFragment)
}