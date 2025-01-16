package com.example.rate_my_day.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.example.rate_my_day.data.Preferences



@Composable
fun RateMyDayTheme(
    preferences: Preferences,

    content: @Composable () -> Unit
) {

    val theme by preferences.getString.collectAsState("")

    val colorScheme = when (theme) {
        "Dark Mode" -> DarkColorScheme
        "Default" -> LightColorScheme
        else -> {LightColorScheme}
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}