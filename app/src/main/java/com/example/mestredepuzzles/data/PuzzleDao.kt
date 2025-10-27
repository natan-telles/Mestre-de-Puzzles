package com.example.mestredepuzzles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleDao {

    // Insere um novo puzzle [cite: 137]
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(puzzle: Puzzle)

    // Atualiza um puzzle (usado para alterar status, tentativas, etc.) [cite: 137]
    @Update
    suspend fun update(puzzle: Puzzle)

    // Deleta um puzzle
    @Delete
    suspend fun delete(puzzle: Puzzle)

    // Busca um puzzle específico por ID
    @Query("SELECT * FROM puzzle WHERE id = :id")
    fun getPuzzle(id: Int): Flow<Puzzle>

    // Busca TODOS os puzzles para a lista principal
    @Query("SELECT * FROM puzzle ORDER BY id DESC")
    fun getAllPuzzles(): Flow<List<Puzzle>>

    // Consulta para o Ranking [cite: 138]
    // Busca puzzles resolvidos (solved = 1) e ordena por tempo limite (menor primeiro) e tentativas (menor primeiro)
    @Query("SELECT * FROM puzzle WHERE solved = 1 ORDER BY time_limit_sec ASC, attempts ASC")
    fun getRanking(): Flow<List<Puzzle>>
}