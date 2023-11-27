package com.dosbots.flixme.ui.screens.home

sealed class MyMoviesListState {
    data class HomeScreenMoviesList(
        val lists: List<HomeScreenListOfMovies>
    ) : MyMoviesListState()
    data object NoListCreated : MyMoviesListState()
}

data class HomeScreenListOfMovies(
    val id: String,
    val title: String,
    val urls: List<String>
)

data class HomeScreenMovie(
    val id: Int,
    val title: String,
    val imageUrl: String
)