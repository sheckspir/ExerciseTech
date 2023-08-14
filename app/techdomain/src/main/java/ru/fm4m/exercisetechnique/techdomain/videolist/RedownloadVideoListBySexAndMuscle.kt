package ru.fm4m.exercisetechnique.techdomain.videolist

import io.reactivex.Observable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import javax.inject.Inject

class RedownloadVideoListBySexAndMuscleImpl @Inject constructor(
    private val serverApi: ServerApi,
    private val schedulersProvider: ISchedulerProvider,
    private val sex: Sex,
    private val muscle: Muscle
) : RedownloadVideoListBySexAndMuscle {

    override fun getData(): Observable<DownloadDataEffect<List<VideoInfo>>> {
        return Observable.just(DownloadDataEffect.StartDownload<List<VideoInfo>>() as DownloadDataEffect<List<VideoInfo>>)
            .observeOn(schedulersProvider.getMainScheduler())
            .mergeWith(serverApi.getVideoList(sex == Sex.MALE, muscle)
                .map { DownloadDataEffect.DownloadedData(it) as DownloadDataEffect<List<VideoInfo>> }
                .onErrorReturn { DownloadDataEffect.ErrorDownload(it)}
                .observeOn(schedulersProvider.getMainScheduler()))
    }
}

interface RedownloadVideoListBySexAndMuscle : IUseCase<List<VideoInfo>>