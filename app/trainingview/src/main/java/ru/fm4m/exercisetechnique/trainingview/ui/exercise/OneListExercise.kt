package ru.fm4m.exercisetechnique.trainingview.ui.exercise

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewExercise

@Composable
fun OneListExercise(
    exercise: TrainingViewExercise,
    modifier: Modifier = Modifier,
    onExerciseClick: ((exerciseId: Int) -> Unit)? = null) {

    var mainModifier : Modifier = modifier
//    if (onExerciseClick != null) {
//        mainModifier = mainModifier.clickable { onExerciseClick.invoke(exercise.id) }
//    }
    Row(modifier = mainModifier)
        {
        AsyncImage(
            model = "https://free-png.ru/wp-content/webp-express/webp-images/uploads/2022/01/free-png.ru-692-340x340.png.webp",
            contentDescription = "android",
            modifier = Modifier
                .width(Dp(40f))
                .height(Dp(40f))
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
        ) {
            Text(text = exercise.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)


            val approaches = remember{exercise.approaches}
            val approachSize = approaches.size
            val itemsSize = if (exercise.planApproach > approachSize) {
                exercise.planApproach
            } else {
                approachSize
            }

            ApproachListShow(approaches.size, itemsSize)

        }
    }

}

@Composable
@Preview
fun OneListExercisePreview() {
    OneListExercise(exercise = TrainingViewExercise.create() )
}