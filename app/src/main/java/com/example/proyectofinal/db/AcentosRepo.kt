package com.example.proyectofinal.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AcentosRepo(private val rgDao: ReglaGeneralDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<ReglaGeneral>> = rgDao.getWords()
    val wordCount: LiveData<Int> = rgDao.getCount()
    var randomReglaGeneral: LiveData<List<ReglaGeneral>> = rgDao.getRandomReglaGeneral()

    suspend fun nukeTable() {
        rgDao.nukeTable()
    }

    suspend fun getRandomReglaGeneral() {
        randomReglaGeneral = rgDao.getRandomReglaGeneral()
    }
}
