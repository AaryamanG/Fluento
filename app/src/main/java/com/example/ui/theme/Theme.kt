package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFB1C9F8),
    tertiary = Color(0xFFFFD8E4),
    background = Color(0xFF1D1B1E),
    surface = Color(0xFF2D2A2E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFFFDF8F6),
    onSurface = Color(0xFFFDF8F6),
    error = CoralCorrection
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BentoPurpleAccent,
    secondary = BentoBlueAccent,
    tertiary = BentoPinkLight,
    background = BentoLightBg,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = BentoLightText,
    onSurface = BentoLightText,
    error = CoralCorrection
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
