package com.leirach.acentos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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