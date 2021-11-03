package ru.fm4m.exercisetechnique.videolist

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.core.AndroidFragmentBindings

class VideoListBindings(view : VideoListFragment,
private val feature: VideoListFeature): AndroidFragmentBindings<VideoListFragment>(view) {
    override fun setup(view: VideoListFragment) {
        binder.bind(view to feature using UIEventTransformerVideos())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}