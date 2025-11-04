package com.example.mestredepuzzles.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 🧩 ENTIDADE PUZZLE - Modelo de dados do enigma
 *
 * Representa um puzzle no banco SQLite via Room.
 * @Entity converte esta classe em uma tabela "puzzle".
 */
@Entity(tableName = "puzzle")
data class Puzzle(
    // 🔑 Chave primária auto-incrementada (0 = novo puzzle, banco gera ID real)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 📝 Título obrigatório do puzzle
    @ColumnInfo(name = "title")
    val title: String,

    // 💡 Dicas opcionais (nullable) para ajudar o jogador
    @ColumnInfo(name = "hint1")
    val hint1: String? = null,

    @ColumnInfo(name = "hint2")
    val hint2: String? = null,

    @ColumnInfo(name = "hint3")
    val hint3: String? = null,

    // ⏱️ Tempo limite opcional em segundos (ex: 300 = 5min)
    @ColumnInfo(name = "time_limit_sec")
    val timeLimitSec: Int? = null,

    // ✅ Status: true = resolvido, false = pendente
    @ColumnInfo(name = "solved")
    val solved: Boolean = false,

    // 🎯 Contador de tentativas (usado no ranking)
    @ColumnInfo(name = "attempts")
    val attempts: Int = 0
)