package ru.fm4m.exercisetechnique.trainingdomain.repository

import kotlinx.coroutines.Job
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproach
import ru.fm4m.exercisetechnique.trainingdomain.data.TrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import java.util.Date

interface ExerciseRepository {

    fun saveExercise(trainingId: String, exercise: TrainingExercise)

    fun saveTraining(userTraining: UserTraining)

    fun getTraining(userId: Int, programId : Int, date: Date) : UserTraining

    fun getExercise(userTrainingId: String, exerciseId: Int) : TrainingExercise

    fun changeApproaches(trainingId : String, exerciseId: Int, approaches: List<ExerciseApproach>)

}