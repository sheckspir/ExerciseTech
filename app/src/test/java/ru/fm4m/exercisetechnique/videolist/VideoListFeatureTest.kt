package ru.fm4m.exercisetechnique.videolist

import android.os.Looper
import android.util.Log
import com.badoo.binder.Binder
import com.badoo.binder.lifecycle.ManualLifecycle
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.VideoInfo
import ru.fm4m.exercisetechnique.techdomain.data.YouTubeVideoInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.MuscleVideos
import ru.fm4m.exercisetechnique.techdomain.videolist.RedownloadVideoListBySexAndMuscle
import java.lang.IllegalArgumentException


class VideoListFeatureTest {

    @MockK
    lateinit var stateConsumer : Consumer<VideoListFeature.State>
    @MockK
    lateinit var newsConsumer : Consumer<VideoListFeature.News>
    @MockK
    lateinit var looper: Looper
    @MockK
    lateinit var redownloadVideoListBySexAndMuscle: RedownloadVideoListBySexAndMuscle

    private val lifecycle = ManualLifecycle()
    private val binder = Binder(lifecycle)

    init {
        MockKAnnotations.init(this)
        mockkStatic("android.util.Log")
        every { Log.d(any(), any()) } returns 0
        every { stateConsumer.accept(any()) } returns Unit
        every { newsConsumer.accept(any()) }returns Unit

        mockkStatic("android.os.Looper")
        every { Looper.getMainLooper() } returns looper
        mockkStatic("io.reactivex.android.schedulers.AndroidSchedulers")
        every { AndroidSchedulers.mainThread() } returns Schedulers.trampoline()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }


    @Test
    fun checkDownloadProcess_allGood() {
        val muscleInfo = MuscleInfo(Muscle.BICEPS, "")
        val muscleVideos = ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("111"))
        }
        every { redownloadVideoListBySexAndMuscle.invoke() }returns Observable
            .just(DownloadDataEffect.StartDownload<MuscleVideos>() as DownloadDataEffect<MuscleVideos>)
            .mergeWith(Observable.just(DownloadDataEffect.DownloadedData(MuscleVideos(muscleInfo, muscleVideos)) as DownloadDataEffect<MuscleVideos>))
        val feature = VideoListFeature(null,
            redownloadVideoListBySexAndMuscle,
            Sex.MALE,
            Muscle.TRICEPS)
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(VideoListFeature.Wish.RedownloadList)
        Thread.sleep(50)

        verify(exactly = 3) { stateConsumer.accept(any()) }

        verify { stateConsumer.accept(withArg {
            assert(!it.isLoading)
            assert(it.videoLists == null)
        }) }

        verify { stateConsumer.accept(withArg {
            assert(it.isLoading)
            assert(it.videoLists == null)
        }) }

        verify { stateConsumer.accept(withArg {
            assert(!it.isLoading)
            assert(it.videoLists?.first() is YouTubeVideoInfo)
        }) }

        lifecycle.end()
    }

    @Test
    fun checkDownloadProcess_withError() {
        val exception = IllegalArgumentException()
        every { redownloadVideoListBySexAndMuscle.invoke() } returns Observable
            .just(DownloadDataEffect.StartDownload<MuscleVideos>() as DownloadDataEffect<MuscleVideos>)
            .mergeWith(Observable.error(exception))

        val feature = VideoListFeature(null,
            redownloadVideoListBySexAndMuscle,
            Sex.MALE,
            Muscle.TRICEPS)
        binder.bind(feature to stateConsumer)
        binder.bind(feature.news to newsConsumer)
        lifecycle.begin()

        feature.accept(VideoListFeature.Wish.RedownloadList)
        Thread.sleep(50)

        verify(exactly = 3) { stateConsumer.accept(any()) }


        verify { stateConsumer.accept(withArg {
            assert(it.isLoading)
            assert(it.videoLists == null)
        }) }

        verify(exactly = 2) { stateConsumer.accept(withArg {
            assert(it.isLoading == false)
            assert(it.videoLists == null)
        }) }


        verify(exactly = 1) { newsConsumer.accept(withArg {
            assert(it is VideoListFeature.News.ErrorExecuteRequest)
            assert((it as VideoListFeature.News.ErrorExecuteRequest).throwable == exception)
        }) }

        lifecycle.end()
    }
}