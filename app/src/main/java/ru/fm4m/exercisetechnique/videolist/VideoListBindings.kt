package ru.fm4m.exercisetechnique.videolist

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.core.AndroidFragmentBinding

class VideoListBindings(view : VideoListFragment,
private val feature: VideoListFeature): AndroidFragmentBinding<VideoListFragment>(view) {
    override fun setup(view: VideoListFragment) {
        binder.bind(view to feature using UIEventTransformerVideos())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}