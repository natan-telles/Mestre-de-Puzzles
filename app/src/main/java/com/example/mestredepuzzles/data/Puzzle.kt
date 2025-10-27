package com.example.mestredepuzzles.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a tabela 'puzzle' no banco de dados SQLite.
 * Corresponde ao Enunciado 10: Mestre de Puzzles da Mansão[cite: 135].
 */
@Entity(tableName = "puzzle")
data class Puzzle(
    // id INTEGER PRIMARY KEY AUTOINCREMENT [cite: 141]
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // title TEXT NOT NULL [cite: 142]
    @ColumnInfo(name = "title")
    val title: String,

    // hint1 TEXT, hint2 TEXT, hint3 TEXT [cite: 143]
    @ColumnInfo(name = "hint1")
    val hint1: String? = null,

    @ColumnInfo(name = "hint2")
    val hint2: String? = null,

    @ColumnInfo(name = "hint3")
    val hint3: String? = null,

    // time_limit_sec INTEGER [cite: 144]
    @ColumnInfo(name = "time_limit_sec")
    val timeLimitSec: Int? = null, // Tempo limite em segundos

    // solved INTEGER DEFAULT 0 [cite: 145] - Usamos Boolean (Room converte para 0/1)
    @ColumnInfo(name = "solved")
    val solved: Boolean = false,

    // attempts INTEGER DEFAULT 0 [cite: 146]
    @ColumnInfo(name = "attempts")
    val attempts: Int = 0
)