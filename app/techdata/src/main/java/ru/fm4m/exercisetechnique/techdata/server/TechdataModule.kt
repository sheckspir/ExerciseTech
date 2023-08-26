package ru.fm4m.exercisetechnique.techdata.server

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.server.ServerApi
import ru.fm4m.exercisetechnique.techdomain.server.TechniqueRepository
import ru.fm4m.coredomain.system.Logger
import javax.inject.Singleton

@Module
class TechdataModule {

    @Singleton
    @Provides
    fun retrofitProvider(logger: Logger) : ServerApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(VideoInfo::class.java, ApiVideoInfoAdapter(logger))
            .create()

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alekar.ru/exercise/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(okHttpClient)
            .build()

        return retrofit.create(ServerApiBackend::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(logger: Logger) : TechniqueRepository{

        val gson = GsonBuilder()
            .registerTypeAdapter(VideoInfo::class.java, ApiVideoInfoAdapter(logger))
            .create()

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.alekar.ru/exercise/")
            .build()

        return retrofit.create(ServerRepository::class.java)
    }
}