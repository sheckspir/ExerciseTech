package ru.fm4m.exercisetechnique.techview.onevideo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.fragment_exercise_info.view.playerView
import kotlinx.coroutines.launch
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.YouTubeVideoInfo
import ru.fm4m.exercisetechnique.techview.initVideo
import ru.fm4m.exercisetechnique.techview.videolist.VideoListFragment

class ExerciseInfoFragment : Fragment(R.layout.fragment_exercise_info) {

    private val videoInfo: VideoInfo by lazy {
        requireArguments().getSerializable(ARG_VIDEO_INFO) as VideoInfo
    }

    private var titleTextView : TextView? = null
    private var viewTitleShadow : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleTextView = view.findViewById(R.id.textVideoName)
        viewTitleShadow = view.findViewById(R.id.viewShadow)

        ViewCompat.setTransitionName(titleTextView!!, "title_${videoInfo.id}")
        showNativeTitle(true)
        showVideoInfo(videoInfo)
    }

    private fun showNativeTitle(show : Boolean) {
        if (show) {
            var title = videoInfo.name
            if (title.isNullOrEmpty()) {
                val keys = videoInfo.hints.split(",")
                title = if (keys.isNotEmpty()) {
                    keys[0]
                } else {
                    ""
                }
            }
            if(title.isEmpty()) {
                titleTextView?.visibility = View.GONE
                viewTitleShadow?.visibility = View.GONE
            } else {
                titleTextView?.visibility = View.VISIBLE
                viewTitleShadow?.visibility = View.VISIBLE
                titleTextView?.text = title
            }
        } else {
            titleTextView?.visibility = View.GONE
            viewTitleShadow?.visibility = View.GONE
        }


    }

    private fun showVideoInfo(videoInfo: VideoInfo) {
        var text = videoInfo.name
        if (text.isNullOrEmpty()) {
            val keys = videoInfo.hints.split(",")
            text = if (keys.isNotEmpty()) {
                keys[0]
            } else {
                ""
            }
        }
        titleTextView?.text = text


        lifecycleScope.launch {
            if (videoInfo is YouTubeVideoInfo) {
                val youTubeListener = object : AbstractYouTubePlayerListener() {
                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState,
                    ) {
                        if (state == PlayerConstants.PlayerState.VIDEO_CUED) {
                            showNativeTitle(!videoInfo.name.isNullOrEmpty())
                            youTubePlayer.removeListener(this)
                        }
                    }
                }
                view?.playerView?.addYouTubePlayerListener(youTubeListener)

                val result = view?.playerView?.initVideo(videoInfo.youTubeId)
                if (result != true) {
                    Toast.makeText(context, R.string.exercise_video_unavailable, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {

        const val ARG_VIDEO_INFO = "info"

        @JvmStatic
        fun newInstance(videoInfo: VideoInfo) =
            VideoListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VIDEO_INFO, videoInfo)
                }
            }
    }
}