package ru.fm4m.exercisetechnique.techdomain.newprogram

import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.rx2.asObservable
import kotlinx.coroutines.withContext
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.core.IUseCase
import ru.fm4m.exercisetechnique.techdomain.data.ProgramInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import javax.inject.Inject

class DownloadActualProgramImpl @Inject constructor(
    val server : ServerApi,
) : DownloadActualProgram {
    override fun invoke(): Observable<DownloadDataEffect<ProgramInfo>> {
        return flow{
            emit(DownloadDataEffect.DownloadedData(server.getActualProgram()) as DownloadDataEffect<ProgramInfo>)
        }.flowOn(Dispatchers.IO)
            .onStart { emit(DownloadDataEffect.StartDownload()) }
            .catch { emit(DownloadDataEffect.ErrorDownload(it)) }
            .flowOn(Dispatchers.Main)
            .asObservable()
    }
}

interface DownloadActualProgram : IUseCase<ProgramInfo>