package com.example.apitesting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
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

        lifecycleScope.launch {
            val commentsDeferred = async { give7SecondsDelay() }
            val albumsDeferred = async { give2SecDelay() }

            val deferredList = listOf(commentsDeferred, albumsDeferred)

            val result = withContext(lifecycleScope.coroutineContext) {
                select {
                    commentsDeferred.onAwait { it }
                    albumsDeferred.onAwait { it }
                }
            }

            Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()

            for (deferred in deferredList) {
                if (deferred.isCompleted) continue
                val otherResult = deferred.await()
                Toast.makeText(applicationContext, otherResult, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private suspend fun give7SecondsDelay() : String {
        delay(7000L)
        return "7 Second delay"
    }

    private suspend fun give2SecDelay() : String {
        delay(2000L)
        return "2 Second delay"
    }
}