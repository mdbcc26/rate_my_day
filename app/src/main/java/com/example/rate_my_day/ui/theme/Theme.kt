package com.example.rate_my_day.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.rate_my_day.data.Preferences

val DarkColorScheme = darkColorScheme(
    primary = DarkGray,
    secondary = Black,
    tertiary = Gray,
    onPrimary = White,
    onSecondary = LightGray,
    onTertiary = White,
    secondaryContainer = DarkGray,
    surface = Black

    )

val LightColorScheme = lightColorScheme(
    primary = DarkGray,
    secondary = White,
    tertiary = Black,
    onPrimary = White,
    onSecondary = DarkGray,
    onTertiary = White,
    secondaryContainer = Gray,
    surface = White
)

@Composable
fun RateMyDayTheme(
    preferences: Preferences,

    content: @Composable () -> Unit
) {
    val mode by preferences.getBoolean.collectAsState(false)

    val colorScheme = if (mode) {
        DarkColorScheme } else {
        LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}