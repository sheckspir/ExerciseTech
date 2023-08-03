package ru.fm4m.exercisetechnique.videolist

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.bodymain.BodyMainFragment
import ru.fm4m.exercisetechnique.bodymain.BodyMainModule
import ru.fm4m.exercisetechnique.bodymain.body.PerFragment
import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.Sex
import javax.inject.Named

@Module
class VideoListModule {

    @Named("VideoListFeature")
    @Provides
    fun getSavedState(fragment: VideoListFragment) : Parcelable? {
        return AndroidTimeCapsule(fragment.getSavedState())[VideoListFeature::class]
    }

    @PerFragment
    @Provides
    fun getSex(fragment: VideoListFragment) : Sex {
        return fragment.getSex()
    }

    @PerFragment
    @Provides
    fun getMuscle(fragment: VideoListFragment) : Muscle {
        return fragment.getMuscle()
    }

    @PerFragment
    @Provides
    fun getEventPublisher() : PublishSubject<UIEventVideos> {
        return PublishSubject.create()
    }
}

@Module
abstract class VideoListFragmentProvider {
    @PerFragment
    @ContributesAndroidInjector(modules = [VideoListModule::class])
    abstract fun provideVideoListFragment(): VideoListFragment
}