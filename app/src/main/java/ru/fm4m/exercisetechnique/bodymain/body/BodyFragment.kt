

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.fm4m.exercisetechnique.R
import ru.fm4m.exercisetechnique.bodymain.UIEventMainBody
import ru.fm4m.exercisetechnique.bodymain.body.BodyAreasManager
import ru.fm4m.exercisetechnique.bodymain.body.BodyFeature
import ru.fm4m.exercisetechnique.bodymain.body.BodyScreenBinding
import ru.fm4m.exercisetechnique.bodymain.body.OnBodyPartSelectedListener
import ru.fm4m.exercisetechnique.findNavigationPublisher
import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.model.Side
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_body.*
import kotlinx.android.synthetic.main.fragment_body.view.*

class BodyFragment : Fragment(), OnBodyPartSelectedListener, ObservableSource<UIEventMainBody>, Consumer<BodyFeature.State> {

    private lateinit var sex : Sex
    private lateinit var side : Side
    private val source: PublishSubject<UIEventMainBody> = PublishSubject.create()

    private lateinit var manager : BodyAreasManager
    private lateinit var bindings : BodyScreenBinding
    lateinit var feature : BodyFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sex = it.getSerializable(ARG_SEX) as Sex
            side = it.getSerializable(ARG_SIDE) as Side
        }
        feature = BodyFeature(sex, side, findNavigationPublisher())
        bindings = BodyScreenBinding(this, feature)
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

    override fun subscribe(observer: Observer<in UIEventMainBody>) {
        source.subscribe(observer)
    }

    override fun accept(t: BodyFeature.State) {
        Log.d("TAG", "accept new state $t")
        if (!::manager.isInitialized) {
            return
        }
        textNameMuscle.visibility = if (t.showTitleMuscle) View.VISIBLE else View.GONE
        if (t.selectedMuscle != null) {
            textNameMuscle.setText(t.selectedMuscle.muscleName)
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