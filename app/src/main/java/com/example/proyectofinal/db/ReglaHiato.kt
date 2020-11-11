package com.example.proyectofinal.db

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

//@Entity(tableName = "regla_hiato")
class ReglaHiato (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var word: String,
    var syllable: String,
    var silabas: Int,
    var tilde: Boolean,
    var pos: Int) {
    constructor(word: String, syllable: String, silabas: Int, tilde: Boolean, pos: Int): this(0,word, syllable, silabas, tilde, pos)

//    @JvmName("getWord1")
//    public fun getWord(): String {
//        return word
//    }
//    @JvmName("getSyllable1")
//    public fun getSyllable(): String {
//        return syllable
//    }
//
//    @JvmName("getSilabas1")
//    public fun getSilaba(): Int {
//        return silabas
//    }
//    @JvmName("getTilde1")
//    public fun getTilde(): Boolean {
//        return tilde
//    }
//
//    @JvmName("getPos1")
//    public fun getPos(): Int {
//        return pos
//    }
//
//    @JvmName("setWord1")
//    public fun setWord(word: String) {
//        this.word = word
//    }
//
//    @JvmName("setSyllable1")
//    public fun setSyllable(syllable: String) {
//        this.syllable = syllable
//    }
//
//    @JvmName("setSilaba1")
//    public fun setSilaba(silaba: Int) {
//        this.silabas = silaba
//    }
//
//    @JvmName("setTilde1")
//    public fun setTilde(tilde: Boolean) {
//        this.tilde = tilde
//    }
//
//    @JvmName("setPos1")
//    public fun setPos(pos: Int) {
//        this.pos = pos
//    }



}
