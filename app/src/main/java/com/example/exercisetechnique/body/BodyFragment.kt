package com.example.exercisetechnique.body

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uk.co.senab.photoview.PhotoViewAttacher
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener


class BodyFragment : Fragment() {

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


    private var mSvg: Sharp? = null
    private var canvasBoundsTemp: RectF? = null
    private var mAttacher: PhotoViewAttacher? = null
    var bodyParts = HashSet<BodyPart>()
    private var selectedId: Muscle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_body, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        manager = BodyAreasManager(view.imageBody, OnClickableAreaClickedListener {
//            Log.d("TAG", "clicked part ${it.muscle}")
//        })


        mAttacher = PhotoViewAttacher(view.imageBody)
//        mAttacher!!.maximumScale = 10f
        mAttacher!!.onPhotoTapListener =
            OnPhotoTapListener { _, x, y ->
                var canHandleClick = false
                val sCoord: PointF = toImageBound(x, y)
                for (body in bodyParts) {
                    val rectF = RectF()
                    body.path.computeBounds(rectF, true)
                    Log.d("TAG", "on photo tapped ")
                    if (body.bounds.contains(sCoord.x, sCoord.y)) {
                        canHandleClick = true
                        selectedId = body.muscle
                        Log.d("TAG", "clicked on muscle $selectedId")
                    }
                }
                if (canHandleClick) refreshSvg()
            }
        mSvg = Sharp.loadResource(resources, R.raw.body3)
        refreshSvg()


    }

    private fun refreshSvg() {
        mSvg!!.setOnElementListener(object : OnSvgElementListener {
            override fun onSvgStart(p0: Canvas, p1: RectF?) {}

            override fun onSvgEnd(p0: Canvas, p1: RectF?) {}

            override fun <T : Any?> onSvgElement(
                id: String?,
                element: T,
                elementBounds: RectF?,
                canvas: Canvas,
                canvasBounds: RectF?,
                paint: Paint?,
            ): T {
//                if (paint != null && paint.style === Paint.Style.FILL && id != null) {
                Log.d("TAG", "onSvgElement $id")
                if (paint != null && id != null) {
                    canvasBoundsTemp = canvasBounds
                    bodyParts.add(BodyPart(Muscle.valueOf(id), sex, side, element as Path, RectF(elementBounds)))
                    if (selectedId?.name == id) {
                        paint.color = Color.RED
                    }
                }
                return element
            }

            override fun <T : Any?> onSvgElementDrawn(p0: String?, p1: T, p2: Canvas, p3: Paint?) {}


        })
        mSvg!!.getSharpPicture { picture ->
            Log.d("TAG", "sharp get drawable");
            val drawable: Drawable = picture.getDrawable(requireView().imageBody)
            requireView().imageBody.setImageDrawable(drawable)
//            mAttacher?.update();
        }
    }

    @NonNull
    private fun toImageBound(x: Float, y: Float): PointF {
        return PointF(x * this.canvasBoundsTemp!!.right, y * this.canvasBoundsTemp!!.bottom)
    }

    fun showManFront() {
//        manager.showManFront()
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