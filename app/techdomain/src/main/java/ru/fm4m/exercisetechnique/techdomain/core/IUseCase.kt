package ru.fm4m.exercisetechnique.techdomain.core

import io.reactivex.Observable
import io.reactivex.Scheduler

interface IUseCase<T> {

    operator fun invoke() : Observable<DownloadDataEffect<T>>
}

sealed class DownloadDataEffect<T> {
    class StartDownload<T> : DownloadDataEffect<T>()
    class ErrorDownload<T>(val e: Throwable) : DownloadDataEffect<T>()
    class DownloadedData<T> constructor(val result : T) : DownloadDataEffect<T>()
}

interface ISchedulerProvider {

    fun getMainScheduler() : Scheduler

    fun getNetworkScheduler() : Scheduler
}