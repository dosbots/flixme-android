package com.dosbots.flixme.data.models.database

sealed class PredefinedMoviesList(val name: String) {
    data object PopularMovies : PredefinedMoviesList("popular")
    data object TopRatedMovies : PredefinedMoviesList("top-rated")
}