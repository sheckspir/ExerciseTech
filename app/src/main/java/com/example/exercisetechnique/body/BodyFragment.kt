

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.exercisetechnique.R
import com.example.exercisetechnique.body.*
import com.example.exercisetechnique.findNavigationPublisher
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.Side
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_body.view.*


class BodyFragment : Fragment(), OnBodyPartSelectedListener, ObservableSource<UIEventBody>, Consumer<BodyFeature.State> {

    private lateinit var sex : Sex
    private lateinit var side : Side
    private val source = PublishSubject.create<UIEventBody>()

    private lateinit var manager : BodyAreasManager
    private lateinit var bindings : BodyScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sex = it.getSerializable(ARG_SEX) as Sex
            side = it.getSerializable(ARG_SIDE) as Side
        }
        bindings = BodyScreenBinding(this, BodyFeature(sex, side, findNavigationPublisher()))
        bindings.setup(this)
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

    override fun onStart() {
        super.onStart()

    }

    override fun subscribe(observer: Observer<in UIEventBody>) {
        source.subscribe(observer)
    }

    override fun accept(t: BodyFeature.State) {
        Log.d("TAG", "accept new state $t")
        if (!::manager.isInitialized) {
            return
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
        Log.d("TAG", "onMuscleSelected $muscle")
        source.onNext(UIEventBody.MuscleClicked(muscle))
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