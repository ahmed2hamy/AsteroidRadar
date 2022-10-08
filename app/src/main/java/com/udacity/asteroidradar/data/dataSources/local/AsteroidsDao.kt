package com.udacity.asteroidradar.data.dataSources.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.domain.entities.Asteroid

@Dao
interface AsteroidsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroidsToDatabase(asteroids: List<AsteroidDatabaseEntity>)

    @Query(
        "SELECT * FROM ${DatabaseConstants.asteroidsTableName} " +
                "ORDER BY ${DatabaseConstants.closeApproachDate} ASC"
    )
    fun getAllAsteroidsFromDatabase(): LiveData<List<AsteroidDatabaseEntity>>
}