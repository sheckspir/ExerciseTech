package ru.fm4m.exercisetechnique.techdomain.core

import io.reactivex.Observable
import io.reactivex.Scheduler

interface IUseCase<T> {

    fun getData() : Observable<DownloadDataEffect<T>>
}

open class DownloadDataEffect<T> {
    class StartDownload<T> : DownloadDataEffect<T>()
    class ErrorDownload<T>(val e: Throwable) : DownloadDataEffect<T>()
    class DownloadedData<T>(val result : T) : DownloadDataEffect<T>()
}

interface ISchedulerProvider {

    fun getMainScheduler() : Scheduler

    fun getNetworkScheduler() : Scheduler
}