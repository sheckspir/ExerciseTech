package ru.fm4m.exercisetechnique.trainingdomain.data

import java.util.Date

open class UserTraining(
    open val id : String,
    open val userId : Int,
    open var date : Date,
    var durationSec : Int = 0,
    var paused : Boolean = true,
    open val programName: String,
    open var trainingName : String,
    var exercises: MutableList<TrainingExercise>,
    var finished: Boolean = false
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserTraining

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (date != other.date) return false
        if (durationSec != other.durationSec) return false
        if (paused != other.paused) return false
        if (programName != other.programName) return false
        if (trainingName != other.trainingName) return false
        if (exercises != other.exercises) return false
        if (finished != other.finished) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId
        result = 31 * result + date.hashCode()
        result = 31 * result + durationSec
        result = 31 * result + paused.hashCode()
        result = 31 * result + programName.hashCode()
        result = 31 * result + trainingName.hashCode()
        result = 31 * result + exercises.hashCode()
        result = 31 * result + finished.hashCode()
        return result
    }
}