package com.example.magicnumberapp

import android.app.Application
import com.example.magicnumberapp.data.local.DataStoreManager
import com.example.magicnumberapp.data.local.SeedDataLoader
import com.example.magicnumberapp.data.repository.RiddleRepository

class AdivinaloApplication : Application() {

    lateinit var repository: RiddleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = RiddleRepository(
            dataStoreManager = DataStoreManager(this),
            seedDataLoader = SeedDataLoader(this)
        )
    }
}
