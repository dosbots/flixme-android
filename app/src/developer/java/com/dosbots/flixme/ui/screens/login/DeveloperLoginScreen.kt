package com.dosbots.flixme.ui.screens.login

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dosbots.flixme.BUSINESS_EMAIL
import com.dosbots.flixme.R
import com.dosbots.flixme.ui.theme.FlixmeUi
import com.dosbots.flixme.ui.theme.LocalDimens
import com.dosbots.flixme.ui.utils.LightAndDarkModePreview
import com.dosbots.flixme.ui.utils.sendEmail
import kotlinx.coroutines.launch

@Composable
fun DeveloperLoginScreen(
    modifier: Modifier = Modifier,
    viewModel: DeveloperLoginViewModel = hiltViewModel(),
    navigateToUserDisabledScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DeveloperLoginScreen(
        state = state,
        onSignInClick = { email, password ->
            viewModel.signIn(email, password)
        },
        onErrorMessageShown = {
            viewModel.clearErrorMessage()
        },
        onEventHandled = {
            viewModel.clearEvent()
        },
        navigateToUserDisabledScreen = navigateToUserDisabledScreen,
        navigateToHomeScreen = navigateToHomeScreen,
        modifier = modifier
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DeveloperLoginScreen(
    state: DeveloperLoginState,
    onSignInClick: (email: String, password: String) -> Unit,
    onErrorMessageShown: () -> Unit,
    onEventHandled: () -> Unit,
    navigateToUserDisabledScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    state.errorMessage?.let { message ->
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message.get(context),
                    duration = SnackbarDuration.Long
                )
                onErrorMessageShown()
            }
        }
    }

    val context = LocalContext.current

    val emailFocusRequester by remember { mutableStateOf(FocusRequester()) }
    val passwordFocusRequester by remember { mutableStateOf(FocusRequester()) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { _ ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .scrollable(rememberScrollState(), Orientation.Vertical)
                .padding(
                    horizontal = FlixmeUi.dimens.lg,
                    vertical = FlixmeUi.dimens.md
                )
        ) {
            Spacer(modifier = Modifier.height(FlixmeUi.dimens.lg))
            Text(
                text = stringResource(id = R.string.login_screen_welcome_title),
                style = FlixmeUi.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(FlixmeUi.dimens.sm))
            Text(
                text = buildAnnotatedString {
                    append(
                        text = stringResource(id = R.string.login_screen_welcome_message)
                    )
                    append(" ")
                    withStyle(style = SpanStyle(color = FlixmeUi.colorScheme.primary)) {
                        append(text = BUSINESS_EMAIL)
                    }
                },
                style = FlixmeUi.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { context.sendEmail(BUSINESS_EMAIL) }
            )

            Spacer(modifier = Modifier.height(FlixmeUi.dimens.xlg))

            var email by remember { mutableStateOf("") }
            OutlinedTextField(
                value = email,
                placeholder = {
                    Text(
                        text = "myaccount@flixme.com",
                        color = Color.Gray,
                        style = FlixmeUi.typography.bodyMedium
                    )
                },
                label = {
                    Text(
                        text = stringResource(
                            id = R.string.login_screen_email_placeholder
                        ),
                        style = FlixmeUi.typography.bodyMedium
                    )
                },
                onValueChange = { newValue ->
                    email = newValue
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions { passwordFocusRequester.requestFocus() },
                enabled = !state.loading,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester)
            )

            Spacer(modifier = Modifier.height(FlixmeUi.dimens.md))

            var password by remember { mutableStateOf("") }
            OutlinedTextField(
                value = password,
                placeholder = {
                    Text(
                        text = "myawesomepassword",
                        color = Color.Gray,
                        style = FlixmeUi.typography.bodyMedium
                    )
                },
                label = {
                    Text(
                        text = stringResource(
                            id = R.string.login_screen_password_placeholder
                        ),
                        style = FlixmeUi.typography.bodyMedium
                    )
                },
                onValueChange = { newValue ->
                    password = newValue
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions { onSignInClick(email, password) },
                enabled = !state.loading,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester)
            )

            Spacer(modifier = Modifier.height(FlixmeUi.dimens.xlg))

            Button(
                enabled = !state.loading,
                onClick = {
                    onSignInClick(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonDefaults.MinHeight)
            ) {
                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        strokeWidth = progressBarStrokeWidth
                    )
                } else {
                    Text(
                        text = stringResource(
                            id = R.string.login_screen_sign_in_btn_text
                        ),
                        style = FlixmeUi.typography.bodyMedium
                    )
                }
            }
        }
    }

    when (state.event) {
        Event.OnScreenLoaded -> {
            val keyboardController = LocalSoftwareKeyboardController.current
            LaunchedEffect(Unit) {
                if (!state.loading) {
                    emailFocusRequester.requestFocus()
                    keyboardController?.show()
                    onEventHandled()
                }
            }
        }
        Event.OnSignInSuccess -> {
            LaunchedEffect(Unit) {
                navigateToHomeScreen()
                onEventHandled()
            }
        }
        Event.OnUserDisabled -> {
            LaunchedEffect(Unit) {
                navigateToUserDisabledScreen()
                onEventHandled()
            }
        }
        else -> {
            // no-op
        }
    }
}

private val progressBarStrokeWidth = 2.dp

@LightAndDarkModePreview
@Composable
private fun DeveloperLoginScreenPreview() {
    FlixmeUi {
        Surface(
            color = FlixmeUi.colorScheme.background
        ) {
            DeveloperLoginScreen(
                state = DeveloperLoginState(),
                onSignInClick = { _, _ -> },
                onEventHandled = {},
                onErrorMessageShown = {},
                navigateToUserDisabledScreen = {},
                navigateToHomeScreen = {}
            )
        }
    }
}