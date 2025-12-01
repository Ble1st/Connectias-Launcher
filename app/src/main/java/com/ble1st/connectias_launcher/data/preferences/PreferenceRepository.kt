package com.ble1st.connectias_launcher.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_preferences")

@Singleton
class PreferenceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    // Keys
    private object PreferencesKeys {
        val IS_FIRST_RUN = booleanPreferencesKey("is_first_run")
        val GRID_COLUMNS = intPreferencesKey("grid_columns")
        val ICON_PACK_PACKAGE = stringPreferencesKey("icon_pack_package")
    }

    // Flows
    val isFirstRun: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_FIRST_RUN] ?: true
    }

    val gridColumns: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.GRID_COLUMNS] ?: 4 // Default to 4 columns
    }

    val iconPackPackage: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ICON_PACK_PACKAGE]
    }

    // Methods to update
    suspend fun setFirstRunCompleted() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_RUN] = false
        }
    }

    suspend fun setGridColumns(columns: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.GRID_COLUMNS] = columns
        }
    }

    suspend fun setIconPack(packageName: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ICON_PACK_PACKAGE] = packageName
        }
    }
}
