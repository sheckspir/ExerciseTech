package ru.fm4m.exercisetechnique.videosearch

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.PerFragment
import ru.fm4m.exercisetechnique.core.AndroidFragmentBinding
import javax.inject.Inject

@PerFragment
class SearchVideoBindings @Inject constructor(
    view: SearchVideosFragment,
    private val feature: SearchVideosFeature,
) : AndroidFragmentBinding<SearchVideosFragment>(view) {

    override fun setup(view: SearchVideosFragment) {
        binder.bind(view to feature using UIEventTransformerSearchVideos())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}