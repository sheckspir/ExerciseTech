package ru.fm4m.exercisetechnique.techdomain.newprogram

import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.rx2.asObservable
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.TechniqueRepository
import javax.inject.Inject

@Deprecated("remove")
class RedownloadNewProgramUseCaseImpl @Inject constructor(
    private val repository: TechniqueRepository,
) : RedownloadNewProgramUseCase {

    private val coldFlow = flow {
        try {
            emit(
                DownloadDataEffect.DownloadedData(
                    repository.getNewProgram()
                ) as DownloadDataEffect<List<VideoInfo>>
            )
        } catch (e: Exception) {
            emit(DownloadDataEffect.ErrorDownload(e))
        }
    }.flowOn(Dispatchers.IO)
        .onStart {
            emit(DownloadDataEffect.StartDownload<List<VideoInfo>>() as DownloadDataEffect<List<VideoInfo>>)
        }.flowOn(Dispatchers.Main)

    override fun invoke(): Observable<DownloadDataEffect<List<VideoInfo>>> {
        return coldFlow.asObservable()
    }
}

interface RedownloadNewProgramUseCase : IUseCase<List<VideoInfo>>