package ru.fm4m.exercisetechnique.techview

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.sync.Mutex
import java.lang.IllegalStateException

suspend fun YouTubePlayerView?.initVideo(videoId : String) : Boolean {
    if (this == null) {
        return false
    }
    var tag = this.tag
    if (tag is YouTubePlayer) {
        tag.cueVideo(videoId, 0f)
    } else {
        val mutex = Mutex()
        mutex.lock()

        this.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                this@initVideo.tag = youTubePlayer
                mutex.unlock()
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                mutex.unlock()
            }
        })

        //wait until it's unlocked
        mutex.lock()
        mutex.unlock()
        tag = this.tag
        if (tag is YouTubePlayer) {
            tag.cueVideo(videoId, 0f)
        } else {
            return false
        }
    }
    return true
}