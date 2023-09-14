package ru.fm4m.testmodule

import android.util.Log
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.internal.wait


class SecondClass {

    fun initTestClass() {
        Log.d("TAG", "before any")
        one()
        TestClass.second()


        val one = TestClass()
        Log.d("TAG", "After init testClass")
        val just = one.justObject
        Log.d("TAG", "getted just $just")
    }
}