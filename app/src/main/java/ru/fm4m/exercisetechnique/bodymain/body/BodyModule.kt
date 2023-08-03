package ru.fm4m.exercisetechnique.bodymain.body

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.model.Side
import javax.inject.Named

@Module(subcomponents = [BodyFragmentSubcomponent::class], includes = [BodyDataModule::class])
abstract class BodyModule {

    @Binds
    @IntoMap
    @ClassKey(BodyFragment::class)
    abstract fun bindBodyFragmentInjectorFactory(factory: BodyFragmentSubcomponent.Factory) : AndroidInjector.Factory<out Any>
}

@Module
class BodyDataModule {

    @Provides
    @Named("sex")
    fun sex(bodyFragment: BodyFragment) : Sex {
        return bodyFragment.getSex1()
    }

    @Provides
    @Named("side")
    fun side(bodyFragment: BodyFragment) : Side {
        return bodyFragment.getSide1()
    }
}


@Module
abstract class BodyFragmentProvider {

    @ContributesAndroidInjector (modules = [BodyDataModule::class])
    abstract fun provideBodyFragment(): BodyFragment

}