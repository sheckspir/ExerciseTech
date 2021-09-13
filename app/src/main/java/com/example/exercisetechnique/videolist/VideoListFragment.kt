package com.example.exercisetechnique.videolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.example.exercisetechnique.R
import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex
import com.example.exercisetechnique.server.ServerApiImpl


class VideoListFragment : Fragment() {

    private lateinit var feature: VideoListFeature


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sex = arguments!!.getSerializable(ARG_SEX) as Sex
        val muscle = arguments!!.getSerializable(ARG_MUSCLE) as Muscle
        val api = ServerApiImpl.getInstance()
        feature = VideoListFeature(AndroidTimeCapsule(savedInstanceState), api, sex, muscle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        private const val ARG_SEX = "sex"
        private const val ARG_MUSCLE = "muscle"

        @JvmStatic
        fun newInstance(sex: Sex, muscle: Muscle) =
            VideoListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SEX, sex)
                    putSerializable(ARG_MUSCLE, muscle)
                }
            }
    }
}