package ru.fm4m.exercisetechnique.bodymain.body

import android.util.Log
import com.badoo.binder.Binder
import com.badoo.binder.lifecycle.ManualLifecycle
import io.mockk.impl.annotations.MockK
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import io.mockk.*
import ru.fm4m.exercisetechnique.NavigationEvent
import ru.fm4m.exercisetechnique.techdomain.data.Muscle
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side

class BodyFeatureTest {

    @MockK
    lateinit var navigationEventPublisher : PublishSubject<NavigationEvent>
    @MockK
    lateinit var stateConsumer : Consumer<BodyFeature.State>

    val lifecycle = ManualLifecycle()
    val binder = Binder(lifecycle)

    init {
        MockKAnnotations.init(this)
        mockkStatic("android.util.Log")
        every { Log.d(any(),any()) } returns 0
        every { stateConsumer.accept(any()) } returns Unit
        every { navigationEventPublisher.onNext(any()) } returns Unit
    }

    @Test
    fun selectMuscleOne_onlyShowSelectedMuscle() {
        val feature = BodyFeature(
            Sex.MALE,
            Side.FRONT,
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        })}
        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
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
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))
        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        })}
        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
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
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.FocusedSide(Side.BACK))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle == false)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.FocusedSide(Side.FRONT))

        verify(exactly = 2) { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
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
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.SelectMuscle(Muscle.TRICEPS))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        feature.accept(BodyFeature.Wish.ChangeSide)

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == Muscle.TRICEPS)
            assert(it.showTitleMuscle == false)
            assert(it.sex == Sex.MALE)
            assert(it.side == Side.FRONT)
        }) }

        lifecycle.end()

    }
}