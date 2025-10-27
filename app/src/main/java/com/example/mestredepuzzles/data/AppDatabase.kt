package com.example.mestredepuzzles.data

import androidx.room.Database
import androidx.room.RoomDatabase

// Configura o Room para usar a Entity Puzzle
@Database(entities = [Puzzle::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Exposição do DAO para o Repositório
    abstract fun puzzleDao(): PuzzleDao
}