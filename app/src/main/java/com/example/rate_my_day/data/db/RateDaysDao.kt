package com.example.rate_my_day.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDaysDao {

    //Create a rated day
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRateDay(rateDayEntity: RateDayEntity)

    //Update an already existing date
    @Update
    suspend fun updateRateDay(rateDayEntity: RateDayEntity)

    //Custom query to delete by date (if only date is available)
    @Query("DELETE FROM RatedDays WHERE date = :date")
    suspend fun deleteRateDayByDate(date: Long)

    //Searches for all rated days
    @Query("SELECT * FROM RatedDays")
    fun findAllRatedDays(): Flow<List<RateDayEntity>>

    @Query("SELECT * FROM RatedDays WHERE date = :date")
    suspend fun getRateDayByDate(date: Long): RateDayEntity?
}