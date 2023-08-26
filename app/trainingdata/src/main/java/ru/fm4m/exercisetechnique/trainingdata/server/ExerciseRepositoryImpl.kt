package ru.fm4m.exercisetechnique.trainingdata.server

import ru.fm4m.exercisetechnique.trainingdata.data.StubTraining
import ru.fm4m.exercisetechnique.trainingdomain.data.CountableTrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproach
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachCountable
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachTimed
import ru.fm4m.exercisetechnique.trainingdomain.data.TimedTrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.TrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository
import java.util.Date
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(): ExerciseRepository {

    private val trainingMap = HashMap<String, UserTraining>()
    private var savedTraining: UserTraining? = null

    init {
        for (i in 0..5) {
            val stubTraining = StubTraining(id = i.toString(), userId = 1, date = Date())
            trainingMap[stubTraining.id] = stubTraining
        }
    }

    override suspend fun getTrainings(userId: Int): List<UserTraining> {
        return trainingMap.values.toList()
    }

    override suspend fun saveTraining(userTraining: UserTraining): Boolean {
        trainingMap[userTraining.id] = userTraining
        return true

    }

    override suspend fun removeTrainings(trainingId: String, userId: Int): Boolean {
        if (trainingMap.containsKey(trainingId)) {
            trainingMap.remove(trainingId)
            return true

        }
        return false
    }

    override suspend fun saveExercise(trainingId: String, exercise: TrainingExercise): Boolean {
        if (trainingMap.containsKey(trainingId)) {
            val exercises = trainingMap[trainingId]!!.exercises
            for ((index, value) in exercises.withIndex()) {
                if (value.id == exercise.id) {
                    exercises[index] = exercise
                    return true
                }
            }
        }
        return false
    }

    override suspend fun getExercise(userTrainingId: String, exerciseId: Int): TrainingExercise? {
        return trainingMap[userTrainingId]
            ?.exercises
            ?.find { it.id == exerciseId }
    }

    override suspend fun getTraining(userId: Int, programId: Int, date: Date): UserTraining {
        var localSaved = savedTraining
        if (localSaved?.userId != userId) {

            localSaved = StubTraining(userId, date)

            savedTraining = localSaved
        }
        return localSaved
    }

    override suspend fun changeApproaches(
        trainingId: String,
        exerciseId: Int,
        approaches: List<ExerciseApproach>,
    ): Boolean {
        val localTraining = savedTraining
        if (localTraining?.id == trainingId) {
            val exercise = localTraining.exercises.find { it.id == exerciseId }
            if (exercise != null) {
                exercise.approaches.clear()
                when (exercise) {
                    is CountableTrainingExercise -> {
                        @Suppress("UNCHECKED_CAST")
                        exercise.approaches.addAll(approaches as List<ExerciseApproachCountable>)
                        return true
                    }

                    is TimedTrainingExercise -> {
                        @Suppress("UNCHECKED_CAST")
                        exercise.approaches.addAll(approaches as List<ExerciseApproachTimed>)
                        return true
                    }
                }
            }
        }
        return false
    }
}