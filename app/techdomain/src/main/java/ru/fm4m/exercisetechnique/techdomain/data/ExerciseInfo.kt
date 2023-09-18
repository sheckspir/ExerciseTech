package ru.fm4m.exercisetechnique.techdomain.data

data class ExerciseInfo(
    val id: Int,
    val name: String,
    val obligatoryName: Boolean = false,
    val hints: String,
    val url: String,
)