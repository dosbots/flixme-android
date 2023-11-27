package com.dosbots.flixme.ui.screens.createlist

import android.os.Parcelable
import com.dosbots.flixme.ui.communication.UiMessage
import kotlinx.parcelize.Parcelize

data class CreateListState(
    val stepsCount: Int = 0,
    val currentStep: CreateListScreenStep = CreateListScreenStep.SetListTitleStep(),
    val errorMessage: UiMessage? = null,
    val event: Event? = null
)

sealed class CreateListScreenStep(
    val id: String,
    val stepIndex: Int
) : Parcelable {
    companion object {
        fun getStepsIds() = listOf(SET_LIST_TITLE_STEP_KEY, ADD_MOVIES_STEP_KEY)
    }

    @Parcelize
    data class SetListTitleStep(
        val currentTitle: String = ""
    ) : CreateListScreenStep(id = SET_LIST_TITLE_STEP_KEY, stepIndex = 0)

    @Parcelize
    data class AddMoviesStep(
        val addedMovies: List<ListMovie> = emptyList()
    ) : CreateListScreenStep(id = ADD_MOVIES_STEP_KEY, stepIndex = 1)
}

@Parcelize
data class ListMovie(
    val image: String,
    val title: String
) : Parcelable

data class ListMovieDetail(
    val image: String,
    val title: String,
    val overview: String
)

sealed class Event {
    data object NavigateToPreviousScreen : Event()
}