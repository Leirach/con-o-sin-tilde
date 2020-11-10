package com.example.proyectofinal.db

import androidx.lifecycle.LiveData

class AcentosRepo(private val rgDao: ReglaGeneralDao, private val leaderboardDao: LeaderboardDao) {
    // regla general
    val allWords: LiveData<List<ReglaGeneral>> = rgDao.getWords()
    val wordCount: LiveData<Int> = rgDao.getCount()
    var randomReglaGeneral: LiveData<List<ReglaGeneral>> = rgDao.getRandomReglaGeneral()

    //leaderboards
    var leaderboard: LiveData<List<LeaderboardItem>> = leaderboardDao.getAll()


    suspend fun insertScore(leaderboardItem: LeaderboardItem) {
        leaderboardDao.insert(leaderboardItem)
    }

    suspend fun nukeTables() {
        rgDao.nukeTable()
        leaderboardDao.nukeTable()
    }

    // actualizar livedata con valores random
    suspend fun getRandomReglaGeneral() {
        randomReglaGeneral = rgDao.getRandomReglaGeneral()
    }
}
