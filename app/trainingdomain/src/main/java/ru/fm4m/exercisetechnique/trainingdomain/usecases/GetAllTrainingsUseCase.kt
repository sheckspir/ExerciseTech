package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository
import javax.inject.Inject

interface GetAllTrainingsUseCase : IUseCase<AllTrainingsParam, List<UserTraining>>

class AllTrainingsParam(val userId : Int)

class GetAllTrainingsUseCaseImpl @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    override val scope: CoroutineScope) : GetAllTrainingsUseCase {

    override suspend fun protectedExecute(params: AllTrainingsParam): List<UserTraining> {
        delay(1000)
        return exerciseRepository.getTrainings(params.userId)
    }
}