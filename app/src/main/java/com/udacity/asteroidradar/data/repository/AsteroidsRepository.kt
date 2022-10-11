package com.udacity.asteroidradar.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.dataSources.remote.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.entities.Asteroid
import com.udacity.asteroidradar.domain.entities.PictureOfDay
import com.udacity.asteroidradar.domain.entities.asDatabaseModel
import com.udacity.asteroidradar.domain.entities.asDomainModel
import com.udacity.asteroidradar.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidsRepository(
    private val nasaApi: NasaApi,
    private val database: AsteroidsDatabase
) {

    fun getPictureOfDayLiveData(): LiveData<PictureOfDay> = Transformations.map(
        database.asteroidsDao.getPictureOfDayFromDatabase()
    ) {
        it?.asDomainModel()
    }

    fun getAllAsteroidsLiveData(): LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidsDao.getAllAsteroidsFromDatabase()
    ) {
        it?.asDomainModel()
    }

    fun getTodayAsteroidsLiveData(todayDate: String): LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidsDao.getAsteroidsFromDateFromDatabase(todayDate)
    ) {
        it?.asDomainModel()
    }


    suspend fun refreshAsteroids(
        startDate: String?,
        endDate: String?
    ) {
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = nasaApi.retrofitService.getAsteroidsFeed(
                startDate, endDate,
                Constants.API_KEY
            )

            val asteroids: ArrayList<Asteroid> =
                parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))

            database.asteroidsDao.clearAsteroidsTable()

            database.asteroidsDao.insertAsteroidsToDatabase(asteroids.asDatabaseModel())
        }
    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            val picture: PictureOfDay = nasaApi.retrofitService.getPictureOfDay(
                Constants.API_KEY
            )

            database.asteroidsDao.clearPictureOfDayTable()

            database.asteroidsDao.insertPictureOfDayToDatabase(picture.asDatabaseModel(getTodayDate()))
        }
    }
}