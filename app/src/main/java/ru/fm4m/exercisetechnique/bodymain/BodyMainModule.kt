package ru.fm4m.exercisetechnique.bodymain

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.fm4m.exercisetechnique.bodymain.body.BodyDataModule
import ru.fm4m.exercisetechnique.bodymain.body.BodyFragment
import ru.fm4m.exercisetechnique.bodymain.body.PerFragment
import ru.fm4m.exercisetechnique.model.Sex
import javax.inject.Named

@Module
class BodyMainModule {

    @PerFragment
    @Provides
    fun sex(fragment: BodyMainFragment) : Sex {
        return fragment.getSex()
    }
}

@Module
abstract class BodyMainFragmentProvider {

    @PerFragment
    @ContributesAndroidInjector(modules = [BodyMainModule::class])
    abstract fun provideBodyMainFragment(): BodyMainFragment
}