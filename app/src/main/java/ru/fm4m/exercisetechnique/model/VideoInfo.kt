package ru.fm4m.exercisetechnique.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

open class VideoInfo(
    val id: Int,
    val url : String,
    val title: String?,
    val keys : List<String>
): Serializable


class YouTubeVideoInfo constructor(val youTubeId: String,
                                   title: String? = null,
                                   keys : List<String> = ArrayList<String>().apply { if (title != null) add(title) }
) : VideoInfo(youTubeId.hashCode(),"https://www.youtube.com/watch?v=$youTubeId", title, keys)