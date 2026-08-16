package com.joseapps.lapetadopt.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val Terracotta = Color(0xFF8D5A44)
private val TerracottaLight = Color(0xFFD9A488)
private val SageGreen = Color(0xFF5B7B5A)
private val Cream = Color(0xFFFFF8F2)

private val LightColors = lightColorScheme(
    primary = Terracotta,
    secondary = SageGreen,
    tertiary = TerracottaLight,
    background = Cream
)

private val DarkColors = darkColorScheme(
    primary = TerracottaLight,
    secondary = SageGreen,
    tertiary = Terracotta
)

@Composable
fun LaPetAdoptTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    // Status bar appearance is handled by enableEdgeToEdge() in MainActivity.

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
