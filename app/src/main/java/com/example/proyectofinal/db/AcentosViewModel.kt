package com.example.proyectofinal.db

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AcentosViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AcentosRepo

    //regla general
    val rgCount: LiveData<Int>
    var rgRandom: LiveData<List<ReglaGeneral>>

    //contexto
    val contextCount: LiveData<Int>
    var contextRandom: LiveData<List<Contexto>>

    //contexto
    val hiatoCount: LiveData<Int>
    var hiatoRandom: LiveData<List<Hiato>>

    // leaderboards
    var leaderboard: LiveData<List<LeaderboardItem>>

    init {
        val rgDao = AcentosDatabase.getDatabase(application, viewModelScope).reglaGeneralDao()
        val contextDao = AcentosDatabase.getDatabase(application, viewModelScope).contextDao()
        val hiatoDao = AcentosDatabase.getDatabase(application, viewModelScope).hiatoDao()
        val leaderboardDao = AcentosDatabase.getDatabase(application, viewModelScope).leaderboardDao()
        repo = AcentosRepo(rgDao, contextDao, hiatoDao, leaderboardDao)
        rgCount = repo.rgCount
        rgRandom = repo.rgRandom
        contextCount = repo.contextCount
        contextRandom = repo.contextRandom
        hiatoCount = repo.hiatoCount
        hiatoRandom = repo.hiatoRandom
        leaderboard = repo.leaderboard
    }

    fun getRandomReglaGeneral(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getRandomReglaGeneral()
        //rgRandom = repo.rgRandom
    }

    fun getRandomContexto(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getRandomContexto()
        //contextRandom = repo.contextRandom
    }

    fun getRandomHiato(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getRandomHiato()
        //hiatoRandom = repo.hiatoRandom
    }

    fun leaderboardInsert(leaderboardItem: LeaderboardItem): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.insertScore(leaderboardItem)
        repo.deleteLowest(leaderboardItem.juego)
    }

    fun nukeTable() = viewModelScope.launch(Dispatchers.IO) {
        repo.nukeTables()
    }
}
