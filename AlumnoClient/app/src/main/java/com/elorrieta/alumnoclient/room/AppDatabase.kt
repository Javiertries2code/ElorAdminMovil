package com.elorrieta.alumnoclient.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomUser::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(LOCK) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "usersDatabase"
                ).build().also { instance = it }
            }
        }
    }
}
