package com.example.exercisetechnique.videolist

import com.badoo.binder.using
import com.badoo.mvicore.android.AndroidBindings

class VideoListBindings(view : VideoListFragment,
private val feature: VideoListFeature): AndroidBindings<VideoListFragment>(view) {
    override fun setup(view: VideoListFragment) {
        binder.bind(view to feature using UIEventTransformerVideos())
        binder.bind(feature to view)
    }
}