package com.example.rate_my_day.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.rate_my_day.data.Preferences

/* TODO

RGBY
Red -> Green
Urple -> Yeller
Blew -> Orangay
babby blew -> neom ponk


 */

@Composable
fun ColorScheme.star5(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {
        "Dark Mode" -> Persimmon
        "Green" -> Green5
        "Default" -> Tekhelet
        else -> { Persimmon }
    }
}

@Composable
fun ColorScheme.star4(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {
        "Dark Mode" -> Persimmon
        "Green" -> Green4
        "Default" -> Tekhelet
        else -> { Persimmon }
    }
}

@Composable
fun ColorScheme.star3(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {
        "Dark Mode" -> Persimmon
        "Green" -> Green3
        "Default" -> Tekhelet
        else -> { Persimmon }
    }
}

@Composable
fun ColorScheme.star2(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {
        "Dark Mode" -> Persimmon
        "Green" -> Green2
        "Default" -> Tekhelet
        else -> { Persimmon }
    }
}

@Composable
fun ColorScheme.star1(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {
        "Dark Mode" -> Persimmon
        "Green" -> Green1
        "Default" -> Tekhelet
        else -> { Persimmon }
    }
}
