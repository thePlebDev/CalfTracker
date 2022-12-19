package com.elliottsoftware.calftracker.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class ThemePreference(context: Context) {
    companion object {
        private val UI_MODE_KEY = preferencesKey<Boolean>("ui_mode")
    }

    // create the data store
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "ui_mode_preference"
    )
    // save to the data store
    suspend fun saveToDataStore(isNightMode: Boolean) {
        val data = dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isNightMode
        }

    }
    // read from the datastore
    val uiMode: Flow<Boolean> = dataStore.data
        // Intermediate operation to return a boolean
        .map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: false
            uiMode
        }


}
