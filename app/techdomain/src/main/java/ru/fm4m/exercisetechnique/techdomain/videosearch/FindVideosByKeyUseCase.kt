package ru.fm4m.exercisetechnique.techdomain.videosearch

import io.reactivex.Observable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import ru.fm4m.exercisetechnique.techdomain.server.TechniqueRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

interface FindVideosByKeyUseCase {

    fun getData(search: String) : Observable<DownloadDataEffect<List<VideoInfo>>>
}

class FindVideosByKeyUseCaseImpl @Inject constructor(
    private val schedulersProvider: ISchedulerProvider,
    private val serverApi: ServerApi,
) : FindVideosByKeyUseCase  {

    override fun getData(search: String): Observable<DownloadDataEffect<List<VideoInfo>>> {
        return Observable.just(DownloadDataEffect.StartDownload<List<VideoInfo>>() as DownloadDataEffect<List<VideoInfo>>)
            .observeOn(schedulersProvider.getMainScheduler())
            .mergeWith(serverApi.getVideosForKey(search)
                .map { DownloadDataEffect.DownloadedData(it) as DownloadDataEffect<List<VideoInfo>> }
                .onErrorReturn { DownloadDataEffect.ErrorDownload(it)}
                .observeOn(schedulersProvider.getMainScheduler()))
    }
}