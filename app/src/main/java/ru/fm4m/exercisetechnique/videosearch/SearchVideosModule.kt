package ru.fm4m.exercisetechnique.videosearch

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.bodymain.body.PerFragment
import ru.fm4m.exercisetechnique.findNavigationPublisher
import javax.inject.Named

@Module
class SearchVideosModule {

    @PerFragment
    @Named("SearchVideosFeature")
    @Provides
    fun provideLastState(fragment: SearchVideosFragment) : Parcelable? {
        return AndroidTimeCapsule(fragment.getLastState())[SearchVideosFeature::class.java]
    }

    @PerFragment
    @Provides
    fun getNavigatorPublisher(fragment: SearchVideosFragment) : PublishSubject<NavigationEvent> {
        return fragment.findNavigationPublisher()
    }

    @Provides
    fun provideEventPublisher() : PublishSubject<UIEventSearchVideos> {
        return PublishSubject.create()
    }

}

@Module
abstract class SearchVideosFragmentProvider {
    @PerFragment
    @ContributesAndroidInjector(modules = [SearchVideosModule::class])
    abstract fun provideSearchVideosFragment(): SearchVideosFragment
}