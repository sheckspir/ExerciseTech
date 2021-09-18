package com.example.exercisetechnique.body

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.exercisetechnique.R
import com.example.exercisetechnique.model.BodyPart
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.Side
import com.pixplicity.sharp.OnSvgElementListener
import com.pixplicity.sharp.Sharp
import kotlinx.android.synthetic.main.fragment_body.view.*
import uk.co.senab.photoview.DefaultOnDoubleTapListener
import uk.co.senab.photoview.PhotoViewAttacher
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener


class BodyFragment : Fragment(), OnBodyPartSelectedListener{

    private lateinit var sex : Sex
    private lateinit var side : Side

    private lateinit var manager : BodyAreasManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sex = it.getSerializable(ARG_SEX) as Sex
            side = it.getSerializable(ARG_SIDE) as Side
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

        // TODO: 9/16/21 temp call here
        when(sex) {
            Sex.MALE -> manager.showManFront()
            Sex.FEMALE -> manager.showWomanFront()
        }
    }





    fun showManFront() {
        manager.showManFront()
    }

    fun showManBack() {
        manager.showManBack()
    }

    override fun onMuscleSelected(muscle: Muscle) {
        Log.d("TAG", "onMuscleSelected $muscle")
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