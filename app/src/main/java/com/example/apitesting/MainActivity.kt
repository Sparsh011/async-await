package com.example.apitesting

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActTag"

    //    Make multiple network calls and use the result in FCFS manner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//    1. order of await matters
//    2. Select expression makes it possible to await multiple suspending functions simultaneously and select the first one that becomes available. https://kotlinlang.org/docs/select-expression.html

//      Use of select -
        lifecycleScope.launch {
            val deferred1 = async { give7SecondsDelay() }
            val deferred2 = async { give2SecDelay() }

            val deferredList = listOf(deferred1, deferred2)

            val result = withContext(lifecycleScope.coroutineContext) {
                select {
                    deferred1.onAwait { it }
                    deferred2.onAwait { it }
                }
            }

            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()

            for (deferred in deferredList) {
                if (deferred.isCompleted) continue
                val otherResult = deferred.await()
                Toast.makeText(applicationContext, otherResult, Toast.LENGTH_SHORT).show()
            }
        }


//        All 4 results will be logged together after 7 seconds have elapsed.
        lifecycleScope.launch {
            val deferred1 = async { give7SecondsDelay() }
            val deferred2 = async { give2SecDelay() }
            val deferred3 = async { give7SecondsDelay() }
            val deferred4 = async { give2SecDelay() }

            val results = awaitAll(deferred1, deferred2, deferred3, deferred4)

            for (result in results) {
                Log.d(TAG, "awaitAll : $result")
            }
        }

    }


    private suspend fun give7SecondsDelay(): String {
        delay(7000L)
        return "7 Second delay"
    }

    private suspend fun give2SecDelay(): String {
        delay(2000L)
        return "2 Second delay"
    }
}