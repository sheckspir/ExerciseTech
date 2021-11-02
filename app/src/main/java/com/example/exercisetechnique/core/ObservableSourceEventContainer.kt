package com.example.exercisetechnique.core

import io.reactivex.subjects.PublishSubject

interface ObservableSourceEventContainer<E> {

    fun getSource(): PublishSubject<out E>
}