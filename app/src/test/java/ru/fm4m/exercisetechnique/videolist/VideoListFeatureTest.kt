package ru.fm4m.exercisetechnique.videolist

import android.os.Looper
import android.util.Log
import com.badoo.binder.Binder
import com.badoo.binder.lifecycle.ManualLifecycle
import com.badoo.mvicore.android.AndroidTimeCapsule
import ru.fm4m.exercisetechnique.model.Muscle
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.model.VideoInfo
import ru.fm4m.exercisetechnique.model.YouTubeVideoInfo
import ru.fm4m.exercisetechnique.server.ServerApi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.lang.IllegalArgumentException
import java.util.concurrent.Callable


class VideoListFeatureTest {

    @MockK
    lateinit var stateConsumer : Consumer<VideoListFeature.State>
    @MockK
    lateinit var newsConsumer : Consumer<VideoListFeature.News>
    @MockK
    lateinit var serverApi: ServerApi
    @MockK
    lateinit var looper: Looper

    val lifecycle = ManualLifecycle()
    val binder = Binder(lifecycle)

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
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }
    }


    @Test
    fun checkDownloadProcess_allGood() {
        every { serverApi.getVideoList(any(), any()) } returns Single.just(ArrayList<VideoInfo>().apply {
            add(YouTubeVideoInfo("111"))
        })
        val feature = VideoListFeature(AndroidTimeCapsule(null),
            serverApi,
            Sex.MALE,
            Muscle.TRICEPS)
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(VideoListFeature.Wish.RedownloadList)
        Thread.sleep(50)

        verify(exactly = 3) { stateConsumer.accept(any()) }

        verify { stateConsumer.accept(withArg {
            assert(it.isLoading == false)
            assert(it.videoLists == null)
        }) }

        verify { stateConsumer.accept(withArg {
            assert(it.isLoading)
            assert(it.videoLists == null)
        }) }

        verify { stateConsumer.accept(withArg {
            assert(it.isLoading == false)
            assert(it.videoLists?.first() is YouTubeVideoInfo)
        }) }

        lifecycle.end()
    }

    @Test
    fun checkDownloadProcess_withError() {
        val exception = IllegalArgumentException()
        every { serverApi.getVideoList(any(), any()) } returns Single.error(exception)
        val feature = VideoListFeature(AndroidTimeCapsule(null),
            serverApi,
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