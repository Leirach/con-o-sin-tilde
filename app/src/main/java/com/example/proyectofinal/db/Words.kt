package com.example.proyectofinal.db

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
class Words (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String) {
    constructor(word: String): this(0, word)
}
