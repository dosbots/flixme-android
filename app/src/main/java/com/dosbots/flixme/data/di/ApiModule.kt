package com.dosbots.flixme.data.di

import com.dosbots.flixme.data.api.lists.FirestoreMyMoviesListApi
import com.dosbots.flixme.data.api.lists.MyMoviesListApi
import com.dosbots.flixme.data.api.users.FirestoreUsersApi
import com.dosbots.flixme.data.api.movies.MoviesApi
import com.dosbots.flixme.data.api.users.UsersApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseAPIsModule {

    @Binds
    abstract fun bindUsersApi(
        api: FirestoreUsersApi
    ): UsersApi

    @Binds
    abstract fun bindMyMoviesListApi(
        api: FirestoreMyMoviesListApi
    ): MyMoviesListApi
}

@InstallIn(SingletonComponent::class)
@Module
class RetrofitApiModule {

    @Provides
    fun provideMoviesApi(
        retrofit: Retrofit.Builder,
        @Named("movies_api_okhttp_client_builder") okHttpClient: OkHttpClient
    ): MoviesApi {
        return retrofit
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .build()
            .create(MoviesApi::class.java)
    }
}
