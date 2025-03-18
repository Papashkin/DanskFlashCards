package com.antsfamily.danskflashcards.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.antsfamily.danskflashcards.core.util.toModel
import com.antsfamily.danskflashcards.ui.about.AboutScreen
import com.antsfamily.danskflashcards.ui.auth.AuthScreen
import com.antsfamily.danskflashcards.ui.game.GameScreen
import com.antsfamily.danskflashcards.ui.home.HomeScreen
import com.antsfamily.danskflashcards.ui.splash.SplashScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            print(it.toString())
            NavHost(
                navController = navController,
                startDestination = Splash
            ) {
                composable<Splash> {
                    SplashScreen {
                        navController.navigate(Auth) { popUpToTop(navController) }
                    }
                }
                composable<Auth> {
                    AuthScreen { user ->
                        navController.navigate(
                            Home(id = user.userId, name = user.username, email = user.email)
                        )
                    }
                }
                composable<Home>(
                    enterTransition = { scaleIn(tween(300)) },
                    exitTransition = { scaleOut(tween(300)) },
                ) { entry ->
                    BackHandler(true) {
                        //no-op
                    }
                    val data = entry.toRoute<Home>()
                    HomeScreen(
                        user = data.toModel(),
                        navigateBack = {
                            navController.popBackStack()
                        },
                        navigateToAbout = { navController.navigate(About) },
                        navigateToGame = { score ->
                            navController.navigate(
                                Game(id = data.id, name = data.name, score = score)
                            )
                        }
                    )
                }
                composable<About>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up, tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down, tween(300)
                        )
                    },
                ) {
                    AboutScreen { navController.popBackStack() }
                }
                composable<Game>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up, tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down, tween(300)
                        )
                    },
                ) { entry ->
                    val data = entry.toRoute<Game>()
                    GameScreen(
                        userId = data.id,
                        username = data.name,
                        score = data.score
                    ) {
                        navController.popBackStack()
                    }
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
