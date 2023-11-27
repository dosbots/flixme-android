package com.dosbots.flixme.ui.screens.createlist.steps

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.communication.UiMessage
import com.dosbots.flixme.ui.compose.FlixmeTextField
import com.dosbots.flixme.ui.screens.createlist.CreateListScreenStep
import com.dosbots.flixme.ui.screens.createlist.CreateListState
import com.dosbots.flixme.ui.theme.FlixmeUi

@Composable
fun ColumnScope.SetListTitleStep(
    stepState: CreateListScreenStep.SetListTitleStep,
    stepErrorMessage: UiMessage? = null,
    onListTitleSet: (String) -> Unit,
    onErrorMessageShown: () -> Unit
) {
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.xlg)
    )
    Text(
        text = stringResource(id = R.string.create_list_screen_set_title_step_title),
        style = FlixmeUi.typography.displaySmall
    )
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.sm)
    )
    Text(
        text = stringResource(id = R.string.create_list_screen_set_title_step_subtitle),
        style = FlixmeUi.typography.titleSmall
    )
    Spacer(
        modifier = Modifier.height(FlixmeUi.dimens.lg)
    )

    var listTitle by rememberSaveable { mutableStateOf(stepState.currentTitle) }
    FlixmeTextField(
        value = listTitle,
        onValueChanged = { listTitle = it },
        singleLine = true,
        placeHolder = stringResource(id = R.string.create_list_screen_set_title_step_input_placeholder),
        label = stringResource(id = R.string.create_list_screen_set_title_step_input_label),
        supportingText = stepErrorMessage?.get(LocalContext.current),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(
        modifier = Modifier.weight(1f)
    )
    Button(
        onClick = {
            stepErrorMessage?.let { onErrorMessageShown() }
            onListTitleSet(listTitle)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                id = R.string.create_list_screen_set_title_step_cta
            ),
            style = FlixmeUi.typography.bodyMedium
        )
    }
}