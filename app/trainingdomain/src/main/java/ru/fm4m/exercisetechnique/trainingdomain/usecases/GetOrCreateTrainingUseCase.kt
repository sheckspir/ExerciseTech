package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.trainingdomain.data.ParamsUserTraining
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository
import java.lang.Exception

interface IUseCase<Params, Result> {

    fun execute(
        params: Params,
        errorCallback: Callback<Throwable>,
        progressCallback: ProgressShowing,
        dataCallback: Callback<Result>,
    ) {
        scope.launch {
            progressCallback.showProgress(true)
            try {
                val result = protectedExecute(params)
                dataCallback.result(result)
                progressCallback.showProgress(false)
            } catch (e: Throwable) {
                progressCallback.showProgress(false)
                errorCallback.result(e)
            }
        }
    }

    operator fun invoke(params: Params): Flow<DataState<Result>>{
        return flow {
            try {
                val result = protectedExecute(params)
                emit(DataState.Result(result))
            } catch (e: Exception) {
                emit(DataState.Problem(e))
            }
        }.flowOn(Dispatchers.IO //todo хорошая практика передать сюда диспатчер провайдера, чтобы можно быол менять эту логику
        ).onStart {
            emit(DataState.Loading(true))
        }
    }


    val scope: CoroutineScope

    suspend fun protectedExecute(params: Params): Result

}

sealed class DataState<R> {
    class Loading<R>(val loading: Boolean) : DataState<R>()
    class Problem<R>(val e: Throwable) : DataState<R>()
    class Result<R>(val data: R) : DataState<R>()
}

interface ProgressShowing {
    fun showProgress(show: Boolean)
}

interface Callback<Result> {
    fun result(data: Result)
}

interface GetOrCreateTrainingUseCase : IUseCase<ParamsUserTraining, UserTraining>

class GetOrCreateTrainingUseCaseImpl(
    private val repository: ExerciseRepository,
    override val scope: CoroutineScope,
) : GetOrCreateTrainingUseCase {

    override suspend fun protectedExecute(params: ParamsUserTraining): UserTraining {
        return repository.getTraining(params.userId, params.programId, params.date)
    }
}