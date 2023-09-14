package ru.fm4m.exercisetechnique.techview.bodymain.body

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.bodymain.UIEventMainBody
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_body.*
import kotlinx.android.synthetic.main.fragment_body.view.*
import ru.fm4m.exercisetechnique.techview.core.NavigationEvent
import ru.fm4m.exercisetechnique.techview.core.NavigationPublisherContainer
import ru.fm4m.exercisetechnique.techview.core.findNavigationPublisher
import ru.fm4m.exercisetechnique.techview.videolist.VideoListFragment
import javax.inject.Inject

class BodyFragment : Fragment(R.layout.fragment_body),
    OnBodyPartSelectedListener,
    ObservableSource<UIEventMainBody>,
    Consumer<BodyFeature.State>,
    NavigationPublisherContainer {

    @SuppressLint("CheckResult")
    override val navigationPublisher = PublishSubject.create<NavigationEvent>().apply {
            this.subscribe {event ->
                if (event is NavigationEvent.ShowMuscleVideos) {
                    findNavController().navigate(R.id.action_bodyFragment_to_videoListFragment, Bundle().apply {
                        this.putSerializable(VideoListFragment.ARG_SEX, event.sex)
                        this.putSerializable(VideoListFragment.ARG_MUSCLE, event.muscle)
                    })
                } else {
                    //send to parent
                    parentFragment?.findNavigationPublisher()?.onNext(event)
                }
            }
        }
    private lateinit var manager : BodyAreasManager


    @Inject
    lateinit var source: PublishSubject<UIEventMainBody>
    @Inject
    lateinit var feature : BodyFeature
    @Inject
    lateinit var bindings : BodyScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        bindings.setup(this)
    }

    fun getSex() : Sex {
        val result = arguments?.getSerializable(ARG_SEX)
        return if (result == null) {
            Sex.MALE
        } else{
            result as Sex
        }
    }

    fun getSide() : Side {
        val result = arguments?.getSerializable(ARG_SIDE)
        return if (result == null) {
            Side.FRONT
        } else{
            result as Side
        }
    }

    override fun onResume() {
        super.onResume()
        source.onNext(UIEventMainBody.MuscleDownloadClicked(getSex()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = BodyAreasManager(view.imageBody, view.imageFullBody, this)

    }

    override fun subscribe(observer: Observer<in UIEventMainBody>) {
        source.subscribe(observer)
    }

    override fun accept(t: BodyFeature.State) {
        Log.d("TAG", "accept new state $t")
        if (!::manager.isInitialized) {
            return
        }
        if (t.selectedMuscle != null) {
            textNameMuscle.visibility = if (t.showTitleMuscle) View.VISIBLE else View.GONE
            textNameMuscle.text = t.selectedMuscle.name
        }
        if (manager.showed()) {
            manager.updateSelectedId(t.selectedMuscle?.muscle)
        } else if(!t.showedMuscles.isNullOrEmpty()) {
            //пока игнорим showedMuscles, вообще по идее надо проверять этот лист и не отображать если у нас нет в списке, но кажется это может и не пригодиться
            if (t.sex == Sex.MALE) {
               if (t.side == Side.FRONT) {
                   manager.showManFront()
               } else {
                   manager.showManBack()
               }
            } else {
                if (t.side == Side.FRONT) {
                    manager.showWomanFront()
                } else {
                    manager.showWomanBack()
                }
            }
            manager.updateSelectedId(t.selectedMuscle?.muscle)
        }
    }

    override fun onMuscleSelected(muscle: Muscle) {
        source.onNext(UIEventMainBody.MuscleClicked(muscle))
    }

    companion object {

        const val ARG_SEX = "sex"
        const val ARG_SIDE = "side"

        @JvmStatic
        fun newInstance(sex: Sex, side: Side) =
            BodyFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SEX, sex)
                    putSerializable(ARG_SIDE, side)
                }
            }
    }
}