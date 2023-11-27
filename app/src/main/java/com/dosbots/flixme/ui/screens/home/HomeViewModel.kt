package com.dosbots.flixme.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.data.models.MyMoviesListWithMovies
import com.dosbots.flixme.data.repository.MoviesRepository
import com.dosbots.flixme.data.repository.MyMoviesListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val myMoviesListsRepository: MyMoviesListsRepository
) : ViewModel() {

    val myMoviesList: StateFlow<MyMoviesListState> = flow {
        emit(
            viewModelScope.async {
                myMoviesListsRepository.getMostRecentlyEditedLists()
            }.await()
        )
    }.mapToMoviesListUiState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MyMoviesListState.NoListCreated
        )

    val popularMoviesState = moviesRepository.getPopularMovies().mapToUiState()
    val topRatedMovies = moviesRepository.getTopRatedMovies().mapToUiState()

    private fun Flow<PagingData<Movie>>.mapToUiState(): Flow<PagingData<HomeScreenMovie>> {
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

    private fun Flow<List<MyMoviesListWithMovies>>.mapToMoviesListUiState(): Flow<MyMoviesListState> {
        return map { lists ->
            if (lists.isEmpty()) {
                MyMoviesListState.NoListCreated
            } else {
                val listsWithMovies = lists.map {
                    HomeScreenListOfMovies(
                        id = it.list.id,
                        title = it.list.title,
                        urls = it.items
                            .sortedBy { item -> item.listItem.addedAt }
                            .take(HOME_SCREEN_MY_LISTS_COUNT)
                            .map { item -> item.movie.backdropPath.orEmpty() }
                    )
                }
                MyMoviesListState.HomeScreenMoviesList(listsWithMovies)
            }
        }
    }

    private fun getFullImageUrl(resource: String): String {
        // TODO load from configuration API
        return "https://image.tmdb.org/t/p/w300/$resource"
    }
}

private const val HOME_SCREEN_MY_LISTS_COUNT = 4