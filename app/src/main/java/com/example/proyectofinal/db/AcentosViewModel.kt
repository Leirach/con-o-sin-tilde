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
    val words: LiveData<List<ReglaGeneral>>
    val countReglaGeneral: LiveData<Int>
    var randomReglaGeneral: LiveData<List<ReglaGeneral>>

    var leaderboard: LiveData<List<LeaderboardItem>>

    init {
        val wordsDao = AcentosDatabase.getDatabase(application, viewModelScope).reglaGeneralDao()
        val leaderboardDao = AcentosDatabase.getDatabase(application, viewModelScope).leaderboardDao()
        repo = AcentosRepo(wordsDao, leaderboardDao)
        words = repo.allWords
        countReglaGeneral = repo.wordCount
        randomReglaGeneral = repo.randomReglaGeneral
        leaderboard = repo.leaderboard
    }

    fun getRandomReglaGeneral(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getRandomReglaGeneral()
        randomReglaGeneral = repo.randomReglaGeneral
    }

    fun leaderboardInsert(leaderboardItem: LeaderboardItem): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.insertScore(leaderboardItem)
        repo.deleteLowest(leaderboardItem.juego) // VA AQUI?
    }

    fun nukeTable() = viewModelScope.launch(Dispatchers.IO) {
        repo.nukeTables()
    }
}
