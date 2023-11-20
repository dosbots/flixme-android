package com.dosbots.flixme.ui.screens.home

data class HomeScreenState(
    val movies: List<HomeScreenMovie> = emptyList()
)

data class HomeScreenMovie(
    val id: Int,
    val title: String
)