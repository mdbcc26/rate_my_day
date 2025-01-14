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


class Preferences(val context : Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    suspend fun saveString(key: String, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun getString(key: String):String? {
        return context.dataStore.data.map {
            it[stringPreferencesKey(key)]}.first()

    }


}