package com.aftertaste.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val WarmCoffeeLightColorScheme = lightColorScheme(
    primary = TerracottaAccent,
    onPrimary = EspressoText,
    primaryContainer = CoffeeContainer,
    onPrimaryContainer = EspressoText,
    secondary = CoffeeClay,
    onSecondary = ParchmentWhite,
    secondaryContainer = MutedTerracotta,
    onSecondaryContainer = EspressoText,
    tertiary = CoffeeClayDark,
    onTertiary = ParchmentCream,
    background = CoffeeClay,
    onBackground = ParchmentCream,
    surface = ParchmentCream,
    onSurface = EspressoText,
    surfaceVariant = CoffeeSurfaceVariant,
    onSurfaceVariant = CoffeeOnSurfaceVariant,
    outline = CoffeeOutline
)

private val WarmCoffeeDarkColorScheme = darkColorScheme(
    primary = TerracottaAccent,
    onPrimary = EspressoText,
    primaryContainer = CoffeeClayDark,
    onPrimaryContainer = ParchmentCream,
    secondary = MutedTerracotta,
    onSecondary = EspressoText,
    secondaryContainer = CoffeeClay,
    onSecondaryContainer = ParchmentCream,
    tertiary = TerracottaDark,
    onTertiary = EspressoText,
    background = EspressoText,
    onBackground = ParchmentCream,
    surface = CoffeeBrown,
    onSurface = ParchmentCream,
    surfaceVariant = CoffeeClayDark,
    onSurfaceVariant = ParchmentCream,
    outline = CoffeeOutline
)

@Composable
fun AfterTasteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Default false to preserve signature warm coffee brand colors unless requested
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> WarmCoffeeDarkColorScheme
        else -> WarmCoffeeLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AfterTasteTypography,
        shapes = AfterTasteShapes,
        content = content
    )
}
