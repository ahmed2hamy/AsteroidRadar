package com.udacity.asteroidradar.presentation.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import com.udacity.asteroidradar.domain.entities.Asteroid
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _asteroidsDatabase = AsteroidsDatabase.getInstance(application)

    private val _asteroidsRepository = AsteroidsRepository(NasaApi, _asteroidsDatabase)

    private fun refreshAsteroids() {
        viewModelScope.launch {
            _asteroidsRepository.refreshAsteroids()
        }
    }

    init {
        _asteroidList.value = _asteroidsRepository.asteroids.value
        refreshAsteroids()
    }

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid?>()

    val navigateToAsteroidDetails: LiveData<Asteroid?>
        get() = _navigateToAsteroidDetails

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsDone() {
        _navigateToAsteroidDetails.value = null
    }
}