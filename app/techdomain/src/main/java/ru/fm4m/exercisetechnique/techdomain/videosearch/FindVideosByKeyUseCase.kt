package ru.fm4m.exercisetechnique.techdomain.videosearch

import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.rx2.asObservable
import ru.fm4m.coredomain.system.Logger
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.TechniqueRepository
import javax.inject.Inject

interface FindVideosByKeyUseCase {

    fun getData(search: String): Observable<DownloadDataEffect<List<VideoInfo>>>
}

class FindVideosByKeyUseCaseImpl @Inject constructor(
    private val repository: TechniqueRepository,
    context: CoroutineScope,
    private val logger: Logger,
) : FindVideosByKeyUseCase {

    companion object {
        const val DEFAULT_SEARCH = ""
        const val TIME_WAIT_PRINTING = 0L
    }

    @Volatile
    private var lastSearch: String = DEFAULT_SEARCH

    @Volatile
    private var lastResult: List<VideoInfo> = ArrayList()
    private val newSearchState = MutableStateFlow(DEFAULT_SEARCH)
    private val dataFlow = newSearchState
        .debounce(TIME_WAIT_PRINTING)
        .flatMapConcat { search ->
            flow {
                var lastResultLocal = lastResult
                if (search == lastSearch && lastResultLocal.isNotEmpty()) {
                    emit(lastResultLocal)
                } else {
                    lastResultLocal = repository.getVideosForKey(search)
                    lastSearch = search
                    lastResult = lastResultLocal
                    emit(lastResultLocal)
                }
            }.map {
                logger.d(message = "receive videos $it")
                DownloadDataEffect.DownloadedData(it) as DownloadDataEffect<List<VideoInfo>>
            }.flowOn(Dispatchers.IO)
        }
        .catch {
            emit(DownloadDataEffect.ErrorDownload<List<VideoInfo>>(it) as DownloadDataEffect<List<VideoInfo>>)
        }.onStart {
            emit(DownloadDataEffect.StartDownload<List<VideoInfo>>() as DownloadDataEffect<List<VideoInfo>>)
        }.onEach {
            logger.d("TAG", "onEach before shareIn $it")
        }
        .shareIn(context, SharingStarted.Eagerly, 1)


    override fun getData(search: String): Observable<DownloadDataEffect<List<VideoInfo>>> {
        newSearchState.value = search
        return dataFlow.onEach {
            logger.d("TAG", "onEach after shareIn $it Thread = ${Thread.currentThread()}")
        }.asObservable()
    }
}