package com.example.proyectofinal.db

import android.util.Log
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AcentosRepo(private val segaDao: ReglaGeneralDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<ReglaGeneral>> = segaDao.getWords()

    suspend fun nukeTable() {
        segaDao.nukeTable()
    }

    suspend fun getRandom() {
        segaDao.getRandom()
    }
}
