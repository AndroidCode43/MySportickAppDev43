package com.example.mysportickappdev43.Main.fragments.startRun

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class GpsWorker(context : Context, workerParameters : WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {

        Log.d("test: ", "SUCCESS WORKER!")
        return Result.success()
    }

}