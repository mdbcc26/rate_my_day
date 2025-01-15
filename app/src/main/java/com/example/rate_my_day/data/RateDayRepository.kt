package com.example.rate_my_day.data

import com.example.rate_my_day.data.db.RateDayEntity
import com.example.rate_my_day.data.db.RateDaysDao
import kotlinx.coroutines.flow.Flow

class RateDayRepository(private val rateDaysDao: RateDaysDao) {
    fun getAllRatedDays(): Flow<List<RateDayEntity>> = rateDaysDao.findAllRatedDays()

    suspend fun saveRatedDay(rateDayEntity: RateDayEntity) {
        rateDaysDao.addRateDay(rateDayEntity)
    }

    suspend fun deleteRatedDayByDate(date:Long) {
        rateDaysDao.deleteRateDayByDate(date)
    }

    suspend fun updateComment(date: Long, comment: String) {
        rateDaysDao.updateCommentByDate(date, comment)
    }
/**
    suspend fun getRateDayByDate(date: Long): RateDayEntity? {
        return rateDaysDao.getRateDayByDate(date)
    }
    */
}