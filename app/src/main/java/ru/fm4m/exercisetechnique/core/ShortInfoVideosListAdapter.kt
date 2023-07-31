package ru.fm4m.exercisetechnique.videosearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_video_short.view.*
import ru.fm4m.exercisetechnique.R
import ru.fm4m.exercisetechnique.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.videolist.VideoItemVH

class ShortInfoVideosListAdapter(errorListener: ErrorListener,
private val clickListener: ShortInfoListListener) : LoadableRecyclerAdapter<VideoInfo, ShortInfoVideosVH>(errorListener) {

    private var videos : List<VideoInfo> = ArrayList()

    fun setVideos(videos : List<VideoInfo>?) {
        if (videos == null) {
           this.videos = ArrayList()
        } else {
            this.videos = videos
        }
        notifyDataSetChanged()
    }
    override fun getItem(position: Int): VideoInfo {
        return videos[position]
    }

    override fun getNestedItemCount(): Int {
        return videos.size
    }


    override fun onCreateNestedViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ShortInfoVideosVH {
        return ShortInfoVideosVH(LayoutInflater.from(parent.context).inflate(R.layout.item_video_short, parent, false), clickListener)
    }

    override fun onBindNestedViewHolder(holder: ShortInfoVideosVH, position: Int) {
        holder.bind(getItem(position))
    }
}

interface ShortInfoListListener {
    fun onClickVideoInfo(videoInfo: VideoInfo)
}

class ShortInfoVideosVH(view: View, clickListener: ShortInfoListListener) : VideoItemVH(view) {

    val textView = view.textVideoName
    init {
        view.layoutVideoInfo.setOnClickListener {
            val videoInfo = videoInfo
            if(videoInfo != null) {
                clickListener.onClickVideoInfo(videoInfo)
            }
        }
    }

    var videoInfo: VideoInfo? = null

    override fun bind(videoInfo: VideoInfo) {
        this.videoInfo = videoInfo
        var text = videoInfo.title
        if (text.isNullOrEmpty()) {
            val keys = videoInfo.keys.split(",")
            if (keys.isNotEmpty()) {
                text = keys[0]
            } else {
                text = "";
            }
        }
        textView.text = text
    }
}