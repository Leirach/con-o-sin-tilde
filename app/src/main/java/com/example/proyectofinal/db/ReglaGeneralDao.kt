package com.example.proyectofinal.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReglaGeneralDao {
    @Query("SELECT * FROM regla_general")
    fun getWords(): LiveData<List<ReglaGeneral>>

    @Query("SELECT * FROM regla_general ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): List<ReglaGeneral>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: ReglaGeneral)

    @Query("DELETE FROM regla_general")
    suspend fun nukeTable()
}

