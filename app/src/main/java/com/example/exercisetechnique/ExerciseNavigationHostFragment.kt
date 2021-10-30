package com.example.exercisetechnique

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.exercisetechnique.videolist.VideoListFragment
import io.reactivex.subjects.PublishSubject

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