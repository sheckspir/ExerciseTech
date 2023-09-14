package ru.fm4m.testmodule

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.fm4m.exercisetechnique.trainingdata.data.StubTraining
import java.util.Date
import kotlin.coroutines.CoroutineContext

class TestUserTrainingActivity : ComponentActivity(), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO + CoroutineName("main name") + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e("TAG1", "error in coroutine $coroutineContext", throwable)
        }


    val stubTraining = StubTraining(1, Date())

    val state = mutableStateOf(stubTraining)
    val lifeData = MutableLiveData(stubTraining)
    


    class LongTask {
        fun request(string: String): String {
            Thread.sleep(4000)
            return "newString $string"
        }
    }

    interface ExecutorStringGetter {
        fun requestAsync(string: String, onResponse: (String) -> Unit)


        fun requestAsyncCoroutines(string: String, onResponse: (String) -> Unit)
    }

    class ExecutorStringGetterImpl(private val scope: CoroutineScope) : ExecutorStringGetter {

        private val longTask = LongTask()

        private val map = HashMap<String, MutableSet<(String) -> Unit>>()

        override fun requestAsync(string: String, onResponse: (String) -> Unit) {
            Log.d("TAG", "before synchronized")
            synchronized(map) {
                Log.d("TAG", "before contains")
                if (map.containsKey(string)) {
                    Log.d("TAG", "add response")
                    map[string]!!.add(onResponse)
                } else {
                    Log.d("TAG", "create response")
                    map[string] = HashSet<(String) -> Unit>().apply {
                        add(onResponse)
                    }
                    val thread = generateThread(string)
                    Log.d("TAG", "before starting")
                    thread.start()
                }
            }


        }

        val mutex = Mutex()

        override fun requestAsyncCoroutines(string: String, onResponse: (String) -> Unit) {
            scope.launch {
                mutex.withLock {
                    if (map.containsKey(string)) {
                        map[string]!!.add(onResponse)
                    } else {
                        map[string] = HashSet<(String) -> Unit>().apply {
                            add(onResponse)
                        }
                    }
                }

                val result : String
                try {
                    result = longTask.request(string)

                } catch (e: Exception) {
                    //добавить обработку ошибок
                    map.remove(string)
                    return@launch
                }

                mutex.withLock {
                    map[string]?.forEach {
                        it.invoke(result)
                    }
                    map.remove(string)
                }

            }
            TODO("Not yet implemented")
        }

        private fun generateThread(string: String): Thread {
            val runnable = object : Runnable {
                override fun run() {
                    val result: String
                    try {
                        result = longTask.request(string)
                    } catch (e: Exception) {
                        Log.e("TAG", "some exception", e)
                        //тут бы надо сообщить об ошибке, но как?
                        synchronized(map) {
                            map.remove(string)
                        }
                        return
                    }

                    synchronized(map) {
                        map[string]?.forEach {
                            it.invoke(result + this.toString())
                        }
                        map.remove(string)
                    }
                }
            }
            return Thread(runnable)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test)


        /*
        lifecycleScope.launch {
            delay(3000)

            val mutex = Mutex(true)

            workManagerCoroutinesStub.someLongTask {
                Log.d("TAG", "result")
                mutex.unlock()
            }
            //вот тут надо дожаться? Семафоры/ожидание
            Log.d("TAG", "before semaphore")
            mutex.withLock {
                Log.d("TAG", "finish with lock")
            }
            Log.d("TAG", "after semaphore")
        }
         */

//        val firstJob = OneTimeWorkRequestBuilder<MyWorkManager1>()
//            .addTag("FIRST")
//            .build()
//
//        val secondJob = OneTimeWorkRequestBuilder<MyWorkManager2>()
//            .addTag("SECOND")
//            .build()
//
//        WorkManager.getInstance(this)
//            .beginWith(firstJob)
//            .then(secondJob)
//            .enqueue()
        /*






        lifecycleScope.launch {

            val channel = Channel<Boolean>()

            workManagerCoroutinesStub.someLongTask {
                Log.d("TAG", "result")
                launch {
                    channel.send(true)
                }

            }

            //вот тут надо дожаться? Семафоры/ожидание
            Log.d("TAG", "before takeUnless")
            val result = channel.receive()
            Log.d("TAG", "result = $result")
        }



*/
    }
}