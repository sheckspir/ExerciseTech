package com.example.exercisetechnique

import com.example.exercisetechnique.model.Muscle
import com.example.exercisetechnique.model.Sex

sealed class NavigationEvent {
    object ShowAnotherSide: NavigationEvent()
    data class ShowMuscleVideos(val muscle: Muscle, val sex: Sex): NavigationEvent()
}