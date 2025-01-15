package com.example.rate_my_day.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RateDayEntity::class], version = 2)
abstract class RateDaysDatabase : RoomDatabase() {

    abstract fun RateDaysDao(): RateDaysDao

    companion object {
        @Volatile
        private var Instance: RateDaysDatabase? = null
/**
        // Migration strategy
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE RatedDays ADD COLUMN comments TEXT DEFAULT NULL")
            }
        }
*/
        fun getDatabase(context: Context): RateDaysDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    RateDaysDatabase::class.java,
                    "rate_day_database")
                    .fallbackToDestructiveMigration()
                    //.addMigrations(MIGRATION_1_2) // Add migration strategy
                    .build()
                Instance = instance
                return instance
            }
        }
    }
}