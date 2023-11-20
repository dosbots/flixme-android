package com.dosbots.flixme.data.di

import com.dosbots.flixme.data.repository.AuthenticationRepository
import com.dosbots.flixme.data.repository.AuthenticationRepositoryImpl
import com.dosbots.flixme.data.repository.MoviesRepository
import com.dosbots.flixme.data.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthenticationRepository(
        repositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindMoviesRepository(
        repositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}
