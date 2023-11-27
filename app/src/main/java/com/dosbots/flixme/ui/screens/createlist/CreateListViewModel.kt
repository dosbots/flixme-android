package com.dosbots.flixme.ui.screens.createlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dosbots.flixme.R
import com.dosbots.flixme.commons.or
import com.dosbots.flixme.data.repository.MoviesRepository
import com.dosbots.flixme.data.repository.MyMoviesListsRepository
import com.dosbots.flixme.ui.communication.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

const val SET_LIST_TITLE_STEP_KEY = "CreateListViewModel.SET_LIST_TITLE_STEP_ID"
const val ADD_MOVIES_STEP_KEY = "CreateListViewModel.ADD_MOVIES_STEP_ID"

private const val CREATE_LIST_STEPS_COUNT = 2

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val listsRepository: MyMoviesListsRepository,
    private val moviesRepository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
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
                it.copy(
                    errorMessage = UiMessage(
                        R.string.create_list_screen_set_title_step_title_empty_error_message
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

    fun onAddMovieToTheList(movie: ListMovie) {

    }

    fun finishAddMoviesStep() {

    }

    fun searchMovie(query: String) {

    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
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
