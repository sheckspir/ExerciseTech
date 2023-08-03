package ru.fm4m.exercisetechnique.newprogram

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.bodymain.body.BodyFragment
import ru.fm4m.exercisetechnique.bodymain.body.PerFragment
import ru.fm4m.exercisetechnique.findNavigationPublisher
import javax.inject.Named

@Module
abstract class NewProgramProvider {

    @PerFragment
    @ContributesAndroidInjector(modules = [NewProgramModule::class])
    abstract fun provideNewProgramFragment() : NewProgramFragment

}

@Module
class NewProgramModule {

    @Named("NewProgramState")
    @Provides
    fun actualState(fragment: NewProgramFragment): Parcelable? {
        return AndroidTimeCapsule(fragment.getLastSavedState())[NewProgramFeature::class.java]
    }

    @PerFragment
    @Provides
    fun navigationPublisher(fragment: NewProgramFragment) : PublishSubject<NavigationEvent> {
        return fragment.findNavigationPublisher()
    }

    @PerFragment
    @Provides
    fun uiEventProvider() : PublishSubject<UIEventNewProgram> {
        return PublishSubject.create()
    }
}