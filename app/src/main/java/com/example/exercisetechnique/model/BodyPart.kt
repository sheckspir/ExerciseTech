package com.example.exercisetechnique.model

import android.graphics.Path
import android.graphics.RectF

class BodyPart(
    val muscle: Muscle,
    val sex: Sex,
    val side: Side,
    val path : Path,
    val bounds : RectF
    )