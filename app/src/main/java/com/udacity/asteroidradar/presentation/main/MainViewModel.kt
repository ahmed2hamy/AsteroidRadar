package com.udacity.asteroidradar.presentation.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.dataSources.local.AsteroidsDatabase
import com.udacity.asteroidradar.data.dataSources.remote.NasaApi
import com.udacity.asteroidradar.data.repository.AsteroidsRepository
import com.udacity.asteroidradar.domain.entities.Asteroid
import com.udacity.asteroidradar.domain.entities.AsteroidsFilter
import com.udacity.asteroidradar.domain.entities.PictureOfDay
import com.udacity.asteroidradar.getNextWeekDate
import com.udacity.asteroidradar.getTodayDate
import kotlinx.coroutines.launch
import timber.log.Timber

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


    val pictureOfDay: LiveData<PictureOfDay> = _asteroidsRepository.getPictureOfDayLiveData()


    private val _onAsteroidsFilterChanged = MutableLiveData(AsteroidsFilter.All)

    fun onChangeFilter(filter: AsteroidsFilter) {
        _onAsteroidsFilterChanged.postValue(filter)
    }

    var asteroidList = Transformations.switchMap(_onAsteroidsFilterChanged) {
        when (it) {
            AsteroidsFilter.Week -> _asteroidsRepository.getAsteroidsWithStartAndEndDates(
                getTodayDate(),
                getNextWeekDate()
            )
            AsteroidsFilter.Today -> _asteroidsRepository.getTodayAsteroidsLiveData(getTodayDate())

            else -> _asteroidsRepository.getAllAsteroidsLiveData()
        }
    }


    private fun refreshPictureOfDay() {
        viewModelScope.launch {
            try {
                _asteroidsRepository.refreshPictureOfDay()

            } catch (e: Exception) {
                Timber.tag("AsteroidsRepositoryException").e(e)
            }
        }
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                _asteroidsRepository.refreshAsteroids(getTodayDate(), null)

            } catch (e: Exception) {
                Timber.tag("AsteroidsRepositoryException").e(e)
            }
        }
    }


    init {
        refreshPictureOfDay()

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