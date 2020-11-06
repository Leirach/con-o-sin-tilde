package com.example.proyectofinal.db

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "regla_general")
class ReglaGeneral (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String,
    var syllable: String,
    var tilde: Boolean,
    var pos: Int) {
    constructor(word: String, syllable: String, tilde: Boolean, pos: Int): this(0, word, syllable, tilde, pos)
}
