package com.example.rate_my_day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.example.rate_my_day.data.RateDayRepository
import com.example.rate_my_day.data.db.RateDaysDatabase
import com.example.rate_my_day.ui.RateMyDayViewModel
import com.example.rate_my_day.ui.RateMyDayApp
import com.example.rate_my_day.ui.theme.RateMyDayTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Force Light mode

        val database = RateDaysDatabase.getDatabase(this)
        val repository = RateDayRepository(database.RateDaysDao())
        val viewModel = RateMyDayViewModel(repository)
        val preferences by lazy {
            com.example.rate_my_day.data.Preferences(this)
        }

        enableEdgeToEdge()
        setContent {
            RateMyDayTheme(preferences) {
                    RateMyDayApp(viewModel = viewModel, preferences)
            }
        }
    }
}

