package com.dosbots.flixme.ui.screens.createlist.steps

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.communication.UiMessage
import com.dosbots.flixme.ui.compose.FlixmeTextField
import com.dosbots.flixme.ui.compose.ShimmerAsyncImage
import com.dosbots.flixme.ui.screens.createlist.CreateListScreenStep
import com.dosbots.flixme.ui.screens.createlist.ListMovie
import com.dosbots.flixme.ui.screens.createlist.SearchResult
import com.dosbots.flixme.ui.theme.FlixmeUi
import com.dosbots.flixme.ui.theme.Gray100
import com.dosbots.flixme.ui.utils.LightAndDarkModePreview
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.AddMoviesStep(
    state: CreateListScreenStep.AddMoviesStep,
    navigateToNextStep: () -> Unit,
    onSearchMovie: (String) -> Unit,
    onMovieAddedOrRemoved: (ListMovie) -> Unit,
    onErrorMessageShown: () -> Unit
) {
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.xlg)
    )

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        SearchMoviesBottomSheet(
            searchTerm = state.searchTerm,
            searchResults = state.searchResults,
            searchError = state.searchError,
            onDismissRequest = {
                showBottomSheet = false
            },
            onSearchMovie = onSearchMovie,
            onMovieAddedOrRemoved = onMovieAddedOrRemoved,
            onErrorMessageShown = onErrorMessageShown
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.md),
        modifier = Modifier
            .weight(1f)
            .padding(bottom = FlixmeUi.dimens.md)
    ) {
        item(key = "add-movies-step-header") {
            Text(
                text = stringResource(id = R.string.create_list_screen_add_movies_step_title),
                style = FlixmeUi.typography.titleMedium
            )
            Spacer(
                modifier = Modifier.height(FlixmeUi.dimens.sm)
            )
            OutlinedButton(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(
                        id = R.string.create_list_screen_add_movies_step_search_movies_icon_content_description
                    )
                )
                Text(
                    text = stringResource(id = R.string.create_list_screen_add_movies_step_search_movies),
                    style = FlixmeUi.typography.bodyMedium
                )
            }
            Spacer(
                modifier = Modifier.height(FlixmeUi.dimens.xs)
            )
            Text(
                text = stringResource(id = R.string.create_list_screen_add_movies_step_subtitle),
                color = Gray100,
                style = FlixmeUi.typography.bodySmall
            )
            Spacer(
                modifier = Modifier.height(FlixmeUi.dimens.sm)
            )
        }
        items(state.addedMovies) { movie ->
            ListMovieItem(
                movie = movie,
                addedToTheList = true,
                onMovieAddedOrRemoved = onMovieAddedOrRemoved
            )
        }
    }

    Button(
        onClick = navigateToNextStep,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.create_list_screen_add_movies_step_cta),
            style = FlixmeUi.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchMoviesBottomSheet(
    modifier: Modifier = Modifier,
    searchError: UiMessage? = null,
    searchTerm: String,
    searchResults: List<SearchResult>,
    onDismissRequest: () -> Unit,
    onSearchMovie: (String) -> Unit,
    onMovieAddedOrRemoved: (ListMovie) -> Unit,
    onErrorMessageShown: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier.fillMaxSize()
    ) {
        val coroutineScope = rememberCoroutineScope()
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = FlixmeUi.dimens.md)
                .clickable {
                    coroutineScope
                        .launch {
                            sheetState.hide()
                        }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                }
        )
        SearchBar(
            searchTerm = searchTerm,
            onSearchMovie = onSearchMovie
        )
        SearchResultsList(
            searchResults = searchResults,
            onMovieAddedOrRemoved = onMovieAddedOrRemoved
        )

        val context = LocalContext.current
        searchError?.let { error ->
            LaunchedEffect(error) {
                Toast.makeText(
                    context,
                    error.get(context),
                    Toast.LENGTH_SHORT
                ).show()
                onErrorMessageShown()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    searchTerm: String,
    onSearchMovie: (String) -> Unit
) {
    val searchInputFocusRequester = FocusRequester()
    var query by remember { mutableStateOf(searchTerm) }
    FlixmeTextField(
        value = query,
        onValueChanged = { text ->
            query = text
            onSearchMovie(text)
        },
        singleLine = true,
        label = stringResource(id = R.string.create_list_screen_add_movies_step_search_input_label),
        placeholder = stringResource(id = R.string.create_list_screen_add_movies_step_search_input_placeholder),
        modifier = modifier
            .fillMaxWidth()
            .padding(FlixmeUi.dimens.md)
            .focusRequester(searchInputFocusRequester)
    )

    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        searchInputFocusRequester.requestFocus()
        keyboardController?.show()
    }
}

@Composable
private fun SearchResultsList(
    modifier: Modifier = Modifier,
    searchResults: List<SearchResult>,
    onMovieAddedOrRemoved: (ListMovie) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(bottom = FlixmeUi.dimens.md),
        verticalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.md)
    ) {
        items(searchResults) { searchResult ->
            ListMovieItem(
                movie = searchResult.movie,
                addedToTheList = searchResult.alreadyAdded,
                onMovieAddedOrRemoved = onMovieAddedOrRemoved,
                modifier = Modifier.padding(horizontal = FlixmeUi.dimens.md)
            )
        }
    }
}

@Composable
private fun ListMovieItem(
    modifier: Modifier = Modifier,
    movie: ListMovie,
    addedToTheList: Boolean,
    onMovieAddedOrRemoved: (ListMovie) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        ShimmerAsyncImage(
            imageUrl = movie.image,
            imageHeight = 180.dp,
            imageWidth = 120.dp,
            shimmerHeight = 180.dp,
            shimmerWidth = 120.dp
        )
        Spacer(modifier = Modifier.width(FlixmeUi.dimens.md))
        Column(
            verticalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.sm)
        ) {
            Text(
                text = movie.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = FlixmeUi.typography.titleSmall
            )
            Text(
                text = movie.overview,
                style = FlixmeUi.typography.bodyLarge,
                minLines = 4,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            if (addedToTheList) {
                OutlinedButton(
                    onClick = {
                        onMovieAddedOrRemoved(movie)
                    },
                    modifier = Modifier.widthIn(min = 120.dp)
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.create_list_screen_add_movies_step_search_movies_list_remove_movie_cta
                        ),
                        style = FlixmeUi.typography.bodyMedium
                    )
                }
            } else {
                FilledTonalButton(
                    onClick = {
                        onMovieAddedOrRemoved(movie)
                    },
                    modifier = Modifier.widthIn(min = 120.dp)
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.create_list_screen_add_movies_step_search_movies_list_add_movie_cta
                        ),
                        style = FlixmeUi.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@LightAndDarkModePreview
@Composable
private fun AddMoviesStepPreview() {
    FlixmeUi {
        Surface(
            color = FlixmeUi.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = FlixmeUi.dimens.lg, vertical = FlixmeUi.dimens.md)
            ) {
                AddMoviesStep(
                    state = CreateListScreenStep.AddMoviesStep(
                        addedMovies = listOf(
                            ListMovie(
                                id = 1,
                                title = "The godfather",
                                overview = "The godfather overview",
                                image = ""
                            )
                        )
                    ),
                    navigateToNextStep = {},
                    onSearchMovie = { _ -> },
                    onMovieAddedOrRemoved = { _ -> },
                    onErrorMessageShown = {}
                )
            }
        }
    }
}