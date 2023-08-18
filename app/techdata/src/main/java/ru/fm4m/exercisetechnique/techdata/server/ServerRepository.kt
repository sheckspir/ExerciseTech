package ru.fm4m.exercisetechnique.techdata.server

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.TechniqueRepository

interface ServerRepository : TechniqueRepository {

    @GET("get_videos")
    override suspend fun getVideoList(@Query("male") male: Boolean, @Query("muscle") muscle: Muscle): List<VideoInfo>

    @GET("find_videos")
    override suspend fun getVideosForKey(@Query("text") key: String): List<VideoInfo>

    @GET("actual_program")
    override suspend fun getNewProgram(): List<VideoInfo>


}