package ru.fm4m.exercisetechnique.techview.bodymain

import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ReducerFeature
import ru.fm4m.exercisetechnique.techview.core.PerFragment
import ru.fm4m.exercisetechnique.techdomain.data.Sex
import ru.fm4m.exercisetechnique.techdomain.data.Side
import ru.fm4m.coredomain.system.Logger
import javax.inject.Inject

@PerFragment
class BodyMainFeature @Inject constructor(
    sex: Sex,
    logger: Logger
): ReducerFeature<BodyMainFeature.Wish, BodyMainFeature.State, BodyMainFeature.News>(
    State(sex, Side.FRONT, true),
    ReducerImpl(logger),
    newsPublisher = NewsPublisherImpl()
) {

    sealed class Wish {
        object ChangeSide : Wish()
        object FocusedSide : Wish()
    }

    sealed class News {
        object InChangedFocusState: News()
        object FocusOnFront : News()
        object FocusOnBack : News()
    }

    data class State(val sex: Sex, val side: Side, val active: Boolean)

    class ReducerImpl(private val logger: Logger) : Reducer<State, Wish> {
        override fun invoke(state: State, effect: Wish): State {
            val newState = when(effect) {
                is Wish.ChangeSide -> {
                    if (state.side == Side.FRONT) {
                        state.copy(side = Side.BACK, active = false)
                    } else {
                        state.copy(side = Side.FRONT, active = false)
                    }
                }
                is Wish.FocusedSide -> {
                    state.copy(active = true)
                }
            }
            logger.d("TAG", "mainFeature Reduce old state $state newState $newState")
            return newState
        }
    }

    class NewsPublisherImpl : SimpleNewsPublisher<Wish, State, News>() {
        override fun invoke(wish: Wish, state: State): News? {
            return when(wish) {
                is Wish.ChangeSide -> {
                    News.InChangedFocusState
                }
                is Wish.FocusedSide -> {
                    if (state.side == Side.FRONT) {
                        News.FocusOnFront
                    } else {
                        News.FocusOnBack
                    }
                }
            }
        }
    }
}