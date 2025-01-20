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

        "Red" -> Red5
        "Green" -> Green5
        "Blue" -> Blue5
        "Yellow" -> Yellow5
        "Orchard" -> Orchard5
        "Rimmy Tim" -> Rimmy_Tim5
        "Fanta" -> Fanta5
        "OFISF" -> OFISF5
        "Legacy" -> Persimmon
        else -> {
            Persimmon
        }
    }
}

@Composable
fun ColorScheme.star4(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {

        "Red" -> Red4
        "Green" -> Green4
        "Blue" -> Blue4
        "Yellow" -> Yellow4
        "Orchard" -> Orchard4
        "Rimmy Tim" -> Rimmy_Tim4
        "Fanta" -> Fanta4
        "OFISF" -> OFISF4
        "Legacy" -> Tangerine
        else -> {
            Tangerine
        }
    }
}

@Composable
fun ColorScheme.star3(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {

        "Red" -> Red3
        "Green" -> Green3
        "Blue" -> Blue3
        "Yellow" -> Yellow3
        "Orchard" -> Orchard3
        "Rimmy Tim" -> Rimmy_Tim3
        "Fanta" -> Fanta3
        "OFISF" -> OFISF3
        "Legacy" -> SelectiveYellow
        else -> {
            SelectiveYellow
        }
    }
}

@Composable
fun ColorScheme.star2(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {

        "Red" -> Red2
        "Green" -> Green2
        "Blue" -> Blue2
        "Yellow" -> Yellow2
        "Orchard" -> Orchard2
        "Rimmy Tim" -> Rimmy_Tim2
        "Fanta" -> Fanta2
        "OFISF" -> OFISF2
        "Legacy" -> MedSlateBlue
        else -> {
            MedSlateBlue
        }
    }
}

@Composable
fun ColorScheme.star1(preferences: Preferences): Color {
    val theme by preferences.getString.collectAsState("")
    return when (theme) {

        "Red" -> Red1
        "Green" -> Green1
        "Blue" -> Blue1
        "Yellow" -> Yellow1
        "Orchard" -> Orchard1
        "Rimmy Tim" -> Rimmy_Tim1
        "Fanta" -> Fanta1
        "OFISF" -> OFISF1
        "Legacy" -> Tekhelet
        else -> {
            Tekhelet
        }
    }
}
