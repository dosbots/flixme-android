package com.dosbots.flixme.ui.screens.userdisabled

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.dosbots.flixme.ui.navigation.NavigationDestination
import javax.inject.Inject

class UserDisabledDestination @Inject constructor() : NavigationDestination() {

    override val route: String = ROUTE

    override fun registerDestination(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.composable(ROUTE) {
            UserDisabledScreen()
        }
    }

    companion object {
        const val ROUTE = "user-disabled"
    }
}