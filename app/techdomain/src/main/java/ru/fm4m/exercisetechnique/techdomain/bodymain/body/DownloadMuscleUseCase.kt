package ru.fm4m.exercisetechnique.techdomain.bodymain.body

import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx2.asObservable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import javax.inject.Inject
import javax.inject.Singleton

interface DownloadMuscleUseCase {

    fun getData(sex: Sex): Observable<DownloadDataEffect<Map<Muscle, MuscleInfo>>>
}

@Singleton
class DownloadMuscleUseCaseImpl @Inject constructor(
    private val muscleApi: IMuscleInfoApi,
) : DownloadMuscleUseCase {

    override fun getData(sex: Sex): Observable<DownloadDataEffect<Map<Muscle, MuscleInfo>>> {
        return flow {
            emit(muscleApi.getListMusclesInfo(sex))
        }.flowOn(
            Dispatchers.IO
        ).map {
            DownloadDataEffect.DownloadedData(it) as DownloadDataEffect<Map<Muscle, MuscleInfo>>
        }.flowOn(
            Dispatchers.Main
        ).asObservable()
    }
}