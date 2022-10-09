package com.udacity.asteroidradar.presentation.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import com.udacity.asteroidradar.domain.entities.Asteroid
import com.udacity.asteroidradar.formatStringToDate
import com.udacity.asteroidradar.getNextWeekDate
import com.udacity.asteroidradar.getTodayDate
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {


    // ViewModel Factory
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }

    private val _asteroidsDatabase = AsteroidsDatabase.getInstance(application)

    private val _asteroidsRepository = AsteroidsRepository(NasaApi, _asteroidsDatabase)
    var asteroidList = _asteroidsRepository.asteroids

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                _asteroidsRepository.refreshAsteroids(getTodayDate(), null)
            } catch (e: Exception) {
                Timber.tag("AsteroidsRepositoryException").e(e)
            }
        }
    }

    fun getWeekAsteroids() {
        asteroidList = Transformations.switchMap(
            _asteroidsRepository.asteroids
        ) { asteroids ->
            val filteredAsteroids = MutableLiveData<List<Asteroid>>()
            val filteredAsteroidsList = asteroids.filter {
                val date = formatStringToDate(it.closeApproachDate)

                val nextWeekDate = formatStringToDate(getNextWeekDate())

                date?.equals(Calendar.getInstance().time) == true || date?.before(
                    nextWeekDate
                ) == true

            }
            filteredAsteroids.value = filteredAsteroidsList
            filteredAsteroids
        }

    }

    fun getTodayAsteroids() {
        asteroidList = Transformations.switchMap(
            _asteroidsRepository.asteroids
        ) { asteroids ->
            val filteredAsteroids = MutableLiveData<List<Asteroid>>()
            val filteredAsteroidsList = asteroids.filter {
                val date = formatStringToDate(it.closeApproachDate)

                date?.equals(Calendar.getInstance().time) == true
            }
            filteredAsteroids.value = filteredAsteroidsList
            filteredAsteroids
        }
    }

    fun getSavedAsteroids() {
        asteroidList = _asteroidsRepository.asteroids
    }


    init {
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