package com.dosbots.flixme.data.dabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dosbots.flixme.data.models.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

    companion object {
        private var instance: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "LocalDatabase"
                ).build().also { newDatabaseInstance ->
                    instance = newDatabaseInstance
                }
            }
        }
    }
}