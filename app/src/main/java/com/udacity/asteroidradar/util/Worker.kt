package com.udacity.asteroidradar.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.local.getDatabase
import com.udacity.asteroidradar.repository.Repository
import retrofit2.HttpException

class Worker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)
        return try {
            repository.refreshData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}