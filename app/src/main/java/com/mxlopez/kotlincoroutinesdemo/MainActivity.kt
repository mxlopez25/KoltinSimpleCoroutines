package com.mxlopez.kotlincoroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mxlopez.kotlincoroutinesdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    private var BACKGROUND_SERVICE = true

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        
        binding.btnRunProcess.setOnClickListener {
            CoroutineScope(IO).launch {
                runAsyncTask()
            }
        }

        binding.btnBackgroundProcess.setOnClickListener {
            CoroutineScope(IO).launch {
                startBackgroundProcess()
            }
        }

        binding.btnStopProcess.setOnClickListener {
            setBackgroundService()
        }
    }

    private suspend fun runAsyncTask() {
        withContext(IO) {
            val r = async {
                delay(5000)
                "Good Answer"
            }.await()
            setTextToView(r)
        }
    }

    private suspend fun setTextToView(r: String) {
        withContext(Main) {
            binding.tvSampleText.text = r
        }
    }

    private suspend fun startBackgroundProcess() {
        Log.d("startBackgroundProcess", "Initializing background process")
        BACKGROUND_SERVICE = true
        while (BACKGROUND_SERVICE) {
            delay(2000)
            setTextOnMainThreadBackgroundService()
        }

        withContext(Main) {
            binding.tvBackgroundStatus.text = "Background Status: Stopped at ${System.currentTimeMillis()}"
        }
    }

    private suspend fun setTextOnMainThreadBackgroundService() {
        withContext(Main) {
            setTextBackgroundService()
        }
    }

    private fun setTextBackgroundService() {
        binding.tvBackgroundStatus.text = "Background Status: Running, Last update ${System.currentTimeMillis()}"
    }

    private fun setBackgroundService() {
        BACKGROUND_SERVICE = !BACKGROUND_SERVICE
    }
}