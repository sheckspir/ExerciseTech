package ru.fm4m.exercisetechnique.techdomain.server

import io.reactivex.Single
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.Sex

interface IMuscleInfoApi {

    suspend fun getMuscleInfo(muscle: Muscle) : MuscleInfo

    suspend fun getListMusclesInfo(sex: Sex) : Map<Muscle, MuscleInfo>
}