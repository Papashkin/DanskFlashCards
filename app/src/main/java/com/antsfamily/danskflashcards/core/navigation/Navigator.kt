package com.antsfamily.danskflashcards.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.antsfamily.danskflashcards.core.util.toModel
import com.antsfamily.danskflashcards.ui.auth.AuthScreen
import com.antsfamily.danskflashcards.ui.game.GameScreen
import com.antsfamily.danskflashcards.ui.home.HomeScreen
import com.antsfamily.danskflashcards.ui.onboarding.OnboardingScreen
import com.antsfamily.danskflashcards.ui.onboarding2.Onboarding2Screen
import com.antsfamily.danskflashcards.ui.settings.SettingsScreen
import com.antsfamily.danskflashcards.ui.splash.SplashScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { values ->
            print(values.toString())
            NavHost(
                navController = navController,
                startDestination = Splash
            ) {
                composable<Splash> { _ ->
                    SplashScreen(
                        navigateToAuth = {
                            navController.navigate(Auth) { popUpToTop(navController) }
                        },
                        navigateToHome = {
                            navController.navigate(Home(id = it.userId, name = it.username))
                        },
                        navigateToOnboarding = {
                            navController.navigate(Onboarding1(id = it.userId, name = it.username))
                        }
                    )
                }
                composable<Auth> { _ ->
                    AuthScreen(
                        onNavigateToHome = {
                            navController.navigate(Home(id = it.userId, name = it.username))
                        },
                        onNavigateToOnboarding = {
                            navController.navigate(Onboarding1(id = it.userId, name = it.username))
                        }
                    )
                }
                composable<Onboarding1>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                        )
                    },
                ) { entry ->
                    BackHandler(true) {
                        //no-op
                    }
                    val data = entry.toRoute<Onboarding1>()
                    OnboardingScreen(user = data.toModel()) {
                        navController.navigate(Onboarding2(id = it.userId, name = it.username))
                    }
                }
                composable<Onboarding2>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left, tween(500)
                        )
                    },
                ) { entry ->
                    BackHandler(true) {
                        //no-op
                    }
                    val data = entry.toRoute<Onboarding2>()
                    Onboarding2Screen(user = data.toModel()) {
                        navController.navigate(Home(id = it.userId, name = it.username))
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
                        navigateToSettings = { navController.navigate(Settings) },
                        navigateToGame = { score ->
                            navController.navigate(
                                Game(id = data.id, name = data.name, score = score)
                            )
                        }
                    )
                }

                composable<Settings>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up, tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down, tween(500)
                        )
                    },
                ) {
                    SettingsScreen(
                        onLogOut = { navController.navigate(Auth) { popUpToRoute(Splash) } },
                        navigateBack = { navController.popBackStack() }
                    )
                }
                composable<Game>(
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up, tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down, tween(500)
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

fun <T : Any> NavOptionsBuilder.popUpToRoute(route: T) {
    popUpTo(route) { inclusive = true }
}
