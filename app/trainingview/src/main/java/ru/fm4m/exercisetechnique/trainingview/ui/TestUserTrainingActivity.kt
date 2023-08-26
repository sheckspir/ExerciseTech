package ru.fm4m.exercisetechnique.trainingview.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.trainingdata.data.StubTraining
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachCountable
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachTimed
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.OneTrainingScreen
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewData
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewExerciseCountable
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewExerciseTimed
import ru.fm4m.exercisetechnique.trainingview.ui.theme.ExerciseTechniqueTheme
import java.util.Date
import kotlin.coroutines.CoroutineContext

class TestUserTrainingActivity : ComponentActivity(), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO + CoroutineName("main name") + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e("TAG1", "error in coroutine $coroutineContext", throwable)
        }


    val stubTraining = StubTraining(1, Date())

    val state = mutableStateOf(stubTraining)
    val lifeData = MutableLiveData(stubTraining)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val workManager1 = WorkManagerCoroutinesStub("1")

        workManager1.startEmitInSharedFlow(500)

        launch {
            delay(1000)
            workManager1.onSharedFlowObserve {
                Log.d("TAG", "onSharedFlowObserve2 $it")
            }
        }





        val state2 = mutableStateOf(TrainingViewData.create())

        setContent {
            ExerciseTechniqueTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    OneTrainingScreen(userTrainingState = state2) {

                        val oldExerciseList = state2.value.exercises


                        for (i in 0 until oldExerciseList.size) {
                            val exerciseFirst = oldExerciseList[i]
                            if (exerciseFirst.id == it) {
                                val newExercise = when (exerciseFirst) {
                                    is TrainingViewExerciseTimed -> {
                                        exerciseFirst.approaches.add(
                                            ExerciseApproachTimed(
                                                0,
                                                0f,
                                                1f
                                            )
                                        )
                                        exerciseFirst.copy(approaches = exerciseFirst.approaches)
                                    }

                                    is TrainingViewExerciseCountable -> {
                                        exerciseFirst.approaches.add(
                                            ExerciseApproachCountable(
                                                0,
                                                0f,
                                                1f
                                            )
                                        )
                                        exerciseFirst.copy(approaches = exerciseFirst.approaches)
                                    }

                                    else -> {
                                        null
                                    }
                                }

                                if (newExercise != null) {
                                    oldExerciseList[i] = newExercise
                                }
                                break
                            }
                        }


//                        val newExercise: TrainingViewExercise? =
//                            when(exerciseFirst) {
//                                is TrainingViewExerciseTimed -> {
//                                    exerciseFirst.copy(planApproach = exerciseFirst.planApproach+1)
//                                }
//                                is TrainingViewExerciseCountable -> {
//                                    exerciseFirst.copy(planApproach = exerciseFirst.planApproach+1)
//                                }
//
//                                else -> {null}
//                            }
//                        if (newExercise != null) {
//                            oldExerciseList[0] = newExercise
////                            state2.value = state2.value.copy(exercises = oldExerciseList)
//                        }

                    }
//                    OneTrainingScreen(lifeData, state2) { exerciseId ->
//                        val userTraining = StubTraining(1, Date())
//
//                        val exercises = lifeData.value?.exercises
//                        val thisExercise = exercises?.find { it.id == exerciseId }
//                        if (thisExercise != null) {
//                            when(thisExercise) {
//                                is CountableTrainingExercise -> {
//                                    thisExercise.approaches.add(ExerciseApproachCountable(10,1f,1f))
//
//                                    userTraining.exercises.clear()
//                                    userTraining.exercises.addAll(exercises)
//
//                                    userTraining.trainingName = "Тренировка №${thisExercise.approaches.size}"
//                                    lifeData.value = userTraining
//                                    Log.d("TAG", "add exercise countable to $thisExercise")
//                                }
//                                is TimedTrainingExercise -> {
//                                    thisExercise.approaches.add(ExerciseApproachTimed(10, 0f, 1f))
//                                    Log.d("TAG", "add exercise timed to $thisExercise")
//                                }
//                            }
//
//                        }
//                    }
                }
            }
        }
    }
}