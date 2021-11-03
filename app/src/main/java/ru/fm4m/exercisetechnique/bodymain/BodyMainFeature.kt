package ru.fm4m.exercisetechnique.bodymain

import android.util.Log
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ReducerFeature
import ru.fm4m.exercisetechnique.model.Sex
import ru.fm4m.exercisetechnique.model.Side

class BodyMainFeature(
    sex: Sex
): ReducerFeature<BodyMainFeature.Wish, BodyMainFeature.State, BodyMainFeature.News>(
    State(sex, Side.FRONT, true),
    ReducerImpl(),
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

    class ReducerImpl() : Reducer<State, Wish> {
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
            Log.d("TAG", "mainFeature Reduce old state $state newState $newState")
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
                else -> null
            }
        }
    }
}