package com.example.rate_my_day.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class Preferences(private val context : Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")
    private val CURRENT_THEME = stringPreferencesKey("current_theme")

    val getString: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_THEME] ?: "default"
    }

    suspend fun saveString(value: String): Preferences {
        return context.dataStore.edit { preferences ->
            preferences[CURRENT_THEME] = value}
    }


}