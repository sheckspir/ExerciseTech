package ru.fm4m.exercisetechnique.trainingview.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.OneTrainingScreen
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewData
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewExerciseCountable
import ru.fm4m.exercisetechnique.trainingview.ui.onetraining.TrainingViewExerciseTimed
import ru.fm4m.exercisetechnique.trainingview.ui.theme.ExerciseTechniqueTheme
import ru.fm4m.exercisetechnique.trainingdata.data.StubTraining
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachCountable
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachTimed
import java.util.Date

class UserTrainingActivity : ComponentActivity() {

    val stubTraining = StubTraining(1, Date())

    val state = mutableStateOf(stubTraining)
    val lifeData = MutableLiveData<StubTraining>(stubTraining)

    private suspend fun doingSomethingHard(param: Int) {
        Log.d("TAG", "doingSomethingHard1 : $param")
        Thread.sleep(1000)
        Log.d("TAG", "doingSomethingHard2 : $param")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        GlobalScope.launch(Dispatchers.IO) {
            doingSomethingHard(2)
        }

        GlobalScope.launch(Dispatchers.Main) {
            doingSomethingHard(1)
        }

        Log.d("TAG", "MainThread1")

        Log.d("TAG", "MainThread2")

        Log.d("TAG", "MainThread3")
        val state2 = mutableStateOf(TrainingViewData.create())

        setContent {
            ExerciseTechniqueTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    OneTrainingScreen(userTrainingState = state2 ) {

                        val oldExerciseList = state2.value.exercises


                        for (i in 0 until oldExerciseList.size) {
                            val exerciseFirst = oldExerciseList[i]
                            if (exerciseFirst.id == it) {
                                val newExercise = when (exerciseFirst) {
                                    is TrainingViewExerciseTimed -> {
                                        exerciseFirst.approaches.add(ExerciseApproachTimed(0,0f,1f))
                                        exerciseFirst.copy(approaches = exerciseFirst.approaches)
                                    }

                                    is TrainingViewExerciseCountable -> {
                                        exerciseFirst.approaches.add(ExerciseApproachCountable(0,0f,1f))
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