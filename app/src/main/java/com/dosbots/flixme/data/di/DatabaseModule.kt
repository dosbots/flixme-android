package com.dosbots.flixme.data.di

import android.content.Context
import com.dosbots.flixme.data.dabase.LocalDatabase
import com.dosbots.flixme.data.dabase.UsersDao
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
}
