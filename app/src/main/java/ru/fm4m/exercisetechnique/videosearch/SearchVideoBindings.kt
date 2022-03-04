package ru.fm4m.exercisetechnique.videosearch

import com.badoo.binder.using
import ru.fm4m.exercisetechnique.core.AndroidFragmentBindings

class SearchVideoBindings(
    view: SearchVideosFragment,
    private val feature: SearchVideosFeature,
) : AndroidFragmentBindings<SearchVideosFragment>(view) {

    override fun setup(view: SearchVideosFragment) {
        binder.bind(view to feature using UIEventTransformerSearchVideos())
        binder.bind(feature to view)
        binder.bind(feature.news to view.newsConsumer)
    }
}