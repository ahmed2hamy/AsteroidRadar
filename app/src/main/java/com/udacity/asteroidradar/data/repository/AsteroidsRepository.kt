package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.dataSources.remote.getEndDate
import com.udacity.asteroidradar.data.dataSources.remote.getStartDate
import com.udacity.asteroidradar.data.dataSources.remote.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.entities.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidsRepository(private val nasaApi: NasaApi) {
    suspend fun refreshAsteroids(
        startDate: String = getStartDate(),
        endDate: String = getEndDate()
    ): ArrayList<Asteroid> =
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = nasaApi.retrofitService.getAsteroidsFeed(
                startDate, endDate,
                Constants.API_KEY
            )

            return@withContext parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
        }

}