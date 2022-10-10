package com.udacity.asteroidradar.data.dataSources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DatabaseConstants.pictureOfDayTableName)
data class PictureOfDayDatabaseEntity (
    @PrimaryKey
    val url: String,

    @ColumnInfo(name = DatabaseConstants.mediaType)
    val mediaType: String,

    val title: String,
)