package com.dosbots.flixme.ui.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dosbots.flixme.ui.theme.FlixmeUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlixmeTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String? = null,
    label: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        placeholder = {
            placeHolder?.let { text ->
                Text(
                    text = text,
                    color = Color.Gray,
                    style = FlixmeUi.typography.bodyMedium
                )
            }
        },
        label = {
            label?.let { text ->
                Text(
                    text = text,
                    style = FlixmeUi.typography.bodyMedium
                )
            }
        },
        supportingText = {
            supportingText?.let { text ->
                Text(
                    text = text,
                    color = FlixmeUi.colorScheme.error,
                    style = FlixmeUi.typography.labelLarge
                )
            }
        },
        onValueChange = onValueChanged,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        singleLine = singleLine,
        modifier = modifier
    )
}