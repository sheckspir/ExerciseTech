package com.example.exercisetechnique.body

import android.widget.ImageView
import at.lukle.clickableareasimage.*
import com.example.exercisetechnique.R
import com.example.exercisetechnique.model.BodyPart
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.model.Side
import uk.co.senab.photoview.PhotoViewAttacher

class BodyAreasManager(
    private val imageView : ImageView,
    private val clickLister: OnClickableAreaClickedListener<BodyPart>
) {

    private lateinit var areasClickable : ClickableAreasImage

    fun showManFront() {
//        val sex = Sex.MALE
//        val side = Side.FRONT
//        imageView.setImageResource(R.drawable.man_front)
//
//        areasClickable = ClickableAreasImage(PhotoViewAttacher(imageView), clickLister)
//
//        areasClickable.clickableAreas = ArrayList<AbstractArea<BodyPart>>().apply {
//            add(ClickablePolyArea(BodyPart(Muscle.NECK,sex, side), PixelPosition(278, 90), PixelPosition(284,142), PixelPosition(307,147), PixelPosition(332,142), PixelPosition(336,90)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.TRAPECIA, sex, side), PixelPosition(277, 106), PixelPosition(283, 139), PixelPosition(250, 136), PixelPosition(237, 127)))
//            add(ClickablePolyArea(BodyPart(Muscle.TRAPECIA, sex, side), PixelPosition(337, 106), PixelPosition(331, 139), PixelPosition(364, 136), PixelPosition(377, 127)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.CHEST,sex, side), PixelPosition(270, 142), PixelPosition(237,157), PixelPosition(230,172), PixelPosition(235,211), PixelPosition(254,224), PixelPosition(307,217),
//                PixelPosition(359,224),PixelPosition(379,211),PixelPosition(383,172), PixelPosition(373,157), PixelPosition(352, 142), PixelPosition(307,149)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.DELTA, sex, side), PixelPosition(273, 141), PixelPosition(233, 130), PixelPosition(203,140), PixelPosition(188, 163), PixelPosition(188, 205), PixelPosition(198, 205), PixelPosition(225, 173), PixelPosition(240, 153)))
//            add(ClickablePolyArea(BodyPart(Muscle.DELTA, sex, side), PixelPosition(341, 141), PixelPosition(381, 130), PixelPosition(411,140), PixelPosition(426, 163), PixelPosition(426, 205), PixelPosition(406, 205), PixelPosition(389, 173), PixelPosition(374, 153)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.BICEPS, sex, side), PixelPosition(227,180), PixelPosition(226, 228), PixelPosition(205, 289), PixelPosition(191, 280), PixelPosition(179, 280), PixelPosition(168, 254), PixelPosition(176, 221), PixelPosition(186, 203), PixelPosition(188, 216)))
//            add(ClickablePolyArea(BodyPart(Muscle.BICEPS, sex, side), PixelPosition(387,180), PixelPosition(389, 228), PixelPosition(409, 289), PixelPosition(423, 280), PixelPosition(435, 280), PixelPosition(446, 254), PixelPosition(438, 221), PixelPosition(428, 203), PixelPosition(424, 216)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.PREDPLECHIA, sex, side), PixelPosition(207,294), PixelPosition(164, 367), PixelPosition(133, 358), PixelPosition(150, 282), PixelPosition(165, 254), PixelPosition(179, 283)))
//            add(ClickablePolyArea(BodyPart(Muscle.PREDPLECHIA, sex, side), PixelPosition(407,294), PixelPosition(450, 367), PixelPosition(481, 358), PixelPosition(464, 282), PixelPosition(449, 254), PixelPosition(435, 283)))
//
//
//            add(ClickablePolyArea(BodyPart(Muscle.PRESS,sex, side), PixelPosition(307,218),
//                PixelPosition(266, 228), PixelPosition(269,314), PixelPosition(283,387), PixelPosition(300,425),
//                PixelPosition(307,426),
//                PixelPosition(314,425), PixelPosition(331,387),PixelPosition(345,314),PixelPosition(348, 228)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.KOSS_PRESS, sex, side), PixelPosition(230, 245), PixelPosition(265, 229), PixelPosition(269, 315), PixelPosition(279, 360), PixelPosition(240, 338)))
//            add(ClickablePolyArea(BodyPart(Muscle.KOSS_PRESS, sex, side), PixelPosition(384, 245), PixelPosition(349, 229), PixelPosition(345, 315), PixelPosition(335, 360), PixelPosition(374, 338)))
//
//            add(ClickablePolyArea(BodyPart(Muscle.KVADR, sex, side), PixelPosition(260, 365), PixelPosition(220, 440), PixelPosition(215, 495), PixelPosition(230, 560), PixelPosition(285, 575), PixelPosition(300,475)))
//            // TODO: 9/10/21
//            add(ClickablePolyArea(BodyPart(Muscle.KVADR, sex, side), PixelPosition(260, 365), PixelPosition(220, 440), PixelPosition(215, 495), PixelPosition(230, 560), PixelPosition(285, 575), PixelPosition(300,475)))
//
//        }

    }
}