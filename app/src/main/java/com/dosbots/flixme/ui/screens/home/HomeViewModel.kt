package com.dosbots.flixme.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    val popularMoviesState = moviesRepository.getPopularMovies().mapToUiModelFlow()

    val topRatedMovies = moviesRepository.getTopRatedMovies().mapToUiModelFlow()

    private fun Flow<PagingData<Movie>>.mapToUiModelFlow(): Flow<PagingData<HomeScreenMovie>> {
        return map { item ->
            item.map { movie ->
                HomeScreenMovie(
                    id = movie.id,
                    title = movie.title,
                    imageUrl = getFullImageUrl(movie.backdropPath ?: movie.posterPath)
                )
            }
        }
    }

    private fun getFullImageUrl(resource: String): String {
        // TODO load from configuration API
        return "https://image.tmdb.org/t/p/w300/$resource"
    }
}