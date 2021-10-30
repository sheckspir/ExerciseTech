package com.example.exercisetechnique.bodymain

import android.animation.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import com.example.exercisetechnique.R
import com.example.exercisetechnique.body.BodyFragment
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.Side
import kotlinx.android.synthetic.main.fragment_body_main.*
import kotlinx.android.synthetic.main.fragment_body_main.view.*

class BodyMainFragment : Fragment() {

    private lateinit var sex : Sex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate MainBody")
        arguments?.let {
            sex = it.getSerializable(BodyFragment.ARG_SEX) as Sex
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_body_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded){
            Log.d("TAG", "onViewCreated")
            if (childFragmentManager.findFragmentById(R.id.firstContainer) == null) {
                childFragmentManager.beginTransaction()
                    .add(R.id.firstContainer, BodyFragment.newInstance(sex, Side.FRONT))
                    .add(R.id.secondContainer, BodyFragment.newInstance(sex, Side.BACK))
                    .commit()
            } else {
                Log.d("TAG", "${childFragmentManager.findFragmentById(R.id.firstContainer)}")
            }
        }

        view.buttonChangeSides.setOnClickListener {
            Log.d("TAG", "click changeSide")
            if (!animatorsChangeSide.isRunning) {
                changeSide()
            }

        }
    }

    val animatorsChangeSide = AnimatorSet().apply {
        addListener({
            Log.d("TAG","ended")
            buttonChangeSides.isClickable = true
        }, {
            Log.d("TAG","started")
            buttonChangeSides.isClickable = false
        }, {}, {})
    }

    private fun changeSide() {
        if (animatorsChangeSide.isRunning || !isAdded) {
            return
        }
        val fullWidth = requireView().width
        val fullHeight = requireView().height
        val smallWidth = resources.getDimension(R.dimen.bodies_small_view_width).toInt()
        val smallHeight = resources.getDimension(R.dimen.bodies_small_view_height).toInt()


        Log.d("TAG", "widthFull = $fullWidth firstWidth = ${firstContainer.width} measured = ${view?.measuredWidth}")
        if (firstContainer.width == view?.measuredWidth) {
            animatorsChangeSide.playTogether(
                createChangeAnimation(fullHeight, smallHeight,
                    fullWidth, smallWidth,
                    0f, 0f,
                    firstContainer, secondContainer)
            )
            animatorsChangeSide.start()
        } else {
            animatorsChangeSide.playTogether(
                createChangeAnimation(fullHeight, smallHeight,
                    fullWidth, smallWidth,
                    0f, 0f,
                    secondContainer, firstContainer)
            )
            animatorsChangeSide.start()
        }
    }

    private fun createChangeAnimation(
        fromHeight: Int, toHeight: Int,
        fromWidth: Int, toWidth: Int,
        fromElevation: Float, toElevation: Float,
        fromView: View, toView: View,
    ) : ArrayList<Animator> {
        return arrayListOf(
            ValueAnimator.ofObject(WidthEvaluator(fromView), fromWidth, toWidth).apply { duration = 500 },
            ValueAnimator.ofObject(HeightEvaluator(fromView), fromHeight, toHeight).apply { duration = 450 },
            ValueAnimator.ofObject(WidthEvaluator(toView), toWidth, fromWidth).apply { duration = 500 },
            ValueAnimator.ofObject(HeightEvaluator(toView), toHeight, fromHeight).apply { duration = 450 },
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
            Log.d("TAG", "current value $num endValue $endValue")
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