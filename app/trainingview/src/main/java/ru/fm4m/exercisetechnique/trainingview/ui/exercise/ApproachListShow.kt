package ru.fm4m.exercisetechnique.trainingview.ui.exercise

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ApproachListShow(
    approachSize : Int,
    itemsSize : Int
) {
    Row {
        for (i in 0 until itemsSize) {
            if (i < approachSize) {
                Text(text = "1")
            } else {
                Text(text = "0")
            }
        }
    }
}