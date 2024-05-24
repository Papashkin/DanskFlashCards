package com.antsfamily.danskflashcards.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
//
//private val darkColorScheme = darkColorScheme(
//    primary = dark_primary,
//    secondary = dark_secondary,
//    tertiary = dark_neutral,
//    background = dark_background,
//    surface = dark_surface,
//    onSurface = dark_text,
//    onBackground = dark_text
//)

private val lightColorScheme = lightColorScheme(
    primary = wistful_500,
    secondary = light_secondary,
    tertiary = light_neutral,
    background = wistful_0,
    surface = wistful_200,
    onSurface = wistful_1000,
    onBackground = light_text
)

@Composable
fun DanskFlashCardsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme //TODO think to set both themes
//    val colorScheme = when {
//        darkTheme -> darkColorScheme
//        else -> lightColorScheme
//    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
