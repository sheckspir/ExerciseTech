package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.fm4m.exercisetechnique.trainingdomain.repository.AccountRepository
import javax.inject.Inject

interface GetUserIdUseCase {
    operator fun invoke(): Flow<Int>
}

class GetUserIdUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
) : GetUserIdUseCase {

    override operator fun invoke(): Flow<Int> {
        return flow {
            emit(repository.getUserId())
        }
    }
}