package ru.fm4m.exercisetechnique.trainingdomain.repository

import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproach
import ru.fm4m.exercisetechnique.trainingdomain.data.TrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import java.util.Date

interface ExerciseRepository {

    suspend fun removeTrainings(trainingId: String, userId: Int) : Boolean

    suspend fun getTrainings(userId: Int) : List<UserTraining>

    suspend fun saveExercise(trainingId: String, exercise: TrainingExercise) : Boolean

    suspend fun saveTraining(userTraining: UserTraining): Boolean

    suspend fun getTraining(userId: Int, programId : Int, date: Date) : UserTraining

    suspend fun getExercise(userTrainingId: String, exerciseId: Int) : TrainingExercise?

    suspend fun changeApproaches(trainingId : String, exerciseId: Int, approaches: List<ExerciseApproach>) : Boolean

}