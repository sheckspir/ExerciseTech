package ru.fm4m.exercisetechnique.techview.core

import io.reactivex.subjects.PublishSubject

interface ObservableSourceEventContainer<E> {

    fun getSource(): PublishSubject<out E>
}