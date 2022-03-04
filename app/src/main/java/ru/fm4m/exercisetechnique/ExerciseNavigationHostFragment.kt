package ru.fm4m.exercisetechnique

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ru.fm4m.exercisetechnique.videolist.VideoListFragment
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.onevideo.OneVideoFragment

class ExerciseNavigationHostFragment: NavHostFragment(), NavigationPublisherContainer {

    private val navigationPublisher = PublishSubject.create<NavigationEvent>().apply {
        this.subscribe {
            when(it) {
                is NavigationEvent.ShowMuscleVideos -> {
                    findNavController().navigate(R.id.action_bodyFragment_to_videoListFragment, Bundle().apply {
                        this.putSerializable(VideoListFragment.ARG_SEX, it.sex)
                        this.putSerializable(VideoListFragment.ARG_MUSCLE, it.muscle)
                    })
                }
                is NavigationEvent.ShowOneVideo -> {
                    findNavController().navigate(R.id.action_searchVideosFragment_to_oneVideoFragment, Bundle().apply {
                        this.putSerializable(OneVideoFragment.ARG_VIDEO_INFO, it.videoInfo)
                    })
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    override fun getNavPublisher() = navigationPublisher
}

interface NavigationPublisherContainer {

    fun getNavPublisher() : PublishSubject<NavigationEvent>
}

fun Fragment.findNavigationPublisher(): PublishSubject<NavigationEvent> {
    var findFragment: Fragment? = this
    while (findFragment != null) {
        if (findFragment is NavigationPublisherContainer) {
            return findFragment.getNavPublisher()
        }
        val primaryNavFragment = findFragment.parentFragmentManager
            .primaryNavigationFragment
        if (primaryNavFragment is NavigationPublisherContainer) {
            return primaryNavFragment.getNavPublisher()
        }
        findFragment = findFragment.parentFragment
    }

    throw IllegalStateException("Fragment " + this
            + " does not have a NavigationPublisher set")
}