package ru.fm4m.exercisetechnique.trainingdomain.usecases

import ru.fm4m.exercisetechnique.trainingdomain.data.ParamFinishTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository

interface FinishTrainingUseCase : IUseCase<ParamFinishTraining, Unit>

class FinishTrainingUseCaseImpl constructor(
    private val repository: ExerciseRepository
) : FinishTrainingUseCase {


    override fun protectedExecute(params: ParamFinishTraining) {
        params.userTraining.finished = params.finish
        repository.saveTraining(params.userTraining)
    }
}