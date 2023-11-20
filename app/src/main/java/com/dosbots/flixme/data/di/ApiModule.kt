package com.dosbots.flixme.data.di

import com.dosbots.flixme.data.api.FirestoreUsersApi
import com.dosbots.flixme.data.api.MoviesApi
import com.dosbots.flixme.data.api.UsersApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseAPIsModule {

    @Binds
    abstract fun bindUsersApi(
        api: FirestoreUsersApi
    ): UsersApi
}

@InstallIn(SingletonComponent::class)
@Module
class RetrofitApiModule {

    @Provides
    fun provideMoviesApi(
        @Named("movies_api_retrofit_builder") retrofit: Retrofit.Builder
    ): MoviesApi {
        return retrofit
            .baseUrl("https://api.themoviedb.org/3/")
            .build()
            .create(MoviesApi::class.java)
    }
}
