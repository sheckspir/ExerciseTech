package ru.fm4m.exercisetechnique.trainingdomain.usecases

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.trainingdomain.data.ParamsUserTraining
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository

interface IUseCase<Params, Result> {

    fun execute(params: Params, errorCallback : Callback<Throwable>, progressCallback: ProgressShowing, dataCallback: Callback<Result>) {
        GlobalScope.launch {
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

    fun protectedExecute(params: Params) : Result

}

interface ProgressShowing {
    fun showProgress(show: Boolean)
}

interface Callback<Result> {
    fun result(data : Result)
}

interface GetOrCreateTrainingUseCase : IUseCase<ParamsUserTraining, UserTraining>

class GetOrCreateTrainingUseCaseImpl(
    private val repository: ExerciseRepository
) : GetOrCreateTrainingUseCase {

    override fun protectedExecute(params: ParamsUserTraining): UserTraining {
        return repository.getTraining(params.userId, params.programId, params.date)
    }
}