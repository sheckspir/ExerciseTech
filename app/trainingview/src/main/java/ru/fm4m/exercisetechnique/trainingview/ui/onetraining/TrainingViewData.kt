package ru.fm4m.exercisetechnique.trainingview.ui.onetraining

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproach
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachCountable
import ru.fm4m.exercisetechnique.trainingdomain.data.ExerciseApproachTimed
import java.util.Date

@Immutable
interface TrainingViewExercise {
    val id : Int
    val name: String
    val planApproach : Int
    val finished : Boolean
    val approaches : MutableList<out ExerciseApproach>

    companion object Factory {
        fun create() : TrainingViewExercise {
            return TrainingViewExerciseCountable(1, "Name", 5, false, mutableStateListOf())
        }
    }
}


@Immutable
data class TrainingViewExerciseTimed constructor(
    override val id : Int,
    override val name : String,
    override val planApproach : Int,
    override val finished : Boolean,
    override val approaches : MutableList<ExerciseApproachTimed>
) : TrainingViewExercise

@Immutable
data class TrainingViewExerciseCountable(
    override val id : Int,
    override val name : String,
    override val planApproach : Int,
    override val finished : Boolean,
    override val approaches : MutableList<ExerciseApproachCountable>
) : TrainingViewExercise

@Immutable
data class TrainingViewData(val id : String,
                            val userId : Int,
                            val date : Date,
                            var durationSec : Int = 0,
                            val paused : Boolean = true,
                            val programName: String,
                            val trainingName : String,
                            val exercises: MutableList<TrainingViewExercise>,
                            val finished: Boolean = false) {

    companion object Factory {
        fun create() : TrainingViewData {

            val stubApproach = mutableStateListOf<ExerciseApproachTimed>()
            stubApproach.apply {
                add(ExerciseApproachTimed(10, 0f, 1f))
                add(ExerciseApproachTimed(20, 0f, 1f))
            }
            val something = mutableStateListOf<TrainingViewExercise>()
            something.apply {
                for (i in 0..50) {
                    add(
                        TrainingViewExerciseCountable(
                            this.size,
                            "Упражнение по счёту ${this.size}",
                            5,
                            false,
                            mutableStateListOf()
                        )
                    )
                    add(
                        TrainingViewExerciseTimed(
                            this.size,
                            "Упражнение по времени ${this.size}",
                            5,
                            false,
                            mutableStateListOf()
                        )
                    )
                    add(
                        TrainingViewExerciseCountable(
                            this.size,
                            "Упражнение по счёту ${this.size}",
                            5,
                            true,
                            mutableStateListOf()
                        )
                    )
                    add(
                        TrainingViewExerciseTimed(
                            this.size,
                            "Упражнение по времени ${this.size}",
                            5,
                            false,
                            mutableStateListOf<ExerciseApproachTimed>().apply {
                                addAll(stubApproach)
                            })
                    )
                }
            }
            return TrainingViewData(
                "1",
                0,
                Date(),
                programName = "Супер крутая программа",
                trainingName = "Супер крутая тренировка в программе",
                exercises = something

            )
        }
    }
}