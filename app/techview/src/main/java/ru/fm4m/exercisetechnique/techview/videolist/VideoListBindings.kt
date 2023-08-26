package ru.fm4m.exercisetechnique.techview.videolist

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import ru.fm4m.exercisetechnique.techview.core.AndroidFragmentBinding
import javax.inject.Inject

@PerFragment
class VideoListBindings @Inject constructor(
    view : VideoListFragment,
    private val feature: VideoListFeature
): AndroidFragmentBinding<VideoListFragment>(view) {
    override fun setup(view: VideoListFragment) {
        binder.bind(view to feature using UIEventTransformerVideos())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}