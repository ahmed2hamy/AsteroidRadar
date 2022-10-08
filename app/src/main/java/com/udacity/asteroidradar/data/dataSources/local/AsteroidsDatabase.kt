package com.udacity.asteroidradar.data.dataSources.local

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [AsteroidDatabaseEntity::class], version = 1,  exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidsDatabase

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): AsteroidsDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(this) {
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