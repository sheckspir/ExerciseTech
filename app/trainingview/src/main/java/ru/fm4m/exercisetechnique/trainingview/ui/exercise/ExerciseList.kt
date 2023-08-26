package ru.fm4m.exercisetechnique.trainingview.ui.exercise

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewExercise

fun LazyListScope.exerciseList(
    exercises: List<TrainingViewExercise>,
    onExerciseClick: ((exerciseId: Int) -> Unit)? = null,
) {


    items(exercises,
        key = {
            it.id
        }) {
        OneListExercise(
            exercise = it,
            modifier = Modifier.fillMaxWidth(),
            onExerciseClick = onExerciseClick
        )
    }
}
