package ru.fm4m.exercisetechnique

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch {
            Log.d("TAG", "doingSomething0 in ${Thread.currentThread()}")

            withContext(Dispatchers.IO) {
                for (i in 0..5) {
                    Log.d("TAG", "doingSomething1 in ${Thread.currentThread()}")
                    delay(500)
                    Log.d("TAG", "doingSomething2 in ${Thread.currentThread()}")
                    delay(1)
                }
            }

        }

        setContentView(R.layout.activity_main)

    }
}