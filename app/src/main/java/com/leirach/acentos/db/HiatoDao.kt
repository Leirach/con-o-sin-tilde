package com.leirach.acentos.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HiatoDao {
    @Query("SELECT * FROM hiatos")
    fun getWords(): LiveData<List<Hiato>>

    @Query("SELECT COUNT(id) FROM hiatos")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM hiatos ORDER BY RANDOM() LIMIT 10")
    fun getRandomSync(): LiveData<List<Hiato>>

    @Query("SELECT * FROM hiatos ORDER BY RANDOM() LIMIT 10")
    suspend fun getRandom(): List<Hiato>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: Hiato)

    @Query("DELETE FROM hiatos")
    suspend fun nukeTable()
}