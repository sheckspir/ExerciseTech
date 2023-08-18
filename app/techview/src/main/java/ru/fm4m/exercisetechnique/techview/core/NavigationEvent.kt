package ru.fm4m.exercisetechnique.techview.core

import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo

sealed class NavigationEvent {
    object ShowAnotherSide: NavigationEvent()
    data class ShowMuscleVideos(val muscle: Muscle, val sex: Sex): NavigationEvent()
    data class ShowOneVideo(val videoInfo: VideoInfo): NavigationEvent()
    data class ShowOneVideoFromNewProgram(val videoInfo: VideoInfo) : NavigationEvent()
}