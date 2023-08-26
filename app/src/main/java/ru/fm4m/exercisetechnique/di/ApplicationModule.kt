package ru.fm4m.exercisetechnique.di

import android.content.Context
import android.content.res.Resources
import android.util.Log
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.fm4m.coredomain.system.Logger
import ru.fm4m.exercisetechnique.techdata.core.MuscleNameProvider
import ru.fm4m.exercisetechnique.techdomain.bodymain.body.DownloadMuscleUseCase
import ru.fm4m.exercisetechnique.techdomain.bodymain.body.DownloadMuscleUseCaseImpl
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import ru.fm4m.exercisetechnique.trainingdata.server.AccountRepositoryImpl
import ru.fm4m.exercisetechnique.trainingdata.server.ExerciseRepositoryImpl
import ru.fm4m.exercisetechnique.trainingdomain.repository.AccountRepository
import ru.fm4m.exercisetechnique.trainingdomain.repository.ExerciseRepository
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideExerciseRepository(repository: ExerciseRepositoryImpl): ExerciseRepository =
        repository

    @Singleton
    @Provides
    fun accountRepository(repository: AccountRepositoryImpl): AccountRepository = repository

    @Singleton
    @Provides
    fun schedulerProvider(): ISchedulerProvider {
        return object : ISchedulerProvider {
            override fun getMainScheduler(): Scheduler {
                return AndroidSchedulers.mainThread()
            }

            override fun getNetworkScheduler(): Scheduler {
                return Schedulers.io()
            }
        }

    }

    @Singleton
    @Provides
    fun logger(): Logger {
        return object : Logger {
            override fun d(tag: String, message: String) {
                Log.d(tag, message)
            }

            override fun e(tag: String, message: String, e: Throwable?) {
                Log.d(tag, message, e)
            }
        }
    }

    @Singleton
    @Provides
    fun muscleNameProvider(muscleNameProvider: MuscleNameProvider): IMuscleInfoApi =
        muscleNameProvider

    @Provides
    fun downloadMuscleUseCase(downloadMuscleUseCase: DownloadMuscleUseCaseImpl): DownloadMuscleUseCase =
        downloadMuscleUseCase

    @Singleton
    @Provides
    fun resources(context: Context): Resources {
        return context.resources
    }

    @Singleton
    @Provides
    fun context(): Context {
        return context
    }
}