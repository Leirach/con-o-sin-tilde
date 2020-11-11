package com.example.proyectofinal.db

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.InputStream

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [ReglaGeneral::class, Contexto::class, Hiato::class, LeaderboardItem::class], version = 1, exportSchema = true)
abstract class AcentosDatabase : RoomDatabase() {

    abstract fun reglaGeneralDao(): ReglaGeneralDao
    abstract fun contextDao(): ContextoDao
    abstract fun hiatoDao(): HiatoDao
    abstract fun leaderboardDao(): LeaderboardDao

    private class initCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val rgdao = database.reglaGeneralDao()
                    val leaderboardDao = database.leaderboardDao()
                    // insert initial data
//                    rgdao.nukeTable()
//                    rgdao.insert(ReglaGeneral("paralelo", "pa ra le lo", false, 2))
//                    rgdao.insert(ReglaGeneral("prueba", "prue ba", false, 2))
//                    rgdao.insert(ReglaGeneral("sílaba", "si la ba", true, 3))
//                    rgdao.insert(ReglaGeneral("acción", "ac cion", true, 1))
//                    rgdao.insert(ReglaGeneral("árbol", "ar bol"	, true,2))
//                    rgdao.insert(ReglaGeneral("computadora","com pu ta do ra"	,false,2))
//                    rgdao.insert(ReglaGeneral("problemático", "pro ble ma ti co",true,3))
//                    rgdao.insert(ReglaGeneral("alimentación", "a li men ta cion", true, 1))
//                    rgdao.insert(ReglaGeneral("empezó","em pe zo",true,	1))
//                    rgdao.insert(ReglaGeneral("así", "a si", true, 1))
//                    rgdao.insert(ReglaGeneral("Japón", "Ja pon",true,1))

                    //leaderboardDao.nukeTable()
//                    for (i in 0..29) {
//                        leaderboardDao.insert(LeaderboardItem(i/10, System.currentTimeMillis(), i/10, 600))
//                    }

                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AcentosDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AcentosDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AcentosDatabase::class.java,
                    "acentosdb"
                ).createFromAsset("database/acentos.db").addCallback(initCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}