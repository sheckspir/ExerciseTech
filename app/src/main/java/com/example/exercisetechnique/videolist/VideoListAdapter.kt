package com.example.exercisetechnique.videolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exercisetechnique.R
import com.example.exercisetechnique.core.LoadableRecyclerAdapter
import com.example.exercisetechnique.model.VideoInfo
import com.example.exercisetechnique.model.YouTubeVideoInfo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.item_video.view.*

class VideoListAdapter(errorListener: ErrorListener): LoadableRecyclerAdapter<VideoInfo, RecyclerView.ViewHolder>(errorListener) {

    private var items: MutableList<VideoInfo> = ArrayList()


    fun setItems(items : List<VideoInfo>?) {
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): VideoInfo {
        return items[position]
    }

    override fun getNestedItemCount() = items.size

    override fun onCreateNestedViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return YouTubeItemVH(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }


    override fun onBindNestedViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoItemVH).bind(getItem(position))
    }
}

abstract class VideoItemVH(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(videoInfo: VideoInfo)

}

class YouTubeItemVH(view: View) : VideoItemVH(view) {

    var videoId : String? = null
    lateinit var youTubePlayer: YouTubePlayer

    val textView = view.textVideoName
    val gradientVideo = view.viewGradient

    init {
        view.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                this@YouTubeItemVH.youTubePlayer = youTubePlayer
                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId!!, 0f)
                }
            }
        })
    }

    override fun bind(videoInfo: VideoInfo) {
        if (videoInfo.title != null) {
            textView.visibility = View.VISIBLE
            gradientVideo.visibility = View.VISIBLE
            textView.text = videoInfo.title
        } else {
            textView.visibility = View.GONE
            gradientVideo.visibility = View.GONE
        }
        if (videoInfo is YouTubeVideoInfo) {
            val videoId = videoInfo.youTubeId
            this.videoId = videoId

            if (::youTubePlayer.isInitialized) {
                youTubePlayer.cueVideo(videoId, 0f)
            }
        } else {
            youTubePlayer.cueVideo("", 0f)

        }
    }
}