package com.leirach.acentos.db

import androidx.lifecycle.LiveData

class AcentosRepo(private val rgDao: ReglaGeneralDao, private val contextDao: ContextoDao,
                  private val hiatoDao: HiatoDao, private val leaderboardDao: LeaderboardDao) {
    // regla general
    val rgCount: LiveData<Int> = rgDao.getCount()
    var rgRandom: LiveData<List<ReglaGeneral>> = rgDao.getRandomSync()

    // contexto
    var contextCount: LiveData<Int> = contextDao.getCount()
    var contextRandom: LiveData<List<Contexto>> = contextDao.getRandomSync()

    // hiatos
    var hiatoCount: LiveData<Int> = hiatoDao.getCount()
    var hiatoRandom: LiveData<List<Hiato>> = hiatoDao.getRandomSync()

    //leaderboards
    var leaderboard: LiveData<List<LeaderboardItem>> = leaderboardDao.getAll()


    // funciones para leaderboard
    suspend fun insertScore(leaderboardItem: LeaderboardItem) {
        leaderboardDao.insert(leaderboardItem)
    }

    suspend fun deleteLowest(juego: Int) {
        leaderboardDao.delete(juego)
    }

    suspend fun nukeTables() {
        rgDao.nukeTable()
        leaderboardDao.nukeTable()
    }

    // actualizar livedata con valores random
    // regla general
    suspend fun getRandomReglaGeneral() {
        rgRandom = rgDao.getRandomSync()
    }

    // contexto
    suspend fun getRandomContexto() {
        contextRandom = contextDao.getRandomSync()
    }

    // hiatos
    suspend fun getRandomHiato() {
        hiatoRandom = hiatoDao.getRandomSync()
    }
}
