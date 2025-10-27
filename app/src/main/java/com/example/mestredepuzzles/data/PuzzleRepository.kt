package com.example.mestredepuzzles.data

import kotlinx.coroutines.flow.Flow

// O Repositório atua como uma ponte limpa entre o ViewModel e o DAO.
class PuzzleRepository(private val puzzleDao: PuzzleDao) {

    fun getAllPuzzles(): Flow<List<Puzzle>> = puzzleDao.getAllPuzzles()
    fun getRanking(): Flow<List<Puzzle>> = puzzleDao.getRanking()
    fun getPuzzle(id: Int): Flow<Puzzle> = puzzleDao.getPuzzle(id)

    // Funções de escrita (suspend para serem chamadas em coroutines)
    suspend fun insert(puzzle: Puzzle) = puzzleDao.insert(puzzle)
    suspend fun update(puzzle: Puzzle) = puzzleDao.update(puzzle)
    suspend fun delete(puzzle: Puzzle) = puzzleDao.delete(puzzle)
}