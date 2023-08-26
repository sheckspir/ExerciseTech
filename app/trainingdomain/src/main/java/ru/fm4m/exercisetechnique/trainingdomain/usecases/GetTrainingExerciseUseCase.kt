package ru.fm4m.exercisetechnique.trainingdomain.usecases


import kotlinx.coroutines.CoroutineScope
import ru.fm4m.exercisetechnique.trainingdomain.data.ParamGetTrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.data.TrainingExercise
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository

interface GetTrainingExerciseUseCase : IUseCase<ParamGetTrainingExercise, TrainingExercise?>

class GetTrainingExerciseUseCaseImpl(
    private val repository: ExerciseRepository,
    override val scope: CoroutineScope
) : GetTrainingExerciseUseCase {

    override suspend fun protectedExecute(params: ParamGetTrainingExercise): TrainingExercise? {
        return repository.getExercise(params.userTrainingId, params.exerciseId)
    }
}