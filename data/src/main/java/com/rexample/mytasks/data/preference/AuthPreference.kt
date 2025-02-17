package com.rexample.mytasks.data.preference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreference(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
    }

    suspend fun saveUser(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
        }
    }

    fun isSessionValid() = dataStore.data.map { preferences ->
        val isValid = preferences[USER_ID] != null
        Log.d("AuthPreference", "isSessionValid: $isValid")
        isValid
    }

    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }
}
