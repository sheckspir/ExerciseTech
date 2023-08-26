package ru.fm4m.exercisetechnique.trainingview.ui.onetraining

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.fm4m.exercisetechnique.trainingview.R
import ru.fm4m.exercisetechnique.trainingview.ui.exercise.exerciseList

@Composable
fun OneTrainingScreen(
    userTrainingState: State<TrainingViewData>,
    onExerciseClick: ((exerciseId: Int) -> Unit)? = null,
) {
//    Log.d("TAG", "OneTrainingScreen.init")
    val userTraining = remember {
        userTrainingState.value
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = userTraining.programName,
            fontSize = 32.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = dimensionResource(id = (R.dimen.button_regular_height)))
                .clickable { onExerciseClick?.invoke(0) },
            textAlign = TextAlign.Center

        )

        Text(
            text = userTraining.trainingName,
            fontSize = 20.sp,
            modifier = Modifier
        )

        val exercises = remember {
            userTraining.exercises
        }
        LazyColumn {

            exerciseList(exercises, onExerciseClick)
//                contentType = { "exerciseShort" },
//                count = exercises.size,
//                key = {
//                    exercises[it].id
//                },
//                itemContent = {index ->
//                    OneListExercise(
//                        exercise = exercises[index],
//                        modifier = Modifier.fillMaxWidth(),
//                        onExerciseClick = onExerciseClick
//                    )
//                }
//            )
        }
//        LazyColumn(
//            state = rememberLazyListState(),
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.Start)
//                .width(Dp(100f))
//            ,
//            horizontalAlignment = Alignment.Start,
//            userScrollEnabled = true
//        ) {
//            items(items = userTraining.exercises, itemContent = {
//                OneListExercise(
//                    exercise = it,
//                    modifier = Modifier.fillMaxWidth(),
//                    onExerciseClick = onExerciseClick
//                )
//            })
//
//        }

    }

}

@Preview(showBackground = true)
@Composable
fun OneTrainingScreenPreview() {
    OneTrainingScreen(
        remember {
            mutableStateOf(TrainingViewData.create())
        }
    )

}

