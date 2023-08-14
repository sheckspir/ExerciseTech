package ru.fm4m.exercisetechnique.techdomain.bodymain.body

import io.reactivex.Observable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import javax.inject.Inject
import javax.inject.Singleton

interface DownloadMuscleUseCase {

    fun getData(sex: Sex): Observable<DownloadDataEffect<Map<Muscle,MuscleInfo>>>
}

@Singleton
class DownloadMuscleUseCaseImpl @Inject constructor(
    private val muscleApi : IMuscleInfoApi,
    private val schedulersProvider : ISchedulerProvider
): DownloadMuscleUseCase {
    override fun getData(sex: Sex): Observable<DownloadDataEffect<Map<Muscle, MuscleInfo>>> {
        return muscleApi.getListMusclesInfo(sex)
            .subscribeOn(schedulersProvider.getNetworkScheduler())
            .observeOn(schedulersProvider.getMainScheduler())
            .map { DownloadDataEffect.DownloadedData(it) as DownloadDataEffect<Map<Muscle,MuscleInfo>>}
            .toObservable()
    }
}