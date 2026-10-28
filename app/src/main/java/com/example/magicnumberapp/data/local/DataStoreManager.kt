package com.example.magicnumberapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.data.model.GameStats
import com.example.magicnumberapp.data.model.Riddle
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.adivinaloDataStore by preferencesDataStore(name = "adivinalo_data")

class DataStoreManager(context: Context) {

    private val appContext = context.applicationContext

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private object Keys {
        val RIDDLES_JSON = stringPreferencesKey("riddles_json")
        val HISTORY_JSON = stringPreferencesKey("history_json")
        val GAMES_PLAYED = intPreferencesKey("games_played")
        val TOTAL_CORRECT = intPreferencesKey("total_correct")
        val TOTAL_WRONG = intPreferencesKey("total_wrong")
        val SEED_LOADED = booleanPreferencesKey("seed_loaded")
        val SEED_VERSION = intPreferencesKey("seed_version")
    }

    private val preferences: Flow<androidx.datastore.preferences.core.Preferences>
        get() = appContext.adivinaloDataStore.data.catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }

    val riddles: Flow<List<Riddle>> = preferences.map { prefs ->
        decodeRiddles(prefs[Keys.RIDDLES_JSON])
    }

    val history: Flow<List<GameResult>> = preferences.map { prefs ->
        decodeHistory(prefs[Keys.HISTORY_JSON])
            .sortedByDescending { it.timestamp }
    }

    val stats: Flow<GameStats> = preferences.map { prefs ->
        GameStats(
            gamesPlayed = prefs[Keys.GAMES_PLAYED] ?: 0,
            totalCorrect = prefs[Keys.TOTAL_CORRECT] ?: 0,
            totalWrong = prefs[Keys.TOTAL_WRONG] ?: 0
        )
    }

    suspend fun saveRiddles(riddles: List<Riddle>) {
        appContext.adivinaloDataStore.edit { prefs ->
            prefs[Keys.RIDDLES_JSON] = json.encodeToString(riddles)
        }
    }

    suspend fun addRiddle(riddle: Riddle) {
        appContext.adivinaloDataStore.edit { prefs ->
            val current = decodeRiddles(prefs[Keys.RIDDLES_JSON])
            prefs[Keys.RIDDLES_JSON] = json.encodeToString(current + riddle)
        }
    }

    suspend fun addGameResult(result: GameResult) {
        appContext.adivinaloDataStore.edit { prefs ->
            val current = decodeHistory(prefs[Keys.HISTORY_JSON])
            prefs[Keys.HISTORY_JSON] = json.encodeToString(current + result)
            prefs[Keys.GAMES_PLAYED] = (prefs[Keys.GAMES_PLAYED] ?: 0) + 1
            prefs[Keys.TOTAL_CORRECT] = (prefs[Keys.TOTAL_CORRECT] ?: 0) + result.correctAnswers
            prefs[Keys.TOTAL_WRONG] = (prefs[Keys.TOTAL_WRONG] ?: 0) +
                (result.totalRiddles - result.correctAnswers)
        }
    }

    suspend fun isSeedLoaded(): Boolean =
        preferences.first()[Keys.SEED_LOADED] ?: false

    suspend fun seedVersion(): Int =
        preferences.first()[Keys.SEED_VERSION] ?: 0

    suspend fun setSeedVersion(version: Int) {
        appContext.adivinaloDataStore.edit { prefs ->
            prefs[Keys.SEED_VERSION] = version
        }
    }

    suspend fun markSeedLoaded() {
        appContext.adivinaloDataStore.edit { prefs ->
            prefs[Keys.SEED_LOADED] = true
        }
    }

    private fun decodeRiddles(raw: String?): List<Riddle> =
        if (raw.isNullOrBlank()) {
            emptyList()
        } else {
            runCatching { json.decodeFromString<List<Riddle>>(raw) }.getOrDefault(emptyList())
        }

    private fun decodeHistory(raw: String?): List<GameResult> =
        if (raw.isNullOrBlank()) {
            emptyList()
        } else {
            runCatching { json.decodeFromString<List<GameResult>>(raw) }.getOrDefault(emptyList())
        }
}
