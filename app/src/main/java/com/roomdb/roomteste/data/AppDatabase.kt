package com.roomdb.roomteste.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roomdb.roomteste.data.dao.ContatoDao
import com.roomdb.roomteste.domain.entidade.Contato

@Database(entities = [Contato::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contatoDao(): ContatoDao
}
