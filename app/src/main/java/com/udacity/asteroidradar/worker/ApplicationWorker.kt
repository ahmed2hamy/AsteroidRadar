package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import com.udacity.asteroidradar.getTodayDate
import retrofit2.HttpException
import timber.log.Timber

class RefreshAsteroidsWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val asteroidsDatabase = AsteroidsDatabase.getInstance(context)

        val asteroidsRepository = AsteroidsRepository(NasaApi, asteroidsDatabase)


        return try {

            clearDatabaseTables(asteroidsRepository)
            refreshPictureOfDay(asteroidsRepository)
            refreshAsteroids(asteroidsRepository)

            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private suspend fun clearDatabaseTables(repository: AsteroidsRepository){
        try {
            repository.clearDatabaseTables()

        } catch (e: Exception) {
            Timber.tag("AsteroidsRepositoryException").e(e)
        }
    }

    private suspend fun refreshPictureOfDay(repository: AsteroidsRepository) {
        try {
            repository.refreshPictureOfDay()

        } catch (e: Exception) {
            Timber.tag("AsteroidsRepositoryException").e(e)
        }

    }

    private suspend fun refreshAsteroids(repository: AsteroidsRepository) {
        try {
            repository.refreshAsteroids(getTodayDate(), null)

        } catch (e: Exception) {
            Timber.tag("AsteroidsRepositoryException").e(e)
        }

    }

}