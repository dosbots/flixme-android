package com.dosbots.flixme.ui.screens.createlist.steps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.compose.FlixmeTextField
import com.dosbots.flixme.ui.compose.ShimmerAsyncImage
import com.dosbots.flixme.ui.screens.createlist.CreateListScreenStep
import com.dosbots.flixme.ui.screens.createlist.ListMovie
import com.dosbots.flixme.ui.theme.FlixmeUi
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.AddMoviesStep(
    state: CreateListScreenStep.AddMoviesStep,
    navigateToNextStep: () -> Unit,
    onSearchMovie: (String) -> Unit
) {
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.xlg)
    )
    Text(
        text = stringResource(id = R.string.create_list_screen_add_movies_step_title),
        style = FlixmeUi.typography.titleMedium
    )
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.sm)
    )

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        SearchMoviesBottomSheet(
            searchResults = state.searchResults,
            onDismissRequest = {
                showBottomSheet = false
            },
            onSearchMovie = onSearchMovie
        )
    }
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
        style = FlixmeUi.typography.bodySmall,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
    )
    Spacer(
        modifier = Modifier.weight(1f)
    )
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
    searchResults: List<ListMovie>,
    onDismissRequest: () -> Unit,
    onSearchMovie: (String) -> Unit
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
            onSearchMovie = onSearchMovie
        )
        SearchResultsList(
            searchResults = searchResults
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchMovie: (String) -> Unit
) {
    val searchInputFocusRequester = FocusRequester()
    var query by rememberSaveable { mutableStateOf("") }
    FlixmeTextField(
        value = query,
        onValueChanged = { text ->
            query = text
            onSearchMovie(text)
        },
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
    searchResults: List<ListMovie>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FlixmeUi.dimens.md)
    ) {
        items(searchResults) { movie ->
            SearchResult(
                movie = movie
            )
        }
    }
}

@Composable
private fun SearchResult(
    movie: ListMovie,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FlixmeUi.colorScheme.surface),
        modifier = modifier
    ) {
        Row {
            ShimmerAsyncImage(
                imageUrl = movie.image,
                shimmerHeight = 150.dp,
                shimmerWidth = 100.dp
            )
            Text(
                text = movie.title,
                style = FlixmeUi.typography.titleMedium
            )
        }
    }
}

private val imageWidth = 100.dp