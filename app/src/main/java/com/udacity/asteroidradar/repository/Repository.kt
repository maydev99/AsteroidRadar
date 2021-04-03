package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.local.LocalDatabase
import com.udacity.asteroidradar.util.DateUtil
import com.udacity.asteroidradar.util.toDatabaseModel
import com.udacity.asteroidradar.util.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository(private val database: LocalDatabase) {

    val asteroidData: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) { it.toDomainModel()}



    val pictureData: LiveData<PictureOfDay> =
        Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
            it?.toDomainModel()
        }

    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            refreshAsteroidData()
            refreshPictureOfDayData()
        }
    }

    private suspend fun refreshPictureOfDayData() {
        try {
            val networkData = Network.api.getPictureOfTheDay()
            database.pictureOfDayDao.insertPicture(networkData.toDatabaseModel())
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.i("TAG", "Picture of Day Refresh Failed")
            }
        }
    }

    private suspend fun refreshAsteroidData() {
        try {
            val networkData = Network.api.getAsteroids(DateUtil.today(), DateUtil.oneWeekLater())
            val asteroidData = parseAsteroidsJsonResult(JSONObject(networkData))
            database.asteroidDao.insertAsteroids(*asteroidData.toDatabaseModel())
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.i("TAG", "Asteroid Refresh Failed")
            }
        }
    }
}