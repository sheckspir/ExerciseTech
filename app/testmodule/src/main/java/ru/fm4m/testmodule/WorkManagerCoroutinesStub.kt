package ru.fm4m.testmodule

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

class WorkManagerCoroutinesStub(val name: String) {
    private val job = SupervisorJob()

    //        val job = Job()
    private val scope =
        CoroutineScope(job + Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e("TAG", "some error happened somewhere ", throwable)
        })

    private fun doWorkException() = scope.async(Job()) {
        Log.d("TAG", "start doWorkException")
        delay(1500)
        if (true) {
            throw IllegalArgumentException()
        }
        1
    }

    fun doWorkWithNested() = scope.launch {
        Log.d("TAG", "start doWorkWithNested")
        val async = doWorkException()
        doWork1()
        launch {

            Log.d("TAG", "$name start work1_n")
            delay(2000)
            Log.d("TAG", "$name finish work1_n")
        }

        withContext(NonCancellable) {
            delay(1000)
            Log.d("TAG", "non cancellable finished")
        }
        try {
            async.await()
        } catch (e: CancellationException) {
            Log.d("TAG", "cancellation exception")
            throw e
        } catch (e: Exception) {
            Log.d("TAG", "some exception in doWorkException", e)
//                throw e
        }



        Log.d("TAG", "finish doWorkWithNested")
    }


    fun doWork1() = scope.launch {
        Log.d("TAG", "$name start work1")
        delay(2000)
        Log.d("TAG", "$name finish work1")
    }


    fun doWork2() = scope.launch {
        Log.d("TAG", "$name start work2")
        delay(1000)
        Log.d("TAG", "$name finish work2")
    }

    fun cancelAllWorksHard() {
        job.cancel()
        Log.d("TAG", "$name cancel job")
    }

    fun cancelAllWorsScope() {
        scope.cancel()
        Log.d("TAG", "$name cancel scope")
    }

    fun cancelAllChildrens() {
        scope.coroutineContext.cancelChildren()
        Log.d("TAG", "$name cancel children")
    }

    fun simpleSubscribe() {
        job.invokeOnCompletion {
            if (it == null) {
                Log.d("TAG", "$name main job finished success")
            } else {
                Log.e("TAG", "$name main job finished with error")
            }
        }
    }


    private var channel = Channel<String>(capacity = Channel.BUFFERED)

    fun sendListOfMessage(vararg sendValues: String) {
        scope.launch {
            for (one in sendValues) {
//                Log.d("TAG", "presend message $one")
                channel.send(one)
                Log.d("TAG", "send message $one")
                delay(10)
            }
        }
    }

    fun stopChannel() {
        channel.close()
    }

    fun receiveListOfMessage(task: (String) -> Unit) {
        scope.launch {
            delay(1000)
            task.invoke(channel.receive())

            for (one in channel) {
                task.invoke(one)
            }
            Log.d("TAG", "end channel iterator")
        }
    }


    private var taskForActor: ((String) -> Unit)? = null
    val actorChannel: SendChannel<String> = scope.actor(capacity = 2, context = Dispatchers.IO) {
        delay(2000)
        for (msg in channel) {
            taskForActor?.invoke(msg)
        }

    }


    fun sendTaskForActor(newTask: (String) -> Unit) {
        taskForActor = newTask

    }

    fun sendListOfMessageToActor(vararg sendValues: String) {
        scope.launch {
            for (one in sendValues) {
                Log.d("TAG", "presend message $one")
                actorChannel.send(one)
                Log.d("TAG", "send message $one")
                delay(10)
            }
        }
    }


    @Volatile
    private var count = 0
    private val mutex = Mutex()

    private val actorChangeIncreaseCount: SendChannel<ActorCommandClass> = scope.actor {
        for (command in this) {
            when (command) {
                is ActorCommandClass.Add -> {
                    count++
                }

                is ActorCommandClass.Print -> {
                    Log.d("TAG", "Print command count $count")
                }

                is ActorCommandClass.ReturnValue -> {
                    Log.d("TAG", "getMessage ReturnValue")
                    command.response.complete(count)
                }


            }
        }
    }

    fun calculateCount(tryCount: Int) {
        for (i in 0 until tryCount) {
            scope.launch {
                for (i in 0 until tryCount) {
//                    mutex.withLock {
//                        val oldCount = getCount()
//                        val newCount = oldCount + 1
//                        sendCount(newCount)
                    count++
//                    }

//                    actorChangeIncreaseCount.send(ActorCommandClass.Add())

                }
                if (i >= tryCount - 1) {
                    launch {
                        delay(1000)
                        actorChangeIncreaseCount.send(ActorCommandClass.Print())

                    }

                }
            }
        }

        //temp get value

        scope.launch {
            val getCommand = CompletableDeferred<Int>()
            actorChangeIncreaseCount.send(ActorCommandClass.ReturnValue(getCommand))
            val result = getCommand.await()
            Log.d("TAG", "getCommand.response $result")
        }


    }

    fun sendCount(newCount: Int) {

        count = newCount
    }

    fun getCount(): Int {
        return count
    }

    private open class ActorCommandClass {
        class Add : ActorCommandClass()
        class Print : ActorCommandClass()
        class ReturnValue(val response: CompletableDeferred<Int>) : ActorCommandClass()
    }

    private val coldFlowError = flow {
        var emittedValue = 0
        while (currentCoroutineContext().isActive) {
            delay(50)
            emit(emittedValue++)
            if (emittedValue == 50) {
                throw IllegalArgumentException()
            }
        }
    }.onEach {
        Log.d("TAG", "onEach $it")
    }

    private val coldFlow = flow {
        var emittedValue = 0
        while (currentCoroutineContext().isActive && emittedValue < 100) {
            delay(200)
            emit(emittedValue++)
            Log.d("TAG", "coldFlow emitted $emittedValue")
        }
    }.onEach {
        Log.d("TAG", "onEach $it")
    }.onCompletion {
        Log.d("TAG", "onComplete coldFlow1")
    }

    private val coldFlow2 = flow {
        var emittedValue = 0
        while (currentCoroutineContext().isActive && emittedValue < 13) {
            delay(2_000)
            Log.d("TAG", "preemit coldFlow2")
            emit("Stroka#${emittedValue++}")
        }
    }

    private val only2ValuesFlow = flow {
        delay(1000)
        emit("oneValue")
        delay(1000)
        emit("secondValue")
    }

    var flowJob: Job? = null

    fun subscribeToFlow(subscriber: (Int) -> Unit, onComplete: () -> Unit) {
        flowJob = scope.launch(SupervisorJob()) {
            delay(1000)
            Log.d("TAG", "Collect Flow")
            coldFlow
                .onCompletion { onComplete.invoke() }
                .collect {
                    subscriber.invoke(it)
                }

            //launchIn == collect?
        }
    }

    fun stopFlow() {
        scope.launch {
            delay(3000)
            flowJob?.cancel()
        }
    }

    fun differentTypesStartZip() {
        scope.launch {
            coldFlow.zip(coldFlow2) { v1, v2 ->
                Log.d("TAG", "zip process")
                "$v2 $v1"
            }.collect {
                Log.d("TAG", "zip result $it")
            }
        }
    }

    fun differentTypesCombine() {
        scope.launch {
            coldFlow2.combine(coldFlow) { v2, v1 ->
                Log.d("TAG", "combine process")
                "$v2 $v1"
            }.collect {
                Log.d("TAG", "combine result $it")
            }
        }
    }

    fun withDebounce() {
        scope.launch {

            flow {
                emit(1)
                delay(90)
                emit(2)
                delay(90)
                emit(3)
                delay(1010)
                emit(4)
                delay(990)
                emit(5)
                delay(1010)
                emit(6)
            }.debounce(1000)
                .collect {
                    Log.d("TAG", "debounce test $it")
                    //3 5 6
                }

        }
    }

    fun flowWithSample(subscriber: (Int) -> Unit) {
        scope.launch {
            coldFlow.sample(1000)
                .collect {
                    subscriber.invoke(it)
                }
        }
    }


    fun flowWithError() {
        scope.launch {
            coldFlowError.catch {
                Log.d("TAG", "some error happened", it)
                this.emit(999)
            }.collect()
        }
    }

    fun flowWithFlatMapConcat(subscriber: (String) -> Unit) {
        scope.launch {
            coldFlow.flatMapConcat { flow1Value ->
                only2ValuesFlow.map { flow2Value ->
                    "$flow2Value $flow1Value"
                }
            }.collect {
                subscriber.invoke(it)
            }
        }
    }

    fun flowWithFlatMapLatest(subscriber: (String) -> Unit) {
        scope.launch {
            coldFlow.flatMapLatest { flow1Value ->
                only2ValuesFlow.map { flow2Value ->
                    "$flow2Value $flow1Value"
                }
            }.collect {
                subscriber.invoke(it)
            }
        }
    }

    fun merge() {
        scope.launch {
            val mergedFlow = merge(coldFlow, coldFlow2)

            mergedFlow.collect{
                Log.d("TAG", it.toString())
            }
        }

    }

    fun flowWithFlatMapMerge(subscriber: (String) -> Unit) {
        scope.launch {
            coldFlow.flatMapMerge { flow1Value ->
                only2ValuesFlow.map { flow2Value ->
                    "$flow2Value $flow1Value"
                }
            }.collect {
                subscriber.invoke(it)
            }
        }
    }

    fun flowWithBuffer(subscriber: (String) -> Unit) {
        scope.launch {
            coldFlow.buffer(10, BufferOverflow.DROP_OLDEST)
                .flatMapConcat { flowValue1 ->
                    only2ValuesFlow.map {
                        "$flowValue1 $it"
                    }
                }.collect {
                    subscriber.invoke(it)
                }

        }
    }

    fun flowFlowOnContext() {
        scope.launch {
            coldFlow
                .map {
                    Dispatchers.Main
                    Log.d("TAG", "map in ${Thread.currentThread()}")
                    it
                }
                .flowOn(Dispatchers.Main)
                .map {
                    Log.d("TAG", "second map in ${Thread.currentThread()}")
                    it
                }.flowOn(Dispatchers.Default)
                .map {
                    Log.d("TAG", "third map in ${Thread.currentThread()}")
                    it
                }.flowOn(Dispatchers.Main)
                .collect()

        }
    }

    private val stateFlow = MutableStateFlow(-1)

    fun startEmitInStateFlow(delay: Long) {
        scope.launch {
            delay(delay)
            for (i in 1..50) {
                delay(100)
                stateFlow.emit(i)
            }
        }
    }

    fun onStateFlowObserve(subscriber: (String) -> Unit) {
        stateFlow
            .map { it.toString() }
            .onEach { subscriber.invoke(it) }
            .launchIn(scope)
    }


    private val sharedFlow = MutableSharedFlow<Int>(3, 0, BufferOverflow.DROP_LATEST)

    private val shortSharedFlow = sharedFlow.shareIn(scope, SharingStarted.Eagerly, 0)

    fun startEmitInSharedFlow(delay: Long) {
        scope.launch {
            delay(delay)
            for (i in 1..30) {
                delay(100)
                sharedFlow.emit(i)
            }
        }
    }

    fun onSharedFlowObserve(subscriber: (String) -> Unit) {
        shortSharedFlow
            .map { it.toString() }
            .onEach {
                subscriber.invoke(it)
                Log.d("TAG", "subscribeCount = ${sharedFlow.subscriptionCount.value}")
            }

    }

    fun someLongTask(result : ()-> Unit) {
        scope.launch {
            Log.d("TAG", "before delay")
            delay(2000)
            result.invoke()


        }
    }


}