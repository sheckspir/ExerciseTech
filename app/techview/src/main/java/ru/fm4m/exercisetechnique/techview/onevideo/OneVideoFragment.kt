package ru.fm4m.exercisetechnique.techview.onevideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.fm4m.exercisetechnique.techview.R
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.YouTubeVideoInfo
import ru.fm4m.exercisetechnique.techview.videolist.VideoListFragment
import ru.fm4m.exercisetechnique.techview.videolist.YouTubeItemVH

class OneVideoFragment : Fragment() {

    lateinit var videoInfo: VideoInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoInfo = requireArguments().getSerializable(ARG_VIDEO_INFO) as VideoInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_one_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (videoInfo is YouTubeVideoInfo) {
            YouTubeItemVH(view).bind(videoInfo)
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