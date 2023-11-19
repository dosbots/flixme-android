package com.dosbots.flixme.ui.screens.userdisabled

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.dosbots.flixme.BUSINESS_EMAIL
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.theme.FlixmeUi
import com.dosbots.flixme.ui.utils.LightAndDarkModePreview
import com.dosbots.flixme.ui.utils.sendEmail

@Composable
fun UserDisabledScreen(modifier: Modifier = Modifier) {
    Surface(
        color = FlixmeUi.colorScheme.background
    ) {
        val context = LocalContext.current
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(FlixmeUi.dimens.lg)
        ) {
            Text(
                text = stringResource(id = R.string.user_disabled_screen_title),
                style = FlixmeUi.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))
            Text(
                text = buildAnnotatedString {
                    append(
                        text = stringResource(id = R.string.user_disabled_screen_message)
                    )
                    append(" ")
                    withStyle(style = SpanStyle(color = FlixmeUi.colorScheme.primary)) {
                        append(text = BUSINESS_EMAIL)
                    }
                },
                style = FlixmeUi.typography.bodyMedium,
                modifier = Modifier.clickable { context.sendEmail(BUSINESS_EMAIL) }
            )
        }
    }
}

@LightAndDarkModePreview
@Composable
private fun UserDisabledScreenPreview() {
    FlixmeUi {
        Surface(
            color = FlixmeUi.colorScheme.background
        ) {
            UserDisabledScreen()
        }
    }
}