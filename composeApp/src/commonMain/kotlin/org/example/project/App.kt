package org.example.project

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.feature.irregularverbs.GameVerbViewModel
import org.example.project.feature.irregularverbs.GameVisorRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.IrregularVerbs
        ) {

            composable<Route.IrregularVerbs>(
                exitTransition = { slideOutHorizontally() },
                popEnterTransition = { slideInHorizontally() }
            ) {
                val gameViewModel = koinViewModel<GameVerbViewModel>()
                GameVisorRoot(gameViewModel = gameViewModel)
            }
        }
    }
}
