package ru.fm4m.exercisetechnique.techview.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import io.reactivex.subjects.PublishSubject
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.onevideo.ExerciseInfoFragment

class ExerciseNavigationHostFragment: NavHostFragment(), NavigationPublisherContainer {

    @SuppressLint("CheckResult") //maybe change that to some another RX? OR StateFlow?
    override val navigationPublisher = PublishSubject.create<NavigationEvent>().apply {
        this.subscribe {
            when(it) {

                is NavigationEvent.ShowOneVideo -> {
                    findNavController().navigate(R.id.action_searchVideosFragment_to_oneVideoFragment, Bundle().apply {
                        this.putSerializable(ExerciseInfoFragment.ARG_VIDEO_INFO, it.videoInfo)
                    })
                }
                // TODO: костыль. А как сделать так чтобы не надо было думать откуда прилетело? Может сделать из базового переход?

                else -> {
                    //do nothing
                }
            }
        }
    }

}

interface NavigationPublisherContainer {

    val navigationPublisher : PublishSubject<NavigationEvent>
}

fun Fragment.findNavigationPublisher(): PublishSubject<NavigationEvent> {
    var findFragment: Fragment? = this
    do {
        if (findFragment is NavigationPublisherContainer) {
            return findFragment.navigationPublisher
        }
        val primaryNavFragment = findFragment?.parentFragmentManager?.primaryNavigationFragment
        if (primaryNavFragment is NavigationPublisherContainer) {
            return primaryNavFragment.navigationPublisher
        }
        //repeat maybe parent fragment is Navigation
        findFragment = findFragment?.parentFragment
    } while (findFragment != null)

    throw IllegalStateException("Fragment " + this
            + " does not have a NavigationPublisher set")
}