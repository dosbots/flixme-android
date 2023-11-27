package com.dosbots.flixme.ui.screens.createlist.steps

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.screens.createlist.CreateListScreenStep
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun ColumnScope.AddMoviesStep(
    state: CreateListScreenStep.AddMoviesStep,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.xlg)
    )
    Text(
        text = stringResource(id = R.string.create_list_screen_add_movies_step_title),
        style = FlixmeUi.typography.displaySmall
    )
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.sm)
    )
    Text(
        text = stringResource(id = R.string.create_list_screen_add_movies_step_subtitle),
        style = FlixmeUi.typography.titleSmall
    )
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.lg)
    )
}