package ru.fm4m.exercisetechnique.videolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fm4m.exercisetechnique.R
import ru.fm4m.exercisetechnique.core.LoadableRecyclerAdapter
import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.model.YouTubeVideoInfo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.item_title_videos.view.*
import kotlinx.android.synthetic.main.item_video.view.*

class VideoListAdapter(errorListener: ErrorListener): LoadableRecyclerAdapter<VideoInfo, RecyclerView.ViewHolder>(errorListener) {

    private var items: MutableList<VideoInfo> = ArrayList()

    private var muscle : Muscle? = null

    fun setMuscle(muscle: Muscle) {
        if (this.muscle != muscle) {
            this.muscle = muscle
            notifyDataSetChanged()
        }
    }

    fun setItems(items : List<VideoInfo>?) {
        this.items.clear()
        if (items != null) {
            this.items.addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val nestedItems = getNestedItemCount()
        if (position < nestedItems) {
            if(position == 0) {
                return TYPE_TITLE
            } else {
                if (items[position - 1] is YouTubeVideoInfo) {
                    return TYPE_YOUTUBE
                }
            }
        }
        return super.getItemViewType(position)
    }

    override fun getItem(position: Int): VideoInfo {
        return items[position - 1]
    }

    override fun getNestedItemCount() : Int {
        return if (items.size > 0) {
            items.size + 1
        } else {
            items.size
        }
    }

    override fun onCreateNestedViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (viewType == TYPE_YOUTUBE) {
            YouTubeItemVH(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video, parent, false))
        } else {
            TitleVideosVH(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_title_videos, parent, false))
        }
    }


    override fun onBindNestedViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoItemVH) {
            holder.bind(getItem(position))
        } else if (holder is TitleVideosVH){
            holder.bind(muscle)
        }
    }

    companion object{
        private const val TYPE_TITLE = 10
        private const val TYPE_YOUTUBE = 20
    }
}

class TitleVideosVH(view: View) : RecyclerView.ViewHolder(view) {

    private val textView = view.textTitle

    fun bind(muscle: Muscle?) {
        if (muscle == null) {
            itemView.visibility = View.GONE
        } else {
            textView.setText(muscle.muscleName)
            itemView.visibility = View.VISIBLE
        }

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