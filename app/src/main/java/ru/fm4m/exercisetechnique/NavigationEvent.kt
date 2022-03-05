package ru.fm4m.exercisetechnique

import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.model.VideoInfo

sealed class NavigationEvent {
    object ShowAnotherSide: NavigationEvent()
    data class ShowMuscleVideos(val muscle: Muscle, val sex: Sex): NavigationEvent()
    data class ShowOneVideo(val videoInfo: VideoInfo): NavigationEvent()
    data class ShowOneVideoFromNewProgram(val videoInfo: VideoInfo) : NavigationEvent()
}