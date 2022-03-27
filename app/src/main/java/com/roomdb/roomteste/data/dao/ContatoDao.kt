package com.roomdb.roomteste.data.dao

import androidx.room.*
import com.roomdb.roomteste.domain.entidade.Contato

@Dao
interface ContatoDao {
    @Query("SELECT * FROM contato")
    suspend fun getAll(): List<Contato>

    @Query("SELECT * FROM contato WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): Contato

    @Insert
    suspend fun insertAll(vararg contato: Contato)

    @Delete
    suspend fun delete(contato: Contato)

}