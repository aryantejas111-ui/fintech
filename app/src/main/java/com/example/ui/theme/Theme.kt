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
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueDeep,
    onPrimaryContainer = BlueLight,
    secondary = EmeraldPrimary,
    onSecondary = White,
    secondaryContainer = EmeraldDark,
    onSecondaryContainer = EmeraldLight,
    background = Color(0xFF0F172A),
    onBackground = White,
    surface = Color(0xFF1E293B),
    onSurface = White,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Slate200,
    outline = Color(0xFF475569),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDeep,
    secondary = EmeraldPrimary,
    onSecondary = White,
    secondaryContainer = EmeraldLight,
    onSecondaryContainer = EmeraldDark,
    tertiary = PurpleAccent,
    onTertiary = White,
    background = Slate50,
    onBackground = Slate900,
    surface = White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200,
    outlineVariant = Slate300,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For the requested "COMPLATE APP THEME IN PROFESSIONAL WHITE LOOK", default dynamicColor = false
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

