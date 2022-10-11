package com.udacity.asteroidradar.data.dataSources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [AsteroidDatabaseEntity::class, PictureOfDayDatabaseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidsDatabase

        fun getInstance(context: Context): AsteroidsDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AsteroidsDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidsDatabase::class.java,
                        DatabaseConstants.name
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}