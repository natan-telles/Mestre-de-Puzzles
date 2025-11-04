package com.example.mestredepuzzles

import android.app.Application
import androidx.room.Room
import com.example.mestredepuzzles.data.AppDatabase
import com.example.mestredepuzzles.data.PuzzleRepository

/**
 * 📦 CONTAINER DE DEPENDÊNCIAS - Injeção manual (sem Hilt/Dagger)
 *
 * Define quais objetos estão disponíveis globalmente (Singleton).
 */
interface AppContainer {
    val puzzleRepository: PuzzleRepository
}

/**
 * 🏗️ IMPLEMENTAÇÃO DO CONTAINER
 *
 * Cria instâncias únicas (lazy) do banco e repositório.
 */
class AppDataContainer(private val applicationContext: Application) : AppContainer {

    // Banco de dados (lazy = cria só quando usado pela 1ª vez)
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "puzzle_database"  // Arquivo: /data/data/.../databases/puzzle_database
        ).build()
    }

    // Repository com DAO injetado (lazy)
    override val puzzleRepository: PuzzleRepository by lazy {
        PuzzleRepository(database.puzzleDao())
    }
}

/**
 * 🚀 APPLICATION CLASS - Ponto de entrada do app
 *
 * Criada ANTES de qualquer Activity, vive durante todo o ciclo do app.
 * Registrada no AndroidManifest.xml como android:name.
 */
class MestreDePuzzlesApplication : Application() {

    // Container global (inicializado no onCreate)
    lateinit var container: AppContainer

    // Chamado uma única vez ao iniciar o app
    override fun onCreate() {
        super.onCreate()
        // Inicializa container de dependências
        container = AppDataContainer(this)
    }
}
