package com.dosbots.flixme.ui.screens.createlist

import android.os.Parcelable
import androidx.paging.PagingData
import com.dosbots.flixme.data.models.Movie
import com.dosbots.flixme.ui.communication.UiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

data class CreateListState(
    val stepsCount: Int = 0,
    val currentStep: CreateListScreenStep = CreateListScreenStep.SetListTitleStep(),
    val event: Event? = null
)

sealed class CreateListScreenStep(
    val id: String,
    val stepIndex: Int
) : Parcelable {

    companion object {
        fun getAllStepsIds() = listOf(SET_LIST_TITLE_STEP_KEY, ADD_MOVIES_STEP_KEY)
    }

    @Parcelize
    data class SetListTitleStep(
        val currentTitle: String = "",
        val errorMessage: UiMessage? = null,
    ) : CreateListScreenStep(id = SET_LIST_TITLE_STEP_KEY, stepIndex = 0)

    @Parcelize
    data class AddMoviesStep(
        val searchTerm: String = "",
        val searchResults: List<SearchResult> = emptyList(),
        val searchError: UiMessage? = null,
        val addedMovies: List<ListMovie> = emptyList(),
    ) : CreateListScreenStep(id = ADD_MOVIES_STEP_KEY, stepIndex = 1)
}

@Parcelize
data class ListMovie(
    val id: Int,
    val image: String,
    val title: String,
    val overview: String
) : Parcelable

@Parcelize
data class SearchResult(
    val movie: ListMovie,
    val alreadyAdded: Boolean
) : Parcelable

sealed class Event {
    data object NavigateToPreviousScreen : Event()
}