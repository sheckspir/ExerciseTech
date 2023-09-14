package ru.fm4m.exercisetechnique.techview.core

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_video_short.view.layoutVideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techview.videolist.VideoItemVH

class ShortInfoVideosListAdapter(
    errorListener: ErrorListener,
    private val clickListener: ShortInfoListListener,
) : LoadableRecyclerAdapter<VideoInfo, ShortInfoVideosVH>(errorListener) {

    private var videos: List<VideoInfo> = ArrayList()

    fun setVideos(videos: List<VideoInfo>?) {
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

    override fun getItemId(position: Int): Long {
        Log.d("TAG", "for position $position id is ${videos[position].id.toLong()}")
        return videos[position].id.toLong()
    }

    fun getPosition(itemId: Int): Int? {
        videos.forEachIndexed { index, videoInfo ->
            if (videoInfo.id == itemId) {
                return index
            }
        }
        return null
    }

    override fun getNestedItemCount(): Int {
        return videos.size
    }


    override fun onCreateNestedViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ShortInfoVideosVH {
        return ShortInfoVideosVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video_short, parent, false),
            clickListener
        )
    }

    override fun onBindNestedViewHolder(holder: ShortInfoVideosVH, position: Int) {
        holder.bind(getItem(position), position)
    }
}

interface ShortInfoListListener {
    fun onClickVideoInfo(videoInfo: VideoInfo)
}

class ShortInfoVideosVH(view: View, clickListener: ShortInfoListListener) : VideoItemVH(view) {

    init {
        view.layoutVideoInfo.setOnClickListener {
            val videoInfo = videoInfo
            if (videoInfo != null) {
                clickListener.onClickVideoInfo(videoInfo)
            }
        }
    }

    var videoInfo: VideoInfo? = null

    override fun nestedBind(videoInfo: VideoInfo, position: Int) {
        this.videoInfo = videoInfo
        var text = videoInfo.name
        if (text.isNullOrEmpty()) {
            val keys = videoInfo.hints.split(",")
            text = if (keys.isNotEmpty()) {
                keys[0]
            } else {
                ""
            }
        }
        textView.text = text
    }
}