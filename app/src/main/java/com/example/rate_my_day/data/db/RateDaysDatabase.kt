package com.example.rate_my_day.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RateDayEntity::class], version = 1)
abstract class RateDaysDatabase : RoomDatabase() {

    abstract fun RateDaysDao(): RateDaysDao

    companion object {
        @Volatile
        private var Instance: RateDaysDatabase? = null

        fun getDatabase(context: Context): RateDaysDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, RateDaysDatabase::class.java, "rate_day_database")
                    .fallbackToDestructiveMigration()
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}