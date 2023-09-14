package ru.fm4m.exercisetechnique.techdomain.videolist

import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.rx2.asObservable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleVideos
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import ru.fm4m.exercisetechnique.techdomain.server.TechniqueRepository
import javax.inject.Inject

class RedownloadVideoListBySexAndMuscleImpl @Inject constructor(
    private val repository: TechniqueRepository,
    private val muscleInfoProvider: IMuscleInfoApi,
    private val sex: Sex,
    private val muscle: Muscle,
) : RedownloadVideoListBySexAndMuscle {

    private val flowVideos = flow {
        emit(repository.getVideoList(sex == Sex.MALE, muscle))
    }.flowOn(Dispatchers.IO)
    private val flowMuscleInfo = flow {
        emit(muscleInfoProvider.getMuscleInfo(muscle))
    }.flowOn(Dispatchers.IO)

    private val coldFlow = flowVideos.zip(flowMuscleInfo) { videoInfos, muscleInfo ->
        MuscleVideos(muscleInfo, videoInfos)
    }.map {
        DownloadDataEffect.DownloadedData(it) as DownloadDataEffect<MuscleVideos>
    }.flowOn(
        Dispatchers.IO
    ).catch {
        emit(DownloadDataEffect.ErrorDownload(it))
    }.onStart {
        emit(DownloadDataEffect.StartDownload())
    }.flowOn(Dispatchers.Main)

    override fun getData(): Observable<DownloadDataEffect<MuscleVideos>> {
        return coldFlow.asObservable()
    }
}

interface RedownloadVideoListBySexAndMuscle : IUseCase<MuscleVideos>