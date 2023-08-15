package ru.fm4m.exercisetechnique.techdomain.data

import java.io.Serializable

open class VideoInfo(
    stringId: String,
    val url : String,
    val name: String?,
    val hints : String,
    val id: Int = stringId.toIntOrNull()?: stringId.hashCode()
): Serializable


class YouTubeVideoInfo constructor(val youTubeId: String,
                                   name: String? = null,
                                   hints : String = name ?: ""
) : VideoInfo(youTubeId,"https://www.youtube.com/watch?v=$youTubeId", name, hints)