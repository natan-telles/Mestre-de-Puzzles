package com.example.mestredepuzzles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 🗄️ DAO - Interface de operações no banco de dados
 *
 * Define métodos CRUD para a tabela "puzzle".
 * Room gera o SQL automaticamente. Flow observa mudanças em tempo real.
 */
@Dao
interface PuzzleDao {

    // ➕ Insere novo puzzle (REPLACE se ID já existir)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(puzzle: Puzzle)

    // ✏️ Atualiza puzzle existente (identifica pelo ID)
    @Update
    suspend fun update(puzzle: Puzzle)

    // 🗑️ Remove puzzle permanentemente
    @Delete
    suspend fun delete(puzzle: Puzzle)

    // 🔍 Busca puzzle por ID (Flow observa mudanças)
    @Query("SELECT * FROM puzzle WHERE id = :id")
    fun getPuzzle(id: Int): Flow<Puzzle>

    // 📋 Lista todos os puzzles (mais recente primeiro)
    @Query("SELECT * FROM puzzle ORDER BY id DESC")
    fun getAllPuzzles(): Flow<List<Puzzle>>

    // 🏆 Ranking de resolvidos (menor tempo, menos tentativas = melhor)
    // Ordena por: 1º tempo_limite ASC, 2º tentativas ASC
    @Query("SELECT * FROM puzzle WHERE solved = 1 ORDER BY time_limit_sec ASC, attempts ASC")
    fun getRanking(): Flow<List<Puzzle>>
}