package com.example.proyectofinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "context")
class Contexto (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String,
    var opt1: String,
    var opt2: String,
    var opt3: String,
    var correct: Int,
    var sentence: String) {
    constructor(word: String, opt1: String, opt2: String, opt3: String, correct: Int, sentence: String):
            this( 0, word, opt1, opt2, opt3, correct, sentence)
}