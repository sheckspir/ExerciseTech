package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import ru.fm4m.exercisetechnique.trainingdomain.data.ParamFinishTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository

interface FinishTrainingUseCase : IUseCase<ParamFinishTraining, Unit>

class FinishTrainingUseCaseImpl constructor(
    private val repository: ExerciseRepository,
    override val scope: CoroutineScope
) : FinishTrainingUseCase {


    override suspend fun protectedExecute(params: ParamFinishTraining) {
        params.userTraining.finished = params.finish
        repository.saveTraining(params.userTraining)
    }
}