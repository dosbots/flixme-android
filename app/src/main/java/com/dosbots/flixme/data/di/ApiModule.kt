package com.dosbots.flixme.data.di

import com.dosbots.flixme.data.api.FirestoreUsersApi
import com.dosbots.flixme.data.api.UsersApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ApiModule {

    @Binds
    abstract fun bindUsersApi(
        api: FirestoreUsersApi
    ): UsersApi
}
