package com.dosbots.flixme.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dosbots.flixme.ui.navigation.NavigationDestination
import com.dosbots.flixme.ui.theme.FlixmeUi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FlixmeActivity : ComponentActivity() {

    @Inject
    lateinit var destinations: Set<@JvmSuppressWildcards NavigationDestination>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlixmeUi {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    FlixmeApp(
                        destinations = destinations,
                        navHostController = navController
                    )
                }
            }
        }
    }
}
