package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {
    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    init {
        _asteroidList.value = listOf(
            Asteroid(
                id = 120, codename = "(2015 RC)", closeApproachDate = "2015-09-08",
                absoluteMagnitude = 24.3, estimatedDiameter = 0.0820427065,
                relativeVelocity = 19.4850295284, distanceFromEarth = 0.0269230459,
                isPotentiallyHazardous = false
            ),
            Asteroid(
                id = 120, codename = "(2015 RC)", closeApproachDate = "2015-09-08",
                absoluteMagnitude = 24.3, estimatedDiameter = 0.0820427065,
                relativeVelocity = 19.4850295284, distanceFromEarth = 0.0269230459,
                isPotentiallyHazardous = true
            ),
            Asteroid(
                id = 120, codename = "(2015 RC)", closeApproachDate = "2015-09-08",
                absoluteMagnitude = 24.3, estimatedDiameter = 0.0820427065,
                relativeVelocity = 19.4850295284, distanceFromEarth = 0.0269230459,
                isPotentiallyHazardous = false
            ),
        )
    }

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()

    val navigateToAsteroidDetails: LiveData<Asteroid?>
        get() = _navigateToAsteroidDetails

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsDone(){
        _navigateToAsteroidDetails.value = null
    }
}