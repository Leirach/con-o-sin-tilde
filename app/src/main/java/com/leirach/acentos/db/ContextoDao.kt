package com.leirach.acentos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContextoDao {
    @Query("SELECT * FROM context")
    fun getWords(): LiveData<List<Contexto>>

    @Query("SELECT COUNT(id) FROM context")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM context ORDER BY RANDOM() LIMIT 10")
    fun getRandomSync(): LiveData<List<Contexto>>

    @Query("SELECT * FROM context ORDER BY RANDOM() LIMIT 10")
    suspend fun getRandom(): List<Contexto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: Contexto)

    @Query("DELETE FROM context")
    suspend fun nukeTable()
}
