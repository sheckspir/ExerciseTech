package ru.fm4m.exercisetechnique.di

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import ru.fm4m.exercisetechnique.ExerciseApplication
import ru.fm4m.exercisetechnique.techdata.server.TechdataModule
import ru.fm4m.exercisetechnique.techview.bodymain.BodyMainFragmentProvider
import ru.fm4m.exercisetechnique.techview.bodymain.body.BodyFragmentProvider
import ru.fm4m.exercisetechnique.techview.newprogram.NewProgramProvider
import ru.fm4m.exercisetechnique.techview.videolist.VideoListFragmentProvider
import ru.fm4m.exercisetechnique.techview.videosearch.SearchVideosFragmentProvider
import ru.fm4m.exercisetechnique.trainingview.ui.di.TrainingProvider
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        TechdataModule::class,
        BodyFragmentProvider::class,
        NewProgramProvider::class,
        BodyMainFragmentProvider::class,
        VideoListFragmentProvider::class,
        SearchVideosFragmentProvider::class,
        TrainingProvider::class
    ]
)
interface ApplicationComponent {

    fun inject(application: ExerciseApplication)

}