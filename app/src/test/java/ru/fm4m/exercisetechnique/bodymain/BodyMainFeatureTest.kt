package ru.fm4m.exercisetechnique.bodymain

import android.util.Log
import com.badoo.binder.Binder
import com.badoo.binder.lifecycle.ManualLifecycle
import com.badoo.binder.using
import ru.fm4m.exercisetechnique.techdomain.model.Sex
import ru.fm4m.exercisetechnique.techdomain.model.Side
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.functions.Consumer
import org.junit.Test

class BodyMainFeatureTest {

    @MockK
    lateinit var stateConsumer : Consumer<BodyMainFeature.State>
    @MockK
    lateinit var newsToWishConsumer: Consumer<ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish>

    val lifecycle = ManualLifecycle()
    val binder = Binder(lifecycle)
    val newsToWishTransformer = NewsToBodyiesNewsTransformer()

    init {
        MockKAnnotations.init(this)
        mockkStatic("android.util.Log")
        every { Log.d(any(),any()) } returns 0
        every { stateConsumer.accept(any()) } returns Unit
        every { newsToWishConsumer.accept(any()) } returns Unit
    }

    @Test
    fun changeSideClicked_updateState() {
        val feature = BodyMainFeature(ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        verify { stateConsumer.accept(withArg {
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
            assert(it.active)
        }) }

        feature.accept(BodyMainFeature.Wish.ChangeSide)

        verify { stateConsumer.accept(withArg {
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.BACK)
            assert(it.active == false)
        }) }


        lifecycle.end()
    }

    @Test
    fun changeSideComplited_updateState() {
        val feature = BodyMainFeature(ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
        binder.bind(feature to stateConsumer)
        lifecycle.begin()

        feature.accept(BodyMainFeature.Wish.ChangeSide)
        feature.accept(BodyMainFeature.Wish.FocusedSide)

        verify { stateConsumer.accept(withArg {
            assert(it.sex == ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
            assert(it.side == ru.fm4m.exercisetechnique.techdomain.model.Side.BACK)
            assert(it.active)
        }) }

        lifecycle.end()
    }

    @Test
    fun changeSideClicked_NotifyAnotherFeature() {
        val feature = BodyMainFeature(ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
        binder.bind(feature.news to newsToWishConsumer using newsToWishTransformer)
        lifecycle.begin()

        feature.accept(BodyMainFeature.Wish.ChangeSide)

        verify(exactly = 1) { newsToWishConsumer.accept(any()) }
        verify() { newsToWishConsumer.accept(withArg {
            assert(it == ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.ChangeSide)
        }) }

        lifecycle.end()

    }

    @Test
    fun completeSideFront_NotifyAnotherFeature() {
        val feature = BodyMainFeature(ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)

        feature.accept(BodyMainFeature.Wish.ChangeSide)
        feature.accept(BodyMainFeature.Wish.FocusedSide)
        feature.accept(BodyMainFeature.Wish.ChangeSide)

        binder.bind(feature.news to newsToWishConsumer using newsToWishTransformer)
        lifecycle.begin()

        feature.accept(BodyMainFeature.Wish.FocusedSide)

        verify(exactly = 1) { newsToWishConsumer.accept(any()) }
        verify(exactly = 1) { newsToWishConsumer.accept(withArg {
            assert(it is ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.FocusedSide)
            assert((it as ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.FocusedSide).side == ru.fm4m.exercisetechnique.techdomain.model.Side.FRONT)
        }) }

        lifecycle.end()

    }

    @Test
    fun completeSideBack_NotifyAnotherFeature() {
        val feature = BodyMainFeature(ru.fm4m.exercisetechnique.techdomain.model.Sex.MALE)
        binder.bind(feature.news to newsToWishConsumer using newsToWishTransformer)
        lifecycle.begin()

        feature.accept(BodyMainFeature.Wish.ChangeSide)
        feature.accept(BodyMainFeature.Wish.FocusedSide)

        verify(exactly = 2) { newsToWishConsumer.accept(any()) }
        verify(exactly = 1) { newsToWishConsumer.accept(withArg {
            assert(it is ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.FocusedSide)
            assert((it as ru.fm4m.exercisetechnique.techdomain.bodymain.body.BodyFeature.Wish.FocusedSide).side == ru.fm4m.exercisetechnique.techdomain.model.Side.BACK)
        }) }

        lifecycle.end()
    }
}