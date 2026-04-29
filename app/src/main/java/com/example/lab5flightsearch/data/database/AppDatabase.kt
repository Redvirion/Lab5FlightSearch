package com.example.lab5flightsearch.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab5flightsearch.data.database.dao.AirportDao
import com.example.lab5flightsearch.data.database.dao.FavoriteDao
import com.example.lab5flightsearch.data.database.entities.AirportEntity
import com.example.lab5flightsearch.data.database.entities.FavoriteEntity

@Database(
    entities = [AirportEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flight_search.db"
                )
                    .createFromAsset("flight_search.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}