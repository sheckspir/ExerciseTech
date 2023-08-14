package ru.fm4m.exercisetechnique.videosearch

import ru.fm4m.exercisetechnique.UIEvent
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo

sealed class UIEventSearchVideos : UIEvent() {
    object RedownloadAllVideos: UIEventSearchVideos()
    class DownloadMoreVideos(val countHave: Int) : UIEventSearchVideos()
    class SearchVideos(val text: String) : UIEventSearchVideos()
    class ShowVideo(val videoInfo: VideoInfo) : UIEventSearchVideos()
}

class UIEventTransformerSearchVideos : (UIEvent) -> SearchVideosFeature.Wish? {
    override fun invoke(p1: UIEvent): SearchVideosFeature.Wish? {
        return when(p1) {
            is UIEventSearchVideos.RedownloadAllVideos -> SearchVideosFeature.Wish.RedownloadLast
            is UIEventSearchVideos.DownloadMoreVideos -> SearchVideosFeature.Wish.DownloadMore(p1.countHave)
            is UIEventSearchVideos.SearchVideos -> SearchVideosFeature.Wish.FindVideos(p1.text)
            is UIEventSearchVideos.ShowVideo -> SearchVideosFeature.Wish.ShowVideo(p1.videoInfo)
            else -> null
        }
    }
}