package ru.fm4m.exercisetechnique.trainingview.ui.programs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import ru.fm4m.exercisetechnique.trainingdomain.data.UserTraining
import ru.fm4m.exercisetechnique.trainingdomain.usecases.AllTrainingsParam
import ru.fm4m.exercisetechnique.trainingdomain.usecases.DataState
import ru.fm4m.exercisetechnique.trainingdomain.usecases.GetAllTrainingsUseCase
import ru.fm4m.exercisetechnique.trainingdomain.usecases.GetUserIdUseCase
import ru.fm4m.exercisetechnique.trainingdomain.usecases.RemoveTrainingParam
import ru.fm4m.exercisetechnique.trainingdomain.usecases.RemoveTrainingUseCase
import javax.inject.Inject

class AvailableProgramsViewModel private constructor(
    private val allTrainingsUseCase: GetAllTrainingsUseCase,
    private val removeTrainingUseCase: RemoveTrainingUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
) : ViewModel() {

    companion object {
        class Factory @Inject constructor(
            private val allTrainingsUseCase: GetAllTrainingsUseCase,
            private val removeTrainingUseCase: RemoveTrainingUseCase,
            private val getUserIdUseCase: GetUserIdUseCase,
        ) : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass == AvailableProgramsViewModel::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    return AvailableProgramsViewModel(
                        allTrainingsUseCase,
                        removeTrainingUseCase,
                        getUserIdUseCase
                    ) as T
                } else {
                    return super.create(modelClass)
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(AvailableProgramsState())

    val uiState = _uiState.asStateFlow()

    val programClickListener = object : ProgramClickListener {
        override fun onProgramClicked(id: String) {
            deleteFromUserTraining(id)
        }
    }

    fun deleteFromUserTraining(trainingId: String) {

        val flow = getUserIdUseCase()
            .flatMapConcat {
                deleteFromUserTraining(it, trainingId)
            }
        mapDataAndStart(flow)

    }

    fun restoreAllUserTraining() {
        val flow = getUserIdUseCase()
            .flatMapConcat {
                getActualList(it)
                    .flowOn(Dispatchers.IO)
            }
        mapDataAndStart(flow)
    }


    private suspend fun deleteFromUserTraining(
        userId: Int,
        trainingId: String,
    ): Flow<DataState<List<UserTraining>>> {
        return removeTrainingUseCase(RemoveTrainingParam(userId, trainingId))
            .flatMapConcat {
                return@flatMapConcat when (it) {
                    is DataState.Loading -> flowOf(DataState.Loading<List<UserTraining>>(it.loading))
                    is DataState.Problem -> flowOf(DataState.Problem<List<UserTraining>>(it.e))
                    is DataState.Result -> getActualList(userId)
                }
            }
    }

    private suspend fun getActualList(userId: Int): Flow<DataState<List<UserTraining>>> {
        return allTrainingsUseCase(AllTrainingsParam(userId))
    }

    private fun mapDataAndStart(flow: Flow<DataState<List<UserTraining>>>) {
        flow.onEach {
            Log.d("TAG", "onEach mapDataAndStart $it")
            val newState = when (it) {
                is DataState.Loading -> _uiState.value.copy(loadingTraining = it.loading)
                is DataState.Problem -> _uiState.value.copy(
                    loadingTraining = false,
                    throwable = it.e
                )

                is DataState.Result -> _uiState.value.copy(
                    loadingTraining = false,
                    userTrainings = it.data,
                    throwable = null
                )
            }
            _uiState.emit(newState)
        }.shareIn(viewModelScope, SharingStarted.Eagerly, 1)
    }


}

data class AvailableProgramsState(
    val loadingTraining: Boolean = false,
    val userTrainings: List<UserTraining> = emptyList(),
    val throwable: Throwable? = null,
)

interface ProgramClickListener {
    fun onProgramClicked(id: String)
}