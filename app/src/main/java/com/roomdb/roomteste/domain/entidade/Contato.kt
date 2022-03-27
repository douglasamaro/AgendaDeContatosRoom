package com.roomdb.roomteste.domain.entidade

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contato")
data class Contato(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "name") val Name: String?,
    @ColumnInfo(name = "number") val Number: String?,
    @ColumnInfo(name = "category") val Category: String?,
    @ColumnInfo(name = "color") val Color: String?,
)
