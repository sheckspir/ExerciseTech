package ru.fm4m.exercisetechnique.newprogram

import ru.fm4m.exercisetechnique.UIEvent
 import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo

sealed class UIEventNewProgram : UIEvent() {
    object RedownloadAllVideos: UIEventNewProgram()
    class ShowVideo(val videoInfo: VideoInfo) : UIEventNewProgram()
}

class UIEventTransformerNewProgram : (UIEvent) -> NewProgramFeature.Wish? {
    override fun invoke(p1: UIEvent): NewProgramFeature.Wish? {
        return when(p1){
            is UIEventNewProgram.RedownloadAllVideos -> NewProgramFeature.Wish.RedownloadLast
            is UIEventNewProgram.ShowVideo -> NewProgramFeature.Wish.ShowVideo(p1.videoInfo)
            else -> null
        }
    }
}