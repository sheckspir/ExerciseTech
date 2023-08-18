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

class ExerciseRepositoryImpl() : ExerciseRepository {

    private var savedTraining : UserTraining? = null

    override fun saveTraining(userTraining: UserTraining) {
        savedTraining = userTraining
    }

    override fun saveExercise(trainingId: String, exercise: TrainingExercise) {
        TODO("Not yet implemented")
    }

    override fun getExercise(userTrainingId: String, exerciseId: Int): TrainingExercise {
        TODO("Not yet implemented")
    }

    override fun getTraining(userId: Int, programId: Int, date: Date): UserTraining {
        var localSaved = savedTraining
        if (localSaved?.userId != userId) {

            localSaved = StubTraining(userId, date)

            savedTraining = localSaved
        }
        return localSaved
    }

    override fun changeApproaches(
        trainingId: String,
        exerciseId: Int,
        approaches: List<ExerciseApproach>,
    ) {
        val localTraining = savedTraining
        if(localTraining?.id == trainingId) {
            val exercise = localTraining.exercises.find { it.id == exerciseId }
            if (exercise != null) {
                exercise.approaches.clear()
                when(exercise) {
                    is CountableTrainingExercise -> {
                        exercise.approaches.addAll(approaches as List<ExerciseApproachCountable>)
                    }
                    is TimedTrainingExercise -> {
                        exercise.approaches.addAll(approaches as List<ExerciseApproachTimed>)
                    }
                }
            }
        }
    }
}