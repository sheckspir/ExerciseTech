package ru.fm4m.exercisetechnique.techdomain.videolist

import io.reactivex.Observable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleVideos
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import javax.inject.Inject

class RedownloadVideoListBySexAndMuscleImpl @Inject constructor(
    private val serverApi: ServerApi,
    private val schedulersProvider: ISchedulerProvider,
    private val muscleInfoProvider: IMuscleInfoApi,
    private val sex: Sex,
    private val muscle: Muscle
) : RedownloadVideoListBySexAndMuscle {

    override fun getData(): Observable<DownloadDataEffect<MuscleVideos>> {
        return Observable.just(DownloadDataEffect.StartDownload<MuscleVideos>() as DownloadDataEffect<MuscleVideos>)
            .observeOn(schedulersProvider.getMainScheduler())
            .mergeWith(serverApi.getVideoList(sex == Sex.MALE, muscle)
                .flatMap { videosList ->
                    muscleInfoProvider.getMuscleInfo(muscle)
                        .map { DownloadDataEffect.DownloadedData(MuscleVideos(it, videosList)) as DownloadDataEffect<MuscleVideos> }
                }
                .onErrorReturn { DownloadDataEffect.ErrorDownload(it)}
                .observeOn(schedulersProvider.getMainScheduler()))
    }
}

interface RedownloadVideoListBySexAndMuscle : IUseCase<MuscleVideos>