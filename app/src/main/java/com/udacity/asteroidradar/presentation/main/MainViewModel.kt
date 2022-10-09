package com.udacity.asteroidradar.presentation.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import com.udacity.asteroidradar.domain.entities.Asteroid
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _asteroidsDatabase = AsteroidsDatabase.getInstance(application)

    private val _asteroidsRepository = AsteroidsRepository(NasaApi, _asteroidsDatabase)

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                _asteroidsRepository.refreshAsteroids()
            }catch (e: Exception){
                Timber.e("AsteroidsRepositoryException: $e")
            }
        }
    }

    init {
        refreshAsteroids()
        _asteroidList.value = _asteroidsRepository.asteroids.value
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

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}