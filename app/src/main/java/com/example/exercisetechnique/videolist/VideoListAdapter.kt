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

class VideoListAdapter(errorListener: ErrorListener): LoadableRecyclerAdapter<VideoInfo>(errorListener) {

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoItemVH) {
            holder.bind(getItem(position))
        } else {
            super.onBindViewHolder(holder, position)
        }
    }
}

abstract class VideoItemVH(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(videoInfo: VideoInfo)

}

class YouTubeItemVH(view: View) : VideoItemVH(view) {

    var videoId : String? = null
    lateinit var youTubePlayer: YouTubePlayer

    init {
        view.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                this@YouTubeItemVH.youTubePlayer = youTubePlayer
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId!!,0f)
                }
            }
        })
    }

    override fun bind(videoInfo: VideoInfo) {
        val videoId = (videoInfo as? YouTubeVideoInfo)?.youTubeId
        this.videoId = videoId

        if (::youTubePlayer.isInitialized && videoId != null) {
            youTubePlayer.cueVideo(videoId, 0f)
        }
    }
}