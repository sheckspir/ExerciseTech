package ru.fm4m.exercisetechnique.di

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.fm4m.exercisetechnique.techdomain.bodymain.body.DownloadMuscleUseCase
import ru.fm4m.exercisetechnique.techdomain.bodymain.body.DownloadMuscleUseCaseImpl
import ru.fm4m.exercisetechnique.techdomain.server.IMuscleInfoApi
import ru.fm4m.exercisetechnique.techdomain.core.ISchedulerProvider
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.system.Logger
import ru.fm4m.exercisetechnique.techniquedata.server.ApiVideoInfoAdapter
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import ru.fm4m.exercisetechnique.techniquedata.core.MuscleNameProvider
import ru.fm4m.exercisetechnique.techniquedata.server.ServerApiBackend
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {
//class ApplicationModule() {

    @Singleton
    @Provides
    fun provideApi(logger : Logger): ServerApi {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(VideoInfo::class.java, ApiVideoInfoAdapter(logger))
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alekar.ru/exercise/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(client)
            .build()

        return retrofit.create(ServerApiBackend::class.java)
    }

    @Singleton
    @Provides
    fun schedulerProvider() : ISchedulerProvider {
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
    fun logger() : Logger {
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
    fun muscleNameProvider(muscleNameProvider: MuscleNameProvider) : IMuscleInfoApi = muscleNameProvider

    @Provides
    fun downloadMuscleUseCase(downloadMuscleUseCase: DownloadMuscleUseCaseImpl) : DownloadMuscleUseCase = downloadMuscleUseCase

    @Singleton
    @Provides
    fun resources(context: Context) : Resources {
        return context.resources
    }

    @Singleton
    @Provides
    fun context() : Context {
        return context
    }
}

//@Module
//abstract class ExerciseAppProvider {
//
//    @ContributesAndroidInjector(modules = [ApplicationModule::class])
//    abstract fun provideExerciseApplication(): ExerciseApplication
//}