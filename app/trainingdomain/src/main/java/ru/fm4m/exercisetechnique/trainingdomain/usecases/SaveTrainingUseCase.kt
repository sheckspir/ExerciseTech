package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository


interface SaveTrainingUseCase : IUseCase<UserTraining, Unit>

class SaveTrainingUseCaseImpl(
    private val repository: ExerciseRepository,
    override val scope: CoroutineScope
): SaveTrainingUseCase {

    override suspend fun protectedExecute(params: UserTraining) {
        repository.saveTraining(params)
    }
}