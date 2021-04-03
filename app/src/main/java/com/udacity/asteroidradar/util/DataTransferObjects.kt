package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.local.AsteroidEntity
import com.udacity.asteroidradar.local.PictureOfDayEntity

fun List<AsteroidEntity>.toDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun PictureOfDayEntity.toDomainModel(): PictureOfDay {
    return PictureOfDay(
        url = this.url,
        title = this.title,
        mediaType = this.mediaType
    )
}

fun List<Asteroid>.toDatabaseModel(): Array<AsteroidEntity> {
    return map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun PictureOfDay.toDatabaseModel(): PictureOfDayEntity {
    return PictureOfDayEntity(
        url = this.url,
        title = this.title,
        mediaType = this.mediaType

    )
}