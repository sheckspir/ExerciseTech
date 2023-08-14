package ru.fm4m.exercisetechnique.techdomain.core

import ru.fm4m.exercisetechnique.techdomain.data.Muscle

interface IMuscleName {

    fun getMuscleName(muscle: Muscle): String
}