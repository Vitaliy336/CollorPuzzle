package com.colors.collorpuzzle.ui.theme

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = colorPrimaryDark,
    onPrimary = colorOnPrimaryDark,
    primaryContainer = colorPrimaryContainerDark,
    onPrimaryContainer = colorOnPrimaryContainerDark,
    secondary = colorSecondaryDark,
    onSecondary = colorOnSecondaryDark,
    secondaryContainer = colorSecondaryContainerDark ,
    onSecondaryContainer = colorOnSecondaryContainerDark,
    surface = colorSurfaceDark,
    onSurface = colorOnSurfaceDark,
    tertiary = colorTertiaryDark,
    onTertiary = colorOnTertiaryDark,
    background = colorBackgroundDark,
    onBackground = colorOnBackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = colorPrimaryLight,
    onPrimary = colorOnPrimaryLight,
    primaryContainer = colorPrimaryContainerLight,
    onPrimaryContainer = colorOnPrimaryContainerLight,
    secondary = colorSecondaryLight,
    onSecondary = colorOnSecondaryLight,
    secondaryContainer = colorOnSecondaryLight ,
    onSecondaryContainer = colorOnSecondaryContainerLight,
    surface = colorSurfaceLight,
    onSurface = colorOnSurfaceLight,
    background = colorBackgroundLight,
    onBackground = colorOnBackgroundLight
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController.apply {
                    this!!.hide(WindowInsets.Type.navigationBars())
                    this.hide(WindowInsets.Type.statusBars())
                    systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}