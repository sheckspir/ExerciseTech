package ru.fm4m.exercisetechnique.techdomain.newprogram

import io.reactivex.Observable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import javax.inject.Inject

class RedownloadNewProgramUseCaseImpl @Inject constructor(
    private val schedulersProvider : ISchedulerProvider,
    private val serverApi : ServerApi
) : RedownloadNewProgramUseCase{

    override fun getData(): Observable<DownloadDataEffect<List<VideoInfo>>> {
        return Observable.just(DownloadDataEffect.StartDownload<List<VideoInfo>>() as DownloadDataEffect<List<VideoInfo>>)
            .observeOn(schedulersProvider.getMainScheduler())
            .mergeWith(serverApi.getNewProgram()
                .map { DownloadDataEffect.DownloadedData(it)  as DownloadDataEffect<List<VideoInfo>>}
                .onErrorReturn{ DownloadDataEffect.ErrorDownload(it) }
                .observeOn(schedulersProvider.getMainScheduler()))
            .subscribeOn(schedulersProvider.getNetworkScheduler())
    }
}

interface RedownloadNewProgramUseCase : IUseCase<List<VideoInfo>>