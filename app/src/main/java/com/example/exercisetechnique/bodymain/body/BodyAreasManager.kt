package com.example.exercisetechnique.bodymain.body

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.ImageView
import com.example.exercisetechnique.R
import com.example.exercisetechnique.model.BodyPart
import com.example.exercisetechnique.model.Muscle
import com.pixplicity.sharp.OnSvgElementListener
import com.pixplicity.sharp.Sharp
import kotlinx.android.synthetic.main.fragment_body.view.*
import uk.co.senab.photoview.DefaultOnDoubleTapListener
import uk.co.senab.photoview.PhotoViewAttacher

class BodyAreasManager(
    val areasImageView: ImageView,
    val bodyImageView: ImageView,
    val listener: OnBodyPartSelectedListener
) {

    private lateinit var mSvg: Sharp
    private var canvasBoundsTemp: RectF? = null
    private lateinit var mAttacher: PhotoViewAttacher
    var bodyParts = HashMap<String,BodyPart>()
    private var selectedId: Muscle? = null

    fun showed() = ::mAttacher.isInitialized

    fun showManFront() {
        showBody(R.raw.man_front_muscles, R.drawable.man_front)
    }

    fun showManBack() {
        showBody(R.raw.man_back_muscles, R.drawable.man_back)
    }

    fun showWomanFront() {
        showBody(R.raw.woman_front_muscles, R.drawable.woman_front)
    }

    fun showWomanBack() {
        showBody(R.raw.woman_back_muscles, R.drawable.woman_back)
    }

    fun updateSelectedId(muscle: Muscle?) {
        val shouldUpdate = muscle != selectedId
        selectedId = muscle
        if (shouldUpdate) {
            refreshSvg()
        }
    }

    private fun showBody(bodyClickable : Int, bodyFull: Int) {
        mAttacher = PhotoViewAttacher(areasImageView)
        mAttacher.setOnDoubleTapListener(object : DefaultOnDoubleTapListener(mAttacher) {
            override fun onDoubleTap(ev: MotionEvent?): Boolean {
                onSingleTapConfirmed(ev)
                onSingleTapConfirmed(ev)
                return false
            }
        })
        mAttacher.onPhotoTapListener =
            PhotoViewAttacher.OnPhotoTapListener { _, x, y ->
                onImageClicked(x,y)
            }
        mSvg = Sharp.loadResource(areasImageView.resources, bodyClickable)
        mSvg.into(areasImageView)
        bodyImageView.setImageResource(bodyFull)
        refreshSvg()
    }


    private fun onImageClicked(x: Float, y: Float) {
        var canHandleClick = false
        val sCoord: PointF = toImageBound(x, y)
        val lastSelectedMuscle = selectedId
        var countInBounds = 0
        var lastMuscleInBounds : Muscle? = null
        for (item in bodyParts) {
            val bodyArea = item.value
            if (bodyArea.bounds.contains(sCoord.x, sCoord.y)) {
                countInBounds++
                lastMuscleInBounds = bodyArea.muscle
                if (bodyArea.path != null) {
                    val tempPath = Path() // Create temp Path
                    tempPath.moveTo(sCoord.x, sCoord.y) // Move cursor to point
                    val rectangle = RectF(sCoord.x,
                        sCoord.y,
                        sCoord.x + 0.5f,
                        sCoord.y + 0.5f) // create rectangle with size 2xp
                    tempPath.addRect(rectangle, Path.Direction.CW) // add rect to temp path
                    tempPath.op(bodyArea.path,
                        Path.Op.DIFFERENCE) // get difference with our PathToCheck
                    if (tempPath.isEmpty) {
                        selectedId = bodyArea.muscle
                        canHandleClick = true
                        break
                    }
                }

            }
        }

        if (!canHandleClick && countInBounds == 1 && lastMuscleInBounds != null) {
            canHandleClick = true
            selectedId = lastMuscleInBounds
        }

        if (canHandleClick) {
            refreshSvg()
            listener.onMuscleSelected(selectedId!!)
        }
    }

    private fun refreshSvg() {
        mSvg.setOnElementListener(object : OnSvgElementListener {
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
                if (paint != null && id != null) {
                    canvasBoundsTemp = canvasBounds

                    val justClickableArea = id.contains("AREA")
                    val tempId = id.replace(("[0-9]").toRegex(), "").replace("_AREA","")
                    val muscle = Muscle.valueOf(tempId)
                    if (element is Path) {
                        bodyParts[id] = BodyPart(muscle, element as Path, RectF(elementBounds))
                    } else {
                        bodyParts[id] = BodyPart(muscle, null, RectF(elementBounds))
                    }
                    if (selectedId == muscle && !justClickableArea) {
                        paint.color = Color.parseColor("#A0FF0000")
                    }
                }
                return element
            }

            override fun <T : Any?> onSvgElementDrawn(p0: String?, p1: T, p2: Canvas, p3: Paint?) {}


        })
        mSvg.getSharpPicture { picture ->
            val drawable: Drawable = picture.getDrawable(areasImageView.imageBody)
            areasImageView.setImageDrawable(drawable)
        }
    }

    private fun toImageBound(x: Float, y: Float): PointF {
        return PointF(x * this.canvasBoundsTemp!!.right, y * this.canvasBoundsTemp!!.bottom)
    }


}

interface OnBodyPartSelectedListener {

    fun onMuscleSelected(muscle: Muscle)
}