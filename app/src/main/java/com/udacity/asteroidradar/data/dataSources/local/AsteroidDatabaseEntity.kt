package com.udacity.asteroidradar.data.dataSources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DatabaseConstants.asteroidsTableName)
data class AsteroidDatabaseEntity(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = DatabaseConstants.codename)
    val codename: String,

    @ColumnInfo(name = DatabaseConstants.closeApproachDate)
    val closeApproachDate: String,

    @ColumnInfo(name = DatabaseConstants.absoluteMagnitude)
    val absoluteMagnitude: Double,

    @ColumnInfo(name = DatabaseConstants.estimatedDiameter)
    val estimatedDiameter: Double,

    @ColumnInfo(name = DatabaseConstants.relativeVelocity)
    val relativeVelocity: Double,

    @ColumnInfo(name = DatabaseConstants.distanceFromEarth)
    val distanceFromEarth: Double,

    @ColumnInfo(name = DatabaseConstants.isPotentiallyHazardous)
    val isPotentiallyHazardous: Boolean
)

