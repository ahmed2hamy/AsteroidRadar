package com.udacity.asteroidradar.data.dataSources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.getTodayDate
import java.sql.Date

@Entity(tableName = DatabaseConstants.pictureOfDayTableName)
data class PictureOfDayDatabaseEntity (
    @PrimaryKey
    val date: String,

    @ColumnInfo(name = DatabaseConstants.mediaType)
    val mediaType: String,

    val title: String,

    val url: String,
)
