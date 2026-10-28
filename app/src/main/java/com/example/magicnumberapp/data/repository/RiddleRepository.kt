package com.example.magicnumberapp.data.repository

import com.example.magicnumberapp.data.local.DataStoreManager
import com.example.magicnumberapp.data.local.SeedDataLoader
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.data.model.GameResult
import com.example.magicnumberapp.data.model.GameStats
import com.example.magicnumberapp.data.model.Riddle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RiddleRepository(
    private val dataStoreManager: DataStoreManager,
    private val seedDataLoader: SeedDataLoader
) {

    private val seedMutex = Mutex()

    val riddles: Flow<List<Riddle>> = flow {
        ensureSeedLoaded()
        emitAll(dataStoreManager.riddles)
    }

    val history: Flow<List<GameResult>> = dataStoreManager.history

    val stats: Flow<GameStats> = dataStoreManager.stats

    suspend fun ensureSeedLoaded() {
        seedMutex.withLock {
            val seed = seedDataLoader.load()
            val stored = dataStoreManager.riddles.first()

            if (seed.isEmpty()) {
                dataStoreManager.markSeedLoaded()
                return
            }

            if (stored.isEmpty()) {
                dataStoreManager.saveRiddles(seed)
                dataStoreManager.markSeedLoaded()
                dataStoreManager.setSeedVersion(SEED_VERSION)
                return
            }

            if (dataStoreManager.seedVersion() < SEED_VERSION) {
                val seedHints = seed.associate { it.id to it.hint }
                val updated = stored.map { riddle ->
                    val seedHint = seedHints[riddle.id] ?: return@map riddle
                    if (seedHint == riddle.hint) riddle else riddle.copy(hint = seedHint)
                }
                if (updated != stored) {
                    dataStoreManager.saveRiddles(updated)
                }
                dataStoreManager.setSeedVersion(SEED_VERSION)
            }

            dataStoreManager.markSeedLoaded()
        }
    }

    suspend fun startGame(
        difficulty: Difficulty,
        riddleCount: Int = RIDDLES_PER_GAME
    ): List<Riddle> {
        ensureSeedLoaded()
        return dataStoreManager.riddles.first()
            .filter { it.difficulty == difficulty }
            .shuffled()
            .take(riddleCount)
    }

    suspend fun addRiddle(riddle: Riddle) {
        ensureSeedLoaded()
        dataStoreManager.addRiddle(riddle)
    }

    suspend fun saveGameResult(result: GameResult) {
        dataStoreManager.addGameResult(result)
    }

    companion object {
        const val RIDDLES_PER_GAME = 5
        const val SEED_VERSION = 2
    }
}
