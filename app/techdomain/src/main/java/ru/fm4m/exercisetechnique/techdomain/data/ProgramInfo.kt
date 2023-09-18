package ru.fm4m.exercisetechnique.techdomain.data

data class ProgramInfo(
    val id: Int,
    val name: String,
    val description: String,
    val days: List<ProgramPartShort>,
)