package com.example.rate_my_day.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class Preferences(private val context : Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")
    private val CURRENT_THEME = stringPreferencesKey("current_theme")
    private val CURRENT_MODE = booleanPreferencesKey("current_mode")

    val getString: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_THEME] ?: "default"
    }

    suspend fun saveString(value: String): Preferences {
        return context.dataStore.edit { preferences ->
            preferences[CURRENT_THEME] = value}
    }

    val getBoolean: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_MODE] ?: false
    }

    suspend fun saveBoolean(value: Boolean): Preferences {
        return context.dataStore.edit { preferences ->
            preferences[CURRENT_MODE] = value
        }
    }


}