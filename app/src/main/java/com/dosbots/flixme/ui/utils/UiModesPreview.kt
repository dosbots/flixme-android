package com.dosbots.flixme.ui.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(name = "Light mode preview", showBackground = true)
@Preview(name = "Dark mode preview", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightAndDarkModePreview
