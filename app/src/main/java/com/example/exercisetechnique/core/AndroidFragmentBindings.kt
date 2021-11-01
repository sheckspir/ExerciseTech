package com.example.exercisetechnique.core

import androidx.lifecycle.LifecycleOwner
import com.badoo.binder.Binder
import com.badoo.mvicore.android.lifecycle.StartStopBinderLifecycle

abstract class AndroidFragmentBindings<T : Any>(
    lifecycleOwner: LifecycleOwner
) {
    protected val binder = Binder(
        lifecycle = StartStopBinderLifecycle(
        androidLifecycle = lifecycleOwner.lifecycle
    )
    )

    abstract fun setup(view: T)


}