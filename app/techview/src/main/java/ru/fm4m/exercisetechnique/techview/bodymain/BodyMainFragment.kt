package ru.fm4m.exercisetechnique.techview.bodymain

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_body_main.buttonChangeSides
import kotlinx.android.synthetic.main.fragment_body_main.firstContainer
import kotlinx.android.synthetic.main.fragment_body_main.secondContainer
import kotlinx.android.synthetic.main.fragment_body_main.view.buttonChangeSides
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.bodymain.body.BodyFragment
import ru.fm4m.exercisetechnique.techview.core.ObservableSourceEventContainer
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import javax.inject.Inject

class BodyMainFragment : Fragment(R.layout.fragment_body_main),
    ObservableSource<UIEventMainBody>,
    Consumer<BodyMainFeature.State>,
    ObservableSourceEventContainer<UIEventMainBody>{

    private val source = PublishSubject.create<UIEventMainBody>()

    @Inject
    lateinit var binding : BodyMainScreenBinding

    override fun getSource(): PublishSubject<out UIEventMainBody> {
        return source
    }

    override fun subscribe(observer: Observer<in UIEventMainBody>) {
        source.subscribe(observer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        binding.setup(this)
    }

    fun getSex() : Sex {
        return requireArguments().let {
            it.getSerializable(BodyFragment.ARG_SEX) as Sex
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.buttonChangeSides.setOnClickListener {
            source.onNext(UIEventMainBody.ChangeSideClicked)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume")
        val frontFragment = childFragmentManager.findFragmentById(R.id.firstContainer) as BodyFragment
        val backFragment = childFragmentManager.findFragmentById(R.id.secondContainer) as BodyFragment

        binding.setupInnerFeatures(frontFragment.feature, backFragment.feature)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun accept(t: BodyMainFeature.State?) {
        Log.d("TAG","accept new state in mainBody $t")
        if (t == null) {
            return
        }

        if (childFragmentManager.findFragmentById(R.id.firstContainer) == null) {
            Log.d("TAG", "add fragments")
            val frontFragment = BodyFragment.newInstance(t.sex, Side.FRONT)
            val backFragment = BodyFragment.newInstance(t.sex, Side.BACK)
            childFragmentManager.beginTransaction()
                .add(R.id.firstContainer, frontFragment)
                .add(R.id.secondContainer, backFragment)
                .commit()

        } else {
            Log.d("TAG", "${childFragmentManager.findFragmentById(R.id.firstContainer)}")
        }

        buttonChangeSides.isClickable = t.active

        if (t.side == Side.FRONT) {
            focusFront(!t.active)
        } else {
            focusBack(!t.active)
        }
    }

    private val animatorsChangeSide = AnimatorSet().apply {
        addListener({
            Log.d("TAG", "Animation ended")
            source.onNext(UIEventMainBody.FocusedSide)
        }, {}, {}, {})
    }

    private fun frontOnTop(): Boolean {
        return firstContainer.width == view?.measuredWidth
    }

    private fun focusFront(animated : Boolean) {
        if (!isAdded) {
            return
        }
        if (animatorsChangeSide.isRunning) {
            animatorsChangeSide.pause()
        }

        var fullWidth = requireView().measuredWidth
        var fullHeight = requireView().measuredHeight
        val smallWidth = resources.getDimension(R.dimen.bodies_small_view_width).toInt()
        val smallHeight = resources.getDimension(R.dimen.bodies_small_view_height).toInt()
        if (fullWidth == 0 || fullHeight == 0) {
            val parentWidth = (requireView().parent as View).width
            val parentHeight = (requireView().parent as View).height
            if (requireView().layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT
                && requireView().layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                fullWidth = parentWidth
                fullHeight = parentHeight
            } else {
                requireView().measure(parentWidth, parentHeight)
                fullHeight = requireView().measuredHeight
                fullWidth = requireView().measuredWidth
            }
        }

        if (animated) {
            animatorsChangeSide.playTogether(
                createChangeAnimation(fullHeight, smallHeight,
                    fullWidth, smallWidth,
                    0f, 0f,
                    secondContainer, firstContainer)
            )
            animatorsChangeSide.start()
        } else {
            firstContainer.layoutParams.width = fullWidth
            firstContainer.layoutParams.height = fullHeight
            firstContainer.requestLayout()
            secondContainer.layoutParams.width = smallWidth
            secondContainer.layoutParams.height = smallHeight
            secondContainer.requestLayout()
        }
    }

    private fun focusBack(animated: Boolean) {
        if (!isAdded) {
            return
        }
        if (animatorsChangeSide.isRunning) {
            animatorsChangeSide.pause()
        }

        var fullWidth = requireView().measuredWidth
        var fullHeight = requireView().measuredHeight
        val smallWidth = resources.getDimension(R.dimen.bodies_small_view_width).toInt()
        val smallHeight = resources.getDimension(R.dimen.bodies_small_view_height).toInt()

        if (fullWidth == 0 || fullHeight == 0) {
            val parentWidth = (requireView().parent as View).width
            val parentHeight = (requireView().parent as View).height
            if (requireView().layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT
                && requireView().layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                fullWidth = parentWidth
                fullHeight = parentHeight
            } else {
                requireView().measure(parentWidth, parentHeight)
                fullHeight = requireView().measuredHeight
                fullWidth = requireView().measuredWidth
            }
        }
        if (animated) {
            animatorsChangeSide.playTogether(
                createChangeAnimation(fullHeight, smallHeight,
                    fullWidth, smallWidth,
                    0f, 0f,
                    firstContainer, secondContainer)
            )
            animatorsChangeSide.start()
        } else {
            secondContainer.layoutParams.width = fullWidth
            secondContainer.layoutParams.height = fullHeight
            secondContainer.requestLayout()
            firstContainer.layoutParams.width = smallWidth
            firstContainer.layoutParams.height = smallHeight
            firstContainer.requestLayout()
        }
    }

    private fun createChangeAnimation(
        fromHeight: Int, toHeight: Int,
        fromWidth: Int, toWidth: Int,
        fromElevation: Float, toElevation: Float,
        fromView: View, toView: View,
    ) : ArrayList<Animator> {
        return arrayListOf(
            ValueAnimator.ofObject(WidthEvaluator(fromView), fromView.width, toWidth).apply { duration = 500 },
            ValueAnimator.ofObject(HeightEvaluator(fromView), fromView.height, toHeight).apply { duration = 450 },
            ValueAnimator.ofObject(WidthEvaluator(toView), toView.width, fromWidth).apply { duration = 500 },
            ValueAnimator.ofObject(HeightEvaluator(toView), toView.height, fromHeight).apply { duration = 450 },
            ObjectAnimator.ofFloat(fromView, "elevation", fromElevation, toElevation).apply { duration = 500 },
            ObjectAnimator.ofFloat(toView, "elevation", toElevation, fromElevation).apply { duration = 500 }
        )
    }

    private class WidthEvaluator(private val view: View) : IntEvaluator() {
        override fun evaluate(fraction: Float, startValue: Int?, endValue: Int?): Int {
            val num = super.evaluate(fraction, startValue, endValue) as Int
            val params: ViewGroup.LayoutParams = view.layoutParams
            params.width = num
            view.requestLayout()
            return num
        }
    }

    private class HeightEvaluator(private val view: View) : IntEvaluator() {
        override fun evaluate(fraction: Float, startValue: Int?, endValue: Int?): Int {
            val num = super.evaluate(fraction, startValue, endValue) as Int
            val params: ViewGroup.LayoutParams = view.layoutParams
            params.height = num
            view.requestLayout()
            return num
        }
    }


    companion object {

        const val ARG_SEX = "sex"

        @JvmStatic
        fun newInstance(sex: Sex) =
            BodyMainFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SEX, sex)
                }
            }
    }
}