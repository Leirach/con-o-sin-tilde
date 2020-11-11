package com.example.proyectofinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hiatos")
class Hiato(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String,
    var syllable: String,
    var syllableCount: Int,
    var tilde: Boolean,
    var pos: Int) {
    constructor(word: String, syllable: String, syllableCount: Int, tilde: Boolean, pos: Int):
            this(0, word, syllable, syllableCount, tilde, pos)
}