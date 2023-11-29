package com.dosbots.flixme.ui.screens.createlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dosbots.flixme.R
import com.dosbots.flixme.commons.addOrRemove
import com.dosbots.flixme.commons.or
import com.dosbots.flixme.commons.replaceWhen
import com.dosbots.flixme.data.EntityIdGenerator
import com.dosbots.flixme.data.authentication.AuthenticationMethod
import com.dosbots.flixme.data.models.MyMoviesList
import com.dosbots.flixme.data.models.MyMoviesListItem
import com.dosbots.flixme.data.repository.MoviesRepository
import com.dosbots.flixme.data.repository.MyMoviesListsRepository
import com.dosbots.flixme.ui.communication.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SET_LIST_TITLE_STEP_KEY = "CreateListViewModel.SET_LIST_TITLE_STEP_ID"
const val ADD_MOVIES_STEP_KEY = "CreateListViewModel.ADD_MOVIES_STEP_ID"

private const val CREATE_LIST_STEPS_COUNT = 2

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val listsRepository: MyMoviesListsRepository,
    private val moviesRepository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val authenticationMethod: AuthenticationMethod
) : ViewModel() {

    private val _state = MutableStateFlow(
        CreateListState(
            stepsCount = CREATE_LIST_STEPS_COUNT,
            currentStep = getInitialStep()
        )
    )
    val state: StateFlow<CreateListState> = _state.asStateFlow()

    fun setListTitle(title: String) {
        if (title.isEmpty()) {
            _state.update {
                val currentStep = it.currentStep as CreateListScreenStep.SetListTitleStep
                it.copy(
                    currentStep = currentStep.copy(
                        errorMessage = UiMessage(R.string.create_list_screen_set_title_step_title_empty_error_message)
                    )
                )
            }
            return
        }
        _state.update { currentState ->
            val currentStep = currentState.currentStep as CreateListScreenStep.SetListTitleStep
            saveCurrentStep(currentStep.copy(currentTitle = title))
            currentState.copy(
                currentStep = getNextStepIfAlreadyCompleted(
                    currentStepIndex = currentStep.stepIndex,
                    fallbackStep = CreateListScreenStep.AddMoviesStep()
                )
            )
        }
    }

    fun onAddOrRemoveMovieToTheList(movie: ListMovie) {
        _state.update { currentState ->
            val currentStep = currentState.currentStep as CreateListScreenStep.AddMoviesStep

            val moviesListMutable = ArrayList(currentStep.addedMovies)
            moviesListMutable.addOrRemove(movie)

            val mutableSearchResults = ArrayList(currentStep.searchResults)
            mutableSearchResults.replaceWhen(
                replacement = SearchResult(
                    movie = movie,
                    alreadyAdded = true
                )
            ) { searchResult ->
                searchResult.movie.id == movie.id
            }

            currentState.copy(
                currentStep = currentStep.copy(
                    addedMovies = moviesListMutable,
                    searchResults = mutableSearchResults
                )
            )
        }
    }

    fun finishAddMoviesStep() {
        saveCurrentStep(_state.value.currentStep)
        viewModelScope.launch {
            val completedSteps = getCompletedSteps()
            val setTitleStep = completedSteps.find { it.id == SET_LIST_TITLE_STEP_KEY } as CreateListScreenStep.SetListTitleStep
            val addMoviesStep = completedSteps.find { it.id == ADD_MOVIES_STEP_KEY } as CreateListScreenStep.AddMoviesStep

            val listId = EntityIdGenerator.newId()
            listsRepository.createListWithMovies(
                list = MyMoviesList(
                    id = listId,
                    title = setTitleStep.currentTitle,
                    createdAt = System.currentTimeMillis(),
                    editedAt = System.currentTimeMillis(),
                    owners = listOf(authenticationMethod.userId)
                ),
                items = addMoviesStep.addedMovies.map {
                    MyMoviesListItem(
                        listId = listId,
                        movieId = it.id,
                        watched = false,
                        addedAt = System.currentTimeMillis()
                    )
                }
            )
        }
    }

    fun searchMovie(query: String) {
        val errorHandler = CoroutineExceptionHandler { _, _ ->
            _state.update { currentState ->
                val currentStep = currentState.currentStep as CreateListScreenStep.AddMoviesStep
                currentState.copy(
                    currentStep = currentStep.copy(
                        searchError = UiMessage(R.string.generic_error)
                    )
                )
            }
        }
        viewModelScope.launch(errorHandler) {
            val moviesSearchResult = moviesRepository.searchMovieByTitleTopCoincidences(query)
            _state.update { currentState ->
                val currentStep = currentState.currentStep as CreateListScreenStep.AddMoviesStep
                currentState.copy(
                    currentStep = currentStep.copy(
                        searchTerm = query,
                        searchResults = moviesSearchResult.map { movie ->
                            val listMovie = ListMovie(
                                id = movie.id,
                                image = moviesRepository.getMovieFullImagePath(movie.posterPath.orEmpty()),
                                title = movie.title,
                                overview = movie.overview
                            )
                            SearchResult(
                                movie = listMovie,
                                alreadyAdded = currentStep.addedMovies.contains(listMovie)
                            )
                        }
                    )
                )
            }
        }
    }

    fun clearErrorMessage() {
        _state.update {
            val newStepState = when (it.currentStep) {
                is CreateListScreenStep.SetListTitleStep -> {
                    it.currentStep.copy(errorMessage = null)
                }
                is CreateListScreenStep.AddMoviesStep -> {
                    it.currentStep.copy(searchError = null)
                }
            }
            it.copy(currentStep = newStepState)
        }
    }

    fun handleBackButtonTap() {
        _state.update {
            if (it.currentStep.stepIndex == 0) {
                it.copy(event = Event.NavigateToPreviousScreen)
            } else {
                saveCurrentStep(it.currentStep)
                val previousStepIndex = it.currentStep.stepIndex.minus(1)
                it.copy(
                    currentStep = getCompletedSteps().find { step -> step.stepIndex == previousStepIndex }!!
                )
            }
        }
    }

    private fun saveCurrentStep(currentStep: CreateListScreenStep) {
        savedStateHandle[currentStep.id] = currentStep
    }

    private fun getNextStepIfAlreadyCompleted(
        currentStepIndex: Int,
        fallbackStep: CreateListScreenStep
    ): CreateListScreenStep {
        return getCompletedSteps()
            .find { it.stepIndex == currentStepIndex.plus(1) }
            .or { fallbackStep }
    }

    private fun getInitialStep(): CreateListScreenStep {
        return getCompletedSteps()
            .maxByOrNull { it.stepIndex }
            .or { CreateListScreenStep.SetListTitleStep() }
    }

    private fun getCompletedSteps(): List<CreateListScreenStep> {
        return CreateListScreenStep.getAllStepsIds()
            .mapNotNull { stepId -> savedStateHandle[stepId] }
    }
}
