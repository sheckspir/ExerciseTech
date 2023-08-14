package ru.fm4m.exercisetechnique

import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.fm4m.exercisetechnique.di.ApplicationComponent
import ru.fm4m.exercisetechnique.di.ApplicationModule
import ru.fm4m.exercisetechnique.di.DaggerApplicationComponent
import javax.inject.Inject


class ExerciseApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    private lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        appComponent.inject(this)

//            .create()

    }

    override fun androidInjector(): AndroidInjector<Any> {
        return fragmentInjector as AndroidInjector<Any>
    }
}