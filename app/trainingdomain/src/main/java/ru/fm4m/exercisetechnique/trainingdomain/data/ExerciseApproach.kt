package ru.fm4m.exercisetechnique.trainingdomain.data

abstract class ExerciseApproach(open val difficulty: Float)

data class ExerciseApproachCountable(val count: Int, val weight : Float, override val difficulty: Float): ExerciseApproach(difficulty)

data class ExerciseApproachTimed(val seconds: Int, val weight: Float, override val difficulty: Float): ExerciseApproach(difficulty)