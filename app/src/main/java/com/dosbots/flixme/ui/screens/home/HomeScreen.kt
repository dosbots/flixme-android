package com.dosbots.flixme.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState
) {
    Column(modifier = modifier) {
        Text(
            text = "Home screen",
            style = FlixmeUi.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
        Text(
            text = "Popular movies",
            style = FlixmeUi.typography.titleSmall
        )
        LazyRow {

        }
    }
}