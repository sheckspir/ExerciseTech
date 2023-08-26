package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository
import javax.inject.Inject

interface RemoveTrainingUseCase : IUseCase<RemoveTrainingParam, Unit>

class RemoveTrainingParam(
    val userId: Int,
    val trainingId: String
)

class RemoveTrainingUseCaseImpl @Inject constructor(
    val repository: ExerciseRepository,
    override val scope: CoroutineScope
) : RemoveTrainingUseCase {



    override suspend fun protectedExecute(params: RemoveTrainingParam) {
        repository.removeTrainings(params.trainingId, params.userId)
    }
}
