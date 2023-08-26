package ru.fm4m.exercisetechnique.techdomain.server

import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo

interface TechniqueRepository {

    suspend fun getVideoList(male: Boolean, muscle: Muscle): List<VideoInfo>

    suspend fun getVideosForKey(key : String): List<VideoInfo>

    suspend fun getNewProgram() : List<VideoInfo>
}