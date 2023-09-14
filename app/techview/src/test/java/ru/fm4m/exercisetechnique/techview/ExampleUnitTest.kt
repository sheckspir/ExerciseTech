package ru.fm4m.exercisetechnique.techview

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val stringClass = "stroka0"
    @Test
    fun addition_isCorrect() {
        val stringModule = "stroka2"

        val lambda : () -> Unit = {
            var result = 0
            var result2 = ""
            for(i in 0..10) {
                result2 += stringModule
                result += i*i
            }
            Log.d("TAG", "some task $result")
        }

        lambda.invoke()
        assertEquals(4, 2 + 2)
    }

    fun second(i: Int): Int {
        return i*i
    }
}