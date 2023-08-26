package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import ru.fm4m.exercisetechnique.trainingdomain.data.ParamSaveExercise
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository

interface SaveExerciseUseCase : IUseCase<ParamSaveExercise, Unit>

class SaveExerciseUseCaseImpl(
    private val repository: ExerciseRepository,
    override val scope: CoroutineScope,
) : SaveExerciseUseCase {

    override suspend fun protectedExecute(params: ParamSaveExercise) {
        repository.saveExercise(params.userTrainingId, params.exercise)
    }
}