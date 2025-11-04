package com.example.mestredepuzzles.data

import kotlinx.coroutines.flow.Flow

/**
 * 🏪 REPOSITORY - Camada de abstração entre ViewModel e DAO
 *
 * Encapsula acesso aos dados e facilita testes.
 * Fluxo: UI ← ViewModel ← Repository ← DAO ← SQLite
 */
class PuzzleRepository(private val puzzleDao: PuzzleDao) {

    // 📋 Busca todos os puzzles (Flow observa mudanças automaticamente)
    fun getAllPuzzles(): Flow<List<Puzzle>> = puzzleDao.getAllPuzzles()

    // 🏆 Ranking de resolvidos (menor tempo, menos tentativas = melhor)
    fun getRanking(): Flow<List<Puzzle>> = puzzleDao.getRanking()

    // 🔍 Busca puzzle específico por ID (Flow reativo)
    fun getPuzzle(id: Int): Flow<Puzzle> = puzzleDao.getPuzzle(id)

    // ──── OPERAÇÕES DE ESCRITA (modificam banco) ────

    // ➕ Insere novo puzzle (suspend = assíncrono)
    suspend fun insert(puzzle: Puzzle) = puzzleDao.insert(puzzle)

    // ✏️ Atualiza puzzle existente (identifica pelo ID)
    suspend fun update(puzzle: Puzzle) = puzzleDao.update(puzzle)

    // 🗑️ Deleta puzzle (operação irreversível!)
    suspend fun delete(puzzle: Puzzle) = puzzleDao.delete(puzzle)
}