package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    return formatDateToString(calendar.time)
}

fun getNextWeekDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    return formatDateToString(calendar.time)
}

private fun formatDateToString(date: Date): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}

fun formatStringToDate(sateString: String): Date? {
    return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).parse(sateString)
}