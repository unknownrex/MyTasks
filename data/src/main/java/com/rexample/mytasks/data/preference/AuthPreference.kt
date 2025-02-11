package com.rexample.mytasks.data.preference

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
        private val EMAIL_KEY = stringPreferencesKey("email")
    }

    suspend fun saveUser(userId: Int, email: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(EMAIL_KEY)
        }
    }

    fun getUserId() = dataStore.data.map { preference ->
        preference[USER_ID]
    }

    fun isSessionValid() = dataStore.data.map { preferences ->
        preferences[USER_ID] != null && preferences[EMAIL_KEY]?.isNotBlank() == true
    }

    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    val email: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY]
    }
}
