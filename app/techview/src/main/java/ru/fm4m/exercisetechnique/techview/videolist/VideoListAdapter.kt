package ru.fm4m.exercisetechnique.techview.videolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.YouTubeVideoInfo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.android.synthetic.main.item_video_short.view.textVideoName

class VideoListAdapter(errorListener: ErrorListener): LoadableRecyclerAdapter<VideoInfo, VideoItemVH>(errorListener) {

    private var items: MutableList<VideoInfo> = ArrayList()

    fun setItems(items : List<VideoInfo>?) {
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun getPositionById(id : Int) : Int {
        items.forEachIndexed { index, videoInfo ->
            if(videoInfo.id == id) {
                return index
            }
        }
        return -1
    }

    override fun getItem(position: Int): VideoInfo {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getNestedItemCount() : Int {
        return items.size
    }

    override fun onCreateNestedViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VideoItemVH {
        return YouTubeItemVH(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video, parent, false))
    }


    override fun onBindNestedViewHolder(holder: VideoItemVH, position: Int) {
        holder.bind(getItem(position), position)
    }

}

abstract class VideoItemVH(view: View) : RecyclerView.ViewHolder(view) {

    val textView = view.textVideoName

    fun bind(videoInfo: VideoInfo, position: Int) {
        ViewCompat.setTransitionName(textView,"title_${videoInfo.id}")
        textView.transitionName = "title_${videoInfo.id}"
        nestedBind(videoInfo, position)
    }

    abstract fun nestedBind(videoInfo: VideoInfo, position: Int)

}

class YouTubeItemVH(view: View) : VideoItemVH(view) {

    private var videoId : String? = null
    private lateinit var youTubePlayer: YouTubePlayer

    private val gradientVideo = view.viewGradient

    init {
        view.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                this@YouTubeItemVH.youTubePlayer = youTubePlayer
                val videoId = videoId
                if (videoId != null) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            }
        })
    }

    override fun nestedBind(videoInfo: VideoInfo, position: Int) {
        if (!videoInfo.name.isNullOrEmpty()) {
            textView.visibility = View.VISIBLE
            gradientVideo.visibility = View.VISIBLE
            textView.text = videoInfo.name
        } else {
            textView.visibility = View.GONE
            gradientVideo.visibility = View.GONE
        }
        if (videoInfo is YouTubeVideoInfo) {
            showYouTubeVideo(videoInfo)
        } else {
            youTubePlayer.cueVideo("", 0f)
        }
    }

    private fun showYouTubeVideo(videoInfo: YouTubeVideoInfo) {
        val videoId = videoInfo.youTubeId
        this.videoId = videoId

        if (::youTubePlayer.isInitialized) {
            youTubePlayer.cueVideo(videoId, 0f)
        }
    }
}