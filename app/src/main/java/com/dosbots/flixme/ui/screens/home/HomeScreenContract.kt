package com.dosbots.flixme.ui.screens.home

import androidx.paging.PagingData

data class HomeScreenState(
    val popularMovies: PagingData<HomeScreenMovie> = PagingData.empty()
)

data class HomeScreenMovie(
    val id: Int,
    val title: String,
    val imageUrl: String
)