package com.longle.data.db

import androidx.room.*
import com.longle.data.model.local.WeatherEntity
import com.longle.data.util.getWeatherDate
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the WeatherEntity class.
 */
@Dao
abstract class WeatherDao {

    @Query("SELECT * FROM WeatherEntity WHERE keyword = :keyword AND date >= :currentDate ORDER BY date ASC")
    abstract fun getWeathers(keyword: String, currentDate: Long): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(weathers: List<WeatherEntity>)

    @Query("DELETE FROM WeatherEntity WHERE date < :currentDate")
    abstract fun cleanUp(currentDate: Long)

    @Transaction
    open fun updateAll(weathers: List<WeatherEntity>) {
        cleanUp(getWeatherDate())
        insertAll(weathers)
    }

    fun getValidDateWeathers(keyword: String): Flow<List<WeatherEntity>> {
        return getWeathers(keyword, getWeatherDate())
    }
}
