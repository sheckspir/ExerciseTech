package ru.fm4m.exercisetechnique.techdomain.data

import java.io.Serializable

open class VideoInfo(
    val id: Int,
    val url : String,
    val title: String?,
    val keys : String
): Serializable


class YouTubeVideoInfo constructor(val youTubeId: String,
                                   title: String? = null,
                                   keys : String = title ?: ""
) : VideoInfo(youTubeId.hashCode(),"https://www.youtube.com/watch?v=$youTubeId", title, keys)