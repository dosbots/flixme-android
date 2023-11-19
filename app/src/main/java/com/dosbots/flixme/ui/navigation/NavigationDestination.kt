package com.dosbots.flixme.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

abstract class NavigationDestination {

    open val initial: Boolean = false

    abstract val route: String

    abstract fun registerDestination(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    )
}
