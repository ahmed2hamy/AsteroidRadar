package com.udacity.asteroidradar.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.dataSources.remote.getEndDate
import com.udacity.asteroidradar.data.dataSources.remote.getStartDate
import com.udacity.asteroidradar.data.dataSources.remote.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.entities.Asteroid
import com.udacity.asteroidradar.domain.entities.asDatabaseModel
import com.udacity.asteroidradar.domain.entities.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidsRepository(
    private val nasaApi: NasaApi,
    private val database: AsteroidsDatabase
) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidsDao.getAllAsteroidsFromDatabase()
    ) {
        it.asDomainModel()
    }


    suspend fun refreshAsteroids(
        startDate: String = getStartDate(),
        endDate: String = getEndDate()
    ) {
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = nasaApi.retrofitService.getAsteroidsFeed(
                startDate, endDate,
                Constants.API_KEY
            )

            val asteroids: ArrayList<Asteroid> =
                parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))

            database.asteroidsDao.insertAsteroidsToDatabase(asteroids.asDatabaseModel())
        }
    }
}