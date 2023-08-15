package ru.fm4m.exercisetechnique.bodymain.body

import com.badoo.binder.Binder
import com.badoo.binder.lifecycle.ManualLifecycle
import io.mockk.impl.annotations.MockK
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import io.mockk.*
import io.reactivex.Observable
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.techdomain.bodymain.body.DownloadMuscleUseCase
import ru.fm4m.exercisetechnique.techdomain.core.DownloadDataEffect
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.MuscleInfo
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import ru.fm4m.exercisetechnique.techdomain.system.TestLogger

class BodyFeatureTest {

    @MockK
    lateinit var navigationEventPublisher : PublishSubject<NavigationEvent>
    @MockK
    lateinit var stateConsumer : Consumer<BodyFeature.State>
    @MockK
    lateinit var downloadMuscleUseCase : DownloadMuscleUseCase

    val mockedMusclesInfo : Map<Muscle, MuscleInfo> = HashMap<Muscle, MuscleInfo>().apply {
        for (one in Muscle.values()) {
            put(one, MuscleInfo(one, one.name))
        }
    }

    var logger = TestLogger()

    val lifecycle = ManualLifecycle()
    val binder = Binder(lifecycle)

    init {
        MockKAnnotations.init(this)
        every { stateConsumer.accept(any()) } returns Unit
        every { navigationEventPublisher.onNext(any()) } returns Unit

        every { downloadMuscleUseCase.getData(any()) } returns Observable
            .just<DownloadDataEffect<Map<Muscle, MuscleInfo>>?>(DownloadDataEffect.StartDownload())
            .mergeWith(Observable.just(DownloadDataEffect.DownloadedData(mockedMusclesInfo)))

    }

    @Test
    fun selectMuscleOne_onlyShowSelectedMuscle() {
        val feature = BodyFeature(
            Sex.MALE,
            Side.FRONT,
            navigationEventPublisher,
            downloadMuscleUseCase,
            logger
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(!it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        })}
        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        })}

        lifecycle.end()

    }

    @Test
    fun selectMuscleTwice_showSelected_openVideos() {
        val feature = BodyFeature(
            Sex.MALE,
            Side.FRONT,
            navigationEventPublisher,
            downloadMuscleUseCase,
            logger
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))
        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(!it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        })}
        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        })}

        verify { navigationEventPublisher.onNext(withArg {
            assert(it is NavigationEvent.ShowMuscleVideos)
            assert((it as NavigationEvent.ShowMuscleVideos).muscle == Muscle.TRICEPS)
            assert(it.sex == Sex.MALE)
        }) }

        lifecycle.end()
    }

    @Test
    fun focusedBack_hideTitle() {
        val feature = BodyFeature(
            Sex.MALE,
            Side.FRONT,
            navigationEventPublisher,
            downloadMuscleUseCase,
            logger
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(!it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.FocusedSide(Side.BACK))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(!it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.FocusedSide(Side.FRONT))

        verify(exactly = 2) { stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        lifecycle.end()
    }

    @Test
    fun changeSideProcess_hideTitle() {
        val feature = BodyFeature(
            Sex.MALE,
            Side.FRONT,
            navigationEventPublisher,
            downloadMuscleUseCase,
            logger
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(!it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.ChangeSide)

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle?.muscle == Muscle.TRICEPS)
            assert(!it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        lifecycle.end()

    }
}