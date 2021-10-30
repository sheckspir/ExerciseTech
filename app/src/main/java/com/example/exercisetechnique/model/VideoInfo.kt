package com.example.exercisetechnique.model

open class VideoInfo(
    id: Int,
    url : String,
    title: String?
)


class YouTubeVideoInfo constructor(val youTubeId: String, title: String? = null) : VideoInfo(youTubeId.hashCode(),"https://www.youtube.com/watch?v=$youTubeId", title)