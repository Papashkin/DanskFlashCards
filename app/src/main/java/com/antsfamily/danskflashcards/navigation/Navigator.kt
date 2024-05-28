package com.antsfamily.danskflashcards.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.antsfamily.danskflashcards.ui.game.GameScreen
import com.antsfamily.danskflashcards.ui.auth.AuthScreen
import com.antsfamily.danskflashcards.ui.home.HomeScreen
import com.antsfamily.danskflashcards.ui.splash.SplashScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {

        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            print(it.toString())
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(navController)
                }
                composable(Screen.Auth.route) {
                    AuthScreen(navController)
                }
                composable(
                    Screen.Home.route,
                    arguments = listOf(
                        navArgument("username") { type = NavType.StringType },
                        navArgument("userId") { type = NavType.StringType },
                    )
                ) { entry ->
                    BackHandler(true) {
                        //no-op
                    }
                    HomeScreen(
                        navController = navController,
                        username = entry.arguments?.getString("username").orEmpty(),
                        userId = entry.arguments?.getString("userId").orEmpty(),
                    )
                }
                composable(Screen.Game.route) {
                    GameScreen(navController)
                }
            }
        }
    )
}

fun NavOptionsBuilder.popUpToTop(navController: NavController) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}
