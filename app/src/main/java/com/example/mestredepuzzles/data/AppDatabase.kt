package com.example.mestredepuzzles.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 🏢 BANCO DE DADOS ROOM - Configuração central do SQLite
 *
 * Define entidades (tabelas), versão e DAOs.
 * Room gera implementação automaticamente.
 *
 * @entities Lista de tabelas (adicione novas aqui)
 * @version Incrementar ao mudar schema (requer migração)
 * @exportSchema false = não exporta JSON do schema
 */
@Database(
    entities = [Puzzle::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAO de Puzzles (Room gera implementação)
    abstract fun puzzleDao(): PuzzleDao
}
