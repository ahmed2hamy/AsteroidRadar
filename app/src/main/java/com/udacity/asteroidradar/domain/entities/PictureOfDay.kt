package com.udacity.asteroidradar.domain.entities

import com.squareup.moshi.Json
import com.udacity.asteroidradar.data.dataSources.local.PictureOfDayDatabaseEntity

data class PictureOfDay(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)


fun PictureOfDay.asDatabaseModel(): PictureOfDayDatabaseEntity {
    return PictureOfDayDatabaseEntity(
        url = url,
        mediaType = mediaType,
        title = title,
    )
}

fun PictureOfDayDatabaseEntity.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        url = url,
        mediaType = mediaType,
        title = title,
    )
}