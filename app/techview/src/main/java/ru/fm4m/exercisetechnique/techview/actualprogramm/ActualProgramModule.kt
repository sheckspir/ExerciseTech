package ru.fm4m.exercisetechnique.techview.actualprogramm

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.techdomain.newprogram.DownloadActualProgram
import ru.fm4m.exercisetechnique.techdomain.newprogram.DownloadActualProgramImpl
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import ru.fm4m.exercisetechnique.techview.core.findNavigationPublisher
import javax.inject.Named

@Module
abstract class ActualProgramProvider {
    @PerFragment
    @ContributesAndroidInjector(modules = [ActualProgramModule::class])
    abstract fun provideFragment(): ActualProgramFragment

    @Binds
    abstract fun provideUseCase(useCase: DownloadActualProgramImpl): DownloadActualProgram
}

@Module
class ActualProgramModule {

    @Named("ActualProgramState")
    @Provides
    fun actualState(fragment: ActualProgramFragment): Parcelable? {
        return AndroidTimeCapsule(fragment.getLastSavedState())[ActualProgramFeature::class.java]
    }

    @PerFragment
    @Provides
    fun getNavProv(fragment: ActualProgramFragment): PublishSubject<NavigationEvent> {
        return fragment.findNavigationPublisher()
    }

    @PerFragment
    @Provides
    fun getUiEventPublisher() : PublishSubject<UIEventActualProgram> {
        return PublishSubject.create()
    }
}