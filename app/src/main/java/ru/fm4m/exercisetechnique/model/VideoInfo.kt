package ru.fm4m.exercisetechnique.model

open class VideoInfo(
    val id: Int,
    val url : String,
    val title: String?
)


class YouTubeVideoInfo constructor(val youTubeId: String, title: String? = null) : VideoInfo(youTubeId.hashCode(),"https://www.youtube.com/watch?v=$youTubeId", title)