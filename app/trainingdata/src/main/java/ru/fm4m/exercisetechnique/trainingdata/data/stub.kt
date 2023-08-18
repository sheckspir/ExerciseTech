package ru.fm4m.exercisetechnique.trainingdata.data

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import ru.fm4m.exercisetechnique.trainingdomain.data.CountableTrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachTimed
import ru.fm4m.exercisetechnique.trainingdomain.data.TimedTrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.TrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import java.util.Date

data class StubTraining(
    override val userId : Int, override var date: Date) : UserTraining(
    id = "1",
    userId = userId,
    date = date,
    programName = "Супер крутая программа",
    trainingName = "Супер крутая тренировка в программе",
    exercises = ArrayList<TrainingExercise>().apply {
        add(CountableTrainingExercise(0, "Упражнение по счёту 1", 5, false, ArrayList()))
        add(TimedTrainingExercise(1, "Упражнение по времени 1", 5, false, ArrayList()))
        add(CountableTrainingExercise(2, "Упражнение по счёту 2", 5, true, ArrayList()))
        add(TimedTrainingExercise(30, "Упражнение по времени 2", 5, false, stubTimeApproach))
    }
)


private val stubTimeApproach = ArrayList<ExerciseApproachTimed>().apply {
    add(ExerciseApproachTimed(10, 0f, 1f))
    add(ExerciseApproachTimed(20, 0f, 1f))
}