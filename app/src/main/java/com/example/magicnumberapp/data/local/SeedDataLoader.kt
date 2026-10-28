package com.example.magicnumberapp.data.local

import android.content.Context
import com.example.magicnumberapp.data.model.Riddle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SeedDataLoader(private val context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Serializable
    private data class SeedPayload(val riddles: List<Riddle> = emptyList())

    fun load(): List<Riddle> = runCatching {
        val raw = context.assets.open(SEED_FILE).bufferedReader().use { it.readText() }
        json.decodeFromString<SeedPayload>(raw).riddles
    }.getOrDefault(emptyList())

    private companion object {
        const val SEED_FILE = "riddles_seed.json"
    }
}
