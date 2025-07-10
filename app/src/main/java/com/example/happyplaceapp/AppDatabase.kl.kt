package com.example.happyplaceapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.happyplaceapp.PlaceDao

// Die Datenbank mit der tabelle places
@Database(entities = [Place::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    //  Zugriff auf das DAO
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "places_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
