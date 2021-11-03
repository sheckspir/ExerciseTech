package ru.fm4m.exercisetechnique.videolist

import ru.fm4m.exercisetechnique.UIEvent

sealed class UIEventVideos : UIEvent() {
    object RedownloadAllVideos: UIEventVideos()
    class DownloadMoreVideos(val countHave : Int) : UIEventVideos()
}

class UIEventTransformerVideos : (UIEvent) -> VideoListFeature.Wish? {
    override fun invoke(p1: UIEvent) = when(p1) {
            is UIEventVideos.RedownloadAllVideos -> VideoListFeature.Wish.RedownloadList
            is UIEventVideos.DownloadMoreVideos -> VideoListFeature.Wish.DownloadMore(p1.countHave)
            else -> null
        }
}