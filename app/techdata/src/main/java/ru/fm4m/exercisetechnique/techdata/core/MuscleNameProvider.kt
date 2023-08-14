package ru.fm4m.exercisetechnique.techniquedata.core

import android.content.res.Resources
import io.reactivex.Single
import ru.fm4m.exercisetechnique.techdata.R
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MuscleNameProvider @Inject constructor(private val resources: Resources) : IMuscleInfoApi {

    private val names = HashMap<Muscle, Int>().apply {
        put(Muscle.NECK, R.string.neck)
        put(Muscle.BICEPS, R.string.biceps)
        put(Muscle.TRICEPS, R.string.triceps)
        put(Muscle.CHEST, R.string.chest)
        put(Muscle.DELTA, R.string.delta)
        put(Muscle.KVADR, R.string.kvadr)
        put(Muscle.PREDPLECHIA, R.string.predplechia)
        put(Muscle.TRAPECIA, R.string.trapecia)
        put(Muscle.GOLEN, R.string.golen)
        put(Muscle.KOSS_PRESS, R.string.koss_press)
        put(Muscle.PRESS, R.string.press)
        put(Muscle.BICEPS_BEDR, R.string.biceps_bedr)
        put(Muscle.SHIROCH, R.string.shiroch)
        put(Muscle.BUTT, R.string.butt)
        put(Muscle.BRAIN, R.string.brain)
    }

    private val namesString by lazy {
        return@lazy HashMap<Muscle, MuscleInfo>().apply {
            for (one in names) {
                put(one.key, MuscleInfo(one.key, resources.getString(one.value)))
            }
        }
    }

    override fun getMuscleInfo(muscle: Muscle): Single<MuscleInfo> {
        return Single.fromCallable {
            namesString[muscle]
        }
    }

    override fun getListMusclesInfo(sex: Sex): Single<Map<Muscle, MuscleInfo>> {
        return Single.just(namesString)
    }
}