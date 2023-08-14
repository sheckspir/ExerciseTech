package ru.fm4m.exercisetechnique.techdomain.server

import io.reactivex.Single
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.Sex

interface IMuscleInfoApi {

    fun getMuscleInfo(muscle: Muscle) : Single<MuscleInfo>

    fun getListMusclesInfo(sex: Sex) : Single<Map<Muscle, MuscleInfo>>
}