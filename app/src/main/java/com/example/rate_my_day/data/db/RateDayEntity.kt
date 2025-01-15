package com.example.rate_my_day.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RatedDays")
data class RateDayEntity(
    @PrimaryKey
    val date: Long,
    val stars: Int,
    val comment: String? = null
)
