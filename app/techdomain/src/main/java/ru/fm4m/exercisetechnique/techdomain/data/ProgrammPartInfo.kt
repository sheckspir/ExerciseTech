package ru.fm4m.exercisetechnique.techdomain.data

data class ProgrammPartInfo(
    val id: Int,
    val programmName: String,
    val programmId: Int,
    val dayName: String,
    val image : String,
    val exercises: List<ExerciseInfo>
)