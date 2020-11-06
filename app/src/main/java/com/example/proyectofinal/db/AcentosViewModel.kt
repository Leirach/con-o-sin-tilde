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

    init {
        val wordsDao = AcentosDatabase.getDatabase(application, viewModelScope).reglaGeneralDao()
        repo = AcentosRepo(wordsDao)
        words = repo.allWords
    }

    fun getRandom(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getRandom()
    }

    fun nukeTable() = viewModelScope.launch(Dispatchers.IO) {
        repo.nukeTable()
    }
}
