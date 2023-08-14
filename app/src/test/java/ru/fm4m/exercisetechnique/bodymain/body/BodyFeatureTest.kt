package ru.fm4m.exercisetechnique.bodymain.body

import android.util.Log
import com.badoo.binder.Binder
import com.badoo.binder.lifecycle.ManualLifecycle
import ru.fm4m.exercisetechnique.techdomain.model.Muscle
import ru.fm4m.exercisetechnique.techdomain.model.Sex
import ru.fm4m.exercisetechnique.techdomain.model.Side
import io.mockk.impl.annotations.MockK
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import io.mockk.*
import ru.fm4m.exercisetechnique.NavigationEvent

class BodyFeatureTest {

    @MockK
    lateinit var navigationEventPublisher : PublishSubject<NavigationEvent>
    @MockK
    lateinit var stateConsumer : Consumer<ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.State>

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
        val feature = ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature(
            ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE,
            ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT,
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.SelectMuscle(ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS))

        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        })}
        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        })}

        lifecycle.end()

    }

    @Test
    fun selectMuscleTwice_showSelected_openVideos() {
        val feature = ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature(
            ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE,
            ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT,
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.SelectMuscle(ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS))
        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.SelectMuscle(ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS))

        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        })}
        verify(exactly = 1){stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        })}

        verify { navigationEventPublisher.onNext(withArg {
            assert(it is NavigationEvent.ShowMuscleVideos)
            assert((it as NavigationEvent.ShowMuscleVideos).muscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
        }) }

        lifecycle.end()
    }

    @Test
    fun focusedBack_hideTitle() {
        val feature = ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature(
            ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE,
            ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT,
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.SelectMuscle(ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.FocusedSide(ru.fm4m.exercisetechnique.techdomain.model.Side.BACK))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle == false)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.FocusedSide(ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT))

        verify(exactly = 2) { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        lifecycle.end()
    }

    @Test
    fun changeSideProcess_hideTitle() {
        val feature = ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature(
            ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE,
            ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT,
            navigationEventPublisher
        )
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == null)
            assert(it.showTitleMuscle == false)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.SelectMuscle(ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS))

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        feature.accept(ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.ChangeSide)

        verify { stateConsumer.accept(withArg {
            assert(it.selectedMuscle == ru.fm4m.exercisetechnique.techdomain.model.Muscle.TRICEPS)
            assert(it.showTitleMuscle == false)
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        lifecycle.end()

    }
}