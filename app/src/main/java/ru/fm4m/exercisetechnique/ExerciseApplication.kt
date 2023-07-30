package ru.fm4m.exercisetechnique

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.server.ApiVideoInfoAdapter
import ru.fm4m.exercisetechnique.server.ServerApi
import ru.fm4m.exercisetechnique.server.ServerApiBackend


class ExerciseApplication : Application() {

    private lateinit var retrofit: Retrofit;
    private lateinit var serverApi: ServerApi;

    override fun onCreate() {
        super.onCreate()

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(VideoInfo::class.java, ApiVideoInfoAdapter())
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.alekar.ru/exercise/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(client)
            .build()

        serverApi = retrofit.create(ServerApiBackend::class.java)
    }


    public fun getServerApi() : ServerApi {
        return serverApi;
    }
}