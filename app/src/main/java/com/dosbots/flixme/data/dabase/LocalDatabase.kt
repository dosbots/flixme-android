package com.dosbots.flixme.data.dabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.MyMoviesList
import com.dosbots.flixme.data.models.MyMoviesListItem
import com.dosbots.flixme.data.models.User
import com.dosbots.flixme.data.models.PredefinedListItem

@Database(
    entities = [
        User::class,
        Movie::class,
        PredefinedListItem::class,
        MyMoviesList::class,
        MyMoviesListItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun moviesDao(): MoviesDao
    abstract fun myMoviesListsDao(): MyMoviesListsDao

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