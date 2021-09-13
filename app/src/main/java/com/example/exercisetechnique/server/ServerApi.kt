package com.example.exercisetechnique.server

import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.VideoInfo
import io.reactivex.Observable

interface ServerApi {

    fun getVideoList(male: Boolean, muscle: Muscle): Observable<List<VideoInfo>>
}

class ServerApiImpl : ServerApi {

    companion object {

        private val instance : ServerApi = ServerApiImpl()
        fun getInstance() = instance
    }

    private val videos : Map<Muscle, List<VideoInfo>> = HashMap<Muscle, List<VideoInfo>>().apply {
        put(Muscle.NECK, ArrayList<VideoInfo>().apply{
            add(VideoInfo(0,"https://www.youtube.com/watch?v=NvL6jeV05Wk"))
        })
    }

    override fun getVideoList(male: Boolean, muscle: Muscle): Observable<List<VideoInfo>> {
        return if (videos.containsKey(muscle)) {
            Observable.just(videos[muscle]?: emptyList())
        } else {
            Observable.just(emptyList())
        }
    }
}