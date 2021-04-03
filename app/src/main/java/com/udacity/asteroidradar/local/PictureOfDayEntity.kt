package com.udacity.asteroidradar.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PictureOfDayEntity(
    @PrimaryKey
    val url: String,
    val title: String,
    val mediaType: String
)
