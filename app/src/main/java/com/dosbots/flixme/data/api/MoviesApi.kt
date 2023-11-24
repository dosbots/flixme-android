package com.dosbots.flixme.data.api

import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.api.PaginatedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<PaginatedResponse<Movie>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): Response<PaginatedResponse<Movie>>
}
