package com.dosbots.flixme.data.di

import android.content.Context
import com.dosbots.flixme.data.dabase.LocalDatabase
import com.dosbots.flixme.data.dabase.dao.MoviesDao
import com.dosbots.flixme.data.dabase.dao.MyMoviesListsDao
import com.dosbots.flixme.data.dabase.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return LocalDatabase.getInstance(context)
    }

    @Provides
    fun provideUsersDao(localDatabase: LocalDatabase): UsersDao {
        return localDatabase.usersDao()
    }

    @Provides
    fun provideMoviesDao(localDatabase: LocalDatabase): MoviesDao {
        return localDatabase.moviesDao()
    }

    @Provides
    fun provideMyMoviesListsDao(localDatabase: LocalDatabase): MyMoviesListsDao {
        return localDatabase.myMoviesListsDao()
    }
}
