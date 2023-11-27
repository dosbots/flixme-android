package com.dosbots.flixme.ui.screens.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dosbots.flixme.data.authentication.AuthenticationStatus
import com.dosbots.flixme.ui.navigation.NavigationDestination
import com.dosbots.flixme.ui.navigation.popBackStack
import com.dosbots.flixme.ui.screens.home.HomeScreenDestination
import com.dosbots.flixme.ui.screens.userdisabled.UserDisabledDestination
import javax.inject.Inject

class DeveloperLoginDestination @Inject constructor(
    private val authenticationStatus: AuthenticationStatus
) : NavigationDestination() {

    override val initial: Boolean
        get() = !authenticationStatus.isUserLogged

    override val route: String = ROUTE

    override fun registerDestination(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.composable(ROUTE) {
            DeveloperLoginScreen(
                navigateToUserDisabledScreen = {
                    navHostController.navigate(UserDisabledDestination.ROUTE) {
                        popBackStack(it)
                    }
                },
                navigateToHomeScreen = {
                    navHostController.navigate(HomeScreenDestination.ROUTE) {
                        popBackStack(it)
                    }
                }
            )
        }
    }

    companion object {
        const val ROUTE = "developer-login"
    }
}