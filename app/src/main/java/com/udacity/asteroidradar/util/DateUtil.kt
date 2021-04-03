package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.util.Constants.API_QUERY_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun today(): String {
        val calendar =  Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun oneWeekLater(): String {
        val calendar =  Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }
}