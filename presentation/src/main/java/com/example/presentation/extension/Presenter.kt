package com.example.presentation.extension

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Presenter : CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext get() = Dispatchers.Default + job

    fun cancel() {
        println(".HiloCancel..")
        job.cancel()
    }

    fun schedule() = launch {
        while (true) {
            println(".HiloStar..")
            delay(300000)
        }
    }

}