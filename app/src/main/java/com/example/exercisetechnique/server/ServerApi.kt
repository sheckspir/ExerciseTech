package com.example.exercisetechnique.server

import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.VideoInfo
import com.example.exercisetechnique.model.YouTubeVideoInfo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

interface ServerApi {

    fun getVideoList(male: Boolean, muscle: Muscle): Single<List<VideoInfo>>
}

class ServerApiImpl : ServerApi {

    companion object {

        private val instance : ServerApi = ServerApiImpl()
        fun getInstance() = instance
    }

    private val videos : Map<Muscle, List<VideoInfo>> = HashMap<Muscle, List<VideoInfo>>().apply {
        put(Muscle.CHEST, ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("RTlMlKB9rn0","Как накачать грудные. Версия 1"))
            add(YouTubeVideoInfo("xwtaHancCQc"))
            add(YouTubeVideoInfo("WaDPNbP3xWk"))
            add(YouTubeVideoInfo("oW9GfQuhSVE"))

        })
        put(Muscle.NECK, ArrayList<VideoInfo>().apply{
            add(YouTubeVideoInfo("NvL6jeV05Wk"))
        })
    }

    override fun getVideoList(male: Boolean, muscle: Muscle): Single<List<VideoInfo>> {
        return Completable.timer(600, TimeUnit.MILLISECONDS)
            .andThen(
                Single.fromCallable {
                    return@fromCallable if (videos.containsKey(muscle)) {
                        videos[muscle]?: emptyList()
                    } else {
                        emptyList()
                    }
                }
            )
    }
}