package com.shobhit.mvvmtodolistapp.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceManager constructor(private val context: Context) {

    private object PreferencesKeys {
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }

    companion object {
        private val USER_PREFERENCES_NAME = "user_preferences"
        private val Context.dataStore by preferencesDataStore(
            name = USER_PREFERENCES_NAME
        )
    }


    val hideCompletedFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
           val hideCompleted = preferences[PreferenceManager.PreferencesKeys.HIDE_COMPLETED]?: false
           return@map hideCompleted
        }

    suspend fun saveHideCompleted(hideCompleted:Boolean){
        context.dataStore.edit {preferences ->
            preferences[PreferenceManager.PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

}