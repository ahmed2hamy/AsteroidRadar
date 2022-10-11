package com.udacity.asteroidradar.data.dataSources.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDayToDatabase(picture: PictureOfDayDatabaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroidsToDatabase(asteroids: List<AsteroidDatabaseEntity>)

    @Query(
        "SELECT * FROM ${DatabaseConstants.pictureOfDayTableName} " +
                "ORDER BY date DESC LIMIT 1"
    )
    fun getPictureOfDayFromDatabase(): LiveData<PictureOfDayDatabaseEntity>

    @Query(
        "SELECT * FROM ${DatabaseConstants.asteroidsTableName} " +
                "ORDER BY ${DatabaseConstants.closeApproachDate} ASC"
    )
    fun getAllAsteroidsFromDatabase(): LiveData<List<AsteroidDatabaseEntity>>

    @Query(
        "SELECT * FROM ${DatabaseConstants.asteroidsTableName} " +
                "WHERE ${DatabaseConstants.closeApproachDate} LIKE :dateString"
    )
    fun getAsteroidsFromDateFromDatabase(dateString: String): LiveData<List<AsteroidDatabaseEntity>>

    @Query("Delete from ${DatabaseConstants.asteroidsTableName}")
    suspend fun clearAsteroidsTable()

    @Query("Delete from ${DatabaseConstants.pictureOfDayTableName}")
    suspend fun clearPictureOfDayTable()
}