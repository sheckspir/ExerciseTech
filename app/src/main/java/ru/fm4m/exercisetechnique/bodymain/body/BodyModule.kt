package ru.fm4m.exercisetechnique.bodymain.body

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.PerFragment
import ru.fm4m.exercisetechnique.bodymain.UIEventMainBody
import ru.fm4m.exercisetechnique.findNavigationPublisher
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side

import javax.inject.Named


@Module
class BodyDataModule {

    @PerFragment
    @Provides
    @Named("sex")
    fun sex(bodyFragment: BodyFragment) : Sex {
        return bodyFragment.getSex()
    }

    @PerFragment
    @Provides
    @Named("side")
    fun side(bodyFragment: BodyFragment) : Side {
        return bodyFragment.getSide()
    }

    @PerFragment
    @Provides
    fun navigationPublisher(fragment: BodyFragment) : PublishSubject<NavigationEvent> {
        return fragment.findNavigationPublisher()
    }

    @PerFragment
    @Provides
    fun provideEventMainBody() : PublishSubject<UIEventMainBody> {
        return PublishSubject.create()
    }
}


@Module
abstract class BodyFragmentProvider {

    @PerFragment
    @ContributesAndroidInjector (modules = [BodyDataModule::class])
    abstract fun provideBodyFragment(): BodyFragment

}