package ru.fm4m.exercisetechnique.trainingdomain.data



abstract class  TrainingExercise(
    open val id : Int,
    open val name : String,
    open val planApproach : Int,
    open val finished : Boolean,
    open val approaches : MutableList<out ExerciseApproach>
)

data class CountableTrainingExercise(override val id: Int,
                                     override val name: String,
                                     override val planApproach: Int,
                                     override val finished : Boolean,
                                     override val approaches: MutableList<ExerciseApproachCountable>
) : TrainingExercise(id, name, planApproach, finished, approaches)

class TimedTrainingExercise(override val id: Int,
                            override val name: String,
                            override val planApproach: Int,
                            override val finished : Boolean,
                            override val approaches: MutableList<ExerciseApproachTimed>
) : TrainingExercise(id, name, planApproach, finished, approaches)