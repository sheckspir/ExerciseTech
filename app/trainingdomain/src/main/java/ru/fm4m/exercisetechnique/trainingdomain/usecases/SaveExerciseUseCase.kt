package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.trainingdomain.data.ParamSaveExercise
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository

interface SaveExerciseUseCase : IUseCase<ParamSaveExercise, Unit>

class SaveExerciseUseCaseImpl(
    private val repository: ExerciseRepository
) : SaveExerciseUseCase {

    override fun protectedExecute(params: ParamSaveExercise) {
        repository.saveExercise(params.userTrainingId, params.exercise)
    }
}