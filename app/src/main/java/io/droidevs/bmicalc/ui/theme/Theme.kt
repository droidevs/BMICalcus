package io.droidevs.bmicalc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006D3B),
    secondary = Color(0xFF516148),
    tertiary = Color(0xFF37505C),
    surface = Color(0xFFFCFDF7),
    onSurface = Color(0xFF1A1C19),
    background = Color(0xFFFCFDF7)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6CDA9B),
    secondary = Color(0xFFB9CCAD),
    tertiary = Color(0xFFA3C9DD),
    surface = Color(0xFF1A1C19),
    onSurface = Color(0xFFE2E3DD),
    background = Color(0xFF1A1C19)
)

@Composable
fun BmiCalcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val blueYellowGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF0ffff0),
        Color(0xFFfeff01)
    )
)