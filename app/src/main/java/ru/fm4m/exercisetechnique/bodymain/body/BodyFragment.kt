package ru.fm4m.exercisetechnique.bodymain.body

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import ru.fm4m.exercisetechnique.R
import ru.fm4m.exercisetechnique.bodymain.UIEventMainBody
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_body.*
import kotlinx.android.synthetic.main.fragment_body.view.*
import javax.inject.Inject

class BodyFragment : Fragment(), OnBodyPartSelectedListener, ObservableSource<UIEventMainBody>, Consumer<BodyFeature.State> {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_body, container, false)
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
        textNameMuscle.visibility = if (t.showTitleMuscle) View.VISIBLE else View.GONE
        if (t.muscleName.isNotEmpty() && t.selectedMuscle != null) {
            textNameMuscle.text = t.muscleName
        }
        if (manager.showed()) {
            manager.updateSelectedId(t.selectedMuscle)
        } else {
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
            manager.updateSelectedId(t.selectedMuscle)
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