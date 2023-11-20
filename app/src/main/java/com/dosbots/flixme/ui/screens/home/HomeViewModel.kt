package com.dosbots.flixme.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.dosbots.flixme.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    val state = moviesRepository.getPopularMovies()
        .map { item ->
            item.map { movie ->
                HomeScreenMovie(movie.id, movie.title, movie.backdropPath ?: movie.posterPath)
            }
        }
}