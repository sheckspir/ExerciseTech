package ru.fm4m.exercisetechnique.techdomain.data

data class ProgramPartInfo(
    val id: Int,
    val programName: String,
    val programId: Int,
    val dayName: String,
    val image : String,
    val exercises: List<ExerciseInfo>
)