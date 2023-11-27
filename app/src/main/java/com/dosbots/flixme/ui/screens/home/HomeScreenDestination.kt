package com.dosbots.flixme.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dosbots.flixme.data.authentication.AuthenticationStatus
import com.dosbots.flixme.ui.navigation.NavigationDestination
import com.dosbots.flixme.ui.screens.createlist.CreateListDestination
import javax.inject.Inject

class HomeScreenDestination @Inject constructor(
    private val authenticationStatus: AuthenticationStatus
) : NavigationDestination() {

    override val route: String = ROUTE

    override val initial: Boolean
        get() = authenticationStatus.isUserLogged

    override fun registerDestination(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.composable(ROUTE) {
            HomeScreen(
                onCreateListClick = {
                    navHostController.navigate(CreateListDestination.ROUTE)
                }
            )
        }
    }

    companion object {
        const val ROUTE = "home"
    }
}
