package com.mobilitydevai.sce.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = YellowDarkDev,
    onPrimary = WhiteDev,
    background = DarkBackground,
    onBackground = GrayDev90,
    surface = YellowDarkDev,
    onSurfaceVariant = WhiteDev,
    secondary = PurpleGrey80,
    onSecondary = WhiteDev,
    tertiary = PinkDev,
    onTertiary = GrayDev30,
)

private val LightColorScheme = lightColorScheme(
    primary = YellowLightDev,
    onPrimary = BlackDev,
    background = WhiteDev,
    onBackground = GrayDev30,
    surface = YellowDarkDev,
    onSurfaceVariant = GrayDev30,
    secondary = PurpleGrey80,
    onSecondary =GrayDev30,
    tertiary = PinkDev,
    onTertiary = GrayDev100,

)

@Composable
fun SimpsonsInfoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

    /**
     *  Controla los colores del sistema
     */
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colorScheme.background,
            darkIcons = useDarkIcons
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}