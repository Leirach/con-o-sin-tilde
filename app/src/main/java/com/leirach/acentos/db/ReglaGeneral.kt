package com.leirach.acentos.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regla_general")
class ReglaGeneral (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String,
    var syllable: String,
    var tilde: Boolean,
    var pos: Int) {
    constructor(word: String, syllable: String, tilde: Boolean, pos: Int): this(0, word, syllable, tilde, pos)
}
