package ru.fm4m.exercisetechnique.techdomain.server

import io.reactivex.Single
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.ProgramInfo
import ru.fm4m.exercisetechnique.techdomain.data.ProgramPartInfo
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo

interface ServerApi {

    fun getVideoList(male: Boolean, muscle: Muscle): Single<List<VideoInfo>>

    fun getVideosForKey(key : String): Single<List<VideoInfo>>

    fun getNewProgram() : Single<List<VideoInfo>>

    suspend fun getActualProgram() : ProgramInfo

    suspend fun getDayInfo(dayId : Int) : ProgramPartInfo

}