package ru.fm4m.exercisetechnique.trainingdomain.data

import java.util.Date

open class UserTraining(
    val id : String,
    open val userId : Int,
    open var date : Date,
    var durationSec : Int = 0,
    var paused : Boolean = true,
    val programName: String,
    var trainingName : String,
    var exercises: MutableList<TrainingExercise>,
    var finished: Boolean = false
)