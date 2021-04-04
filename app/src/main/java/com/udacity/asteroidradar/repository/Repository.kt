package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    enum class FilterBy { TODAY, WEEK, SAVED }

    private val filterBy = MutableLiveData(FilterBy.WEEK)

    /*val asteroidData: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) { it.toDomainModel()}*/

    val asteroidData: LiveData<List<Asteroid>> =
        Transformations.switchMap(filterBy) { filter ->
            when (filter) {
                FilterBy.TODAY ->
                    Transformations.map(database.asteroidDao.getTodaysAsteroids(DateUtil.today())) {
                        it.toDomainModel()
                    }
                FilterBy.WEEK ->
                    Transformations.map(
                        database.asteroidDao.getWeeksAsteroids(
                            DateUtil.today(),
                            DateUtil.oneWeekLater()
                        )
                    ) { it.toDomainModel() }

                FilterBy.SAVED ->
                    Transformations.map(database.asteroidDao.getAllAsteroids()) { it.toDomainModel() }
            }

        }

    fun addFilter(filter: FilterBy) {
        filterBy.value = filter
    }


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