package com.dosbots.flixme.ui.screens.createlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dosbots.flixme.ui.screens.createlist.steps.AddMoviesStep
import com.dosbots.flixme.ui.screens.createlist.steps.SetListTitleStep
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun CreateListScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateListViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreateListScreen(
        modifier = modifier,
        state = state,
        onListTitleSet = { viewModel.setListTitle(it) },
        onAddMoviesStepFinished = { viewModel.finishAddMoviesStep() },
        onSearchMovie = { query -> viewModel.searchMovie(query) },
        onMovieAddedOrRemoved = { movie -> viewModel.onAddOrRemoveMovieToTheList(movie) },
        onErrorMessageShown = { viewModel.clearErrorMessage() },
        handleBackTap = { viewModel.handleBackButtonTap() },
        navigateToPreviousScreen = navigateBack
    )
}

@Composable
private fun CreateListScreen(
    modifier: Modifier = Modifier,
    state: CreateListState,
    onListTitleSet: (String) -> Unit,
    onAddMoviesStepFinished: () -> Unit,
    onSearchMovie: (String) -> Unit,
    onMovieAddedOrRemoved: (ListMovie) -> Unit,
    onErrorMessageShown: () -> Unit,
    handleBackTap: () -> Unit,
    navigateToPreviousScreen: () -> Unit
) {
    when (state.event) {
        Event.NavigateToPreviousScreen -> {
            LaunchedEffect(Unit) {
                navigateToPreviousScreen()
            }
        }
        else -> {
            // no-op
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = FlixmeUi.dimens.lg, vertical = FlixmeUi.dimens.md)
    ) {
        CreateListTopAppBar(
            currentStepIndex = state.currentStep.stepIndex,
            handleBackTap = handleBackTap
        )
        StepIndicator(
            stepsCount = state.stepsCount,
            currentStep = state.currentStep
        )
        when(state.currentStep) {
            is CreateListScreenStep.SetListTitleStep -> {
                SetListTitleStep(
                    stepState = state.currentStep,
                    onListTitleSet = onListTitleSet,
                    onErrorMessageShown = onErrorMessageShown
                )
            }
            is CreateListScreenStep.AddMoviesStep -> {
                AddMoviesStep(
                    state = state.currentStep,
                    navigateToNextStep = onAddMoviesStepFinished,
                    onSearchMovie = onSearchMovie,
                    onMovieAddedOrRemoved = onMovieAddedOrRemoved,
                    onErrorMessageShown = onErrorMessageShown
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateListTopAppBar(
    modifier: Modifier = Modifier,
    currentStepIndex: Int,
    handleBackTap: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            Icon(
                imageVector = if (currentStepIndex == 0) {
                    Icons.Rounded.Close
                } else {
                    Icons.Rounded.ArrowBack
                },
                contentDescription = "close screen",
                modifier = Modifier.clickable { handleBackTap() }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FlixmeUi.colorScheme.background
        ),
        modifier = modifier
    )
}

@Composable
private fun StepIndicator(
    stepsCount: Int,
    currentStep: CreateListScreenStep,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.md),
        modifier = modifier.fillMaxWidth()
    ) {
        for (stepIndex in 0 until stepsCount) {
            val indicatorColor = if (currentStep.stepIndex == stepIndex) {
                FlixmeUi.colorScheme.primary
            } else {
                FlixmeUi.colorScheme.surface
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(FlixmeUi.dimens.sm)
                    .background(
                        color = indicatorColor,
                        shape = RoundedCornerShape(FlixmeUi.dimens.sm)
                    )
            )
        }
    }
}
