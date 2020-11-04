package com.example.proyectofinal.db

import android.util.Log
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AcentosRepo(private val noteDao: WordDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = noteDao.getWords()

    suspend fun nukeTable() {
        noteDao.nukeTable()
    }
}
