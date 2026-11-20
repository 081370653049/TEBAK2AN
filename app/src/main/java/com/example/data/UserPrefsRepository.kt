package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tebak_hewan_prefs")

data class UserPreferences(
    val isSoundEnabled: Boolean = true,
    val difficulty: Difficulty = Difficulty.EASY_TWO,
    val totalStars: Int = 0
)

class UserPrefsRepository(private val context: Context) {

    private object Keys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val DIFFICULTY = stringPreferencesKey("difficulty")
        val TOTAL_STARS = intPreferencesKey("total_stars")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val difficultyName = prefs[Keys.DIFFICULTY] ?: Difficulty.EASY_TWO.name
        val difficulty = runCatching { Difficulty.valueOf(difficultyName) }
            .getOrDefault(Difficulty.EASY_TWO)
        UserPreferences(
            isSoundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            difficulty = difficulty,
            totalStars = prefs[Keys.TOTAL_STARS] ?: 0
        )
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DIFFICULTY] = difficulty.name
        }
    }

    suspend fun addStars(count: Int) {
        if (count <= 0) return
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.TOTAL_STARS] ?: 0
            prefs[Keys.TOTAL_STARS] = current + count
        }
    }
}
