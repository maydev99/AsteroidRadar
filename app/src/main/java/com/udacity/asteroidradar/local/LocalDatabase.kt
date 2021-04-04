package com.udacity.asteroidradar.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: AsteroidEntity)

    @Query("SELECT * FROM AsteroidEntity ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM AsteroidEntity WHERE closeApproachDate = :today")
    fun getTodaysAsteroids(today: String): LiveData<List<AsteroidEntity>>

    @Query("select * from AsteroidEntity where closeApproachDate between :startDate and :endDate")
    fun getWeeksAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Query("delete from AsteroidEntity where closeApproachDate < :days")
    fun deleteOldAsteroids(days: String)
}

@Dao
interface PictureOfDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(vararg picture: PictureOfDayEntity)

    @Query("SELECT * FROM PictureOfDayEntity")
    fun getPictureOfDay(): LiveData<PictureOfDayEntity>


}

@Database(
    entities = [AsteroidEntity::class, PictureOfDayEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: LocalDatabase

fun getDatabase(context: Context): LocalDatabase {
    synchronized(LocalDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "database"
            ).build()
        }
    }
    return INSTANCE
}