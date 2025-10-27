package com.example.mestredepuzzles

import android.app.Application
import androidx.room.Room
import com.example.mestredepuzzles.data.AppDatabase
import com.example.mestredepuzzles.data.PuzzleRepository

/**
 * Interface que define o container de dependências do aplicativo.
 */
interface AppContainer {
    val puzzleRepository: PuzzleRepository
}

/**
 * Implementação manual do container de dependências.
 * Inicializa o banco de dados e o repositório.
 */
class AppDataContainer(private val applicationContext: Application) : AppContainer {

    // Inicializa o banco de dados Room como um singleton (instância única)
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "puzzle_database" // Nome do arquivo do banco de dados no dispositivo
        ).build()
    }

    // Fornece o Repositório, injetando o DAO do banco de dados
    override val puzzleRepository: PuzzleRepository by lazy {
        PuzzleRepository(database.puzzleDao())
    }
}

/**
 * Classe principal da Aplicação Android.
 * É o ponto onde o container de dependências é criado.
 */
class MestreDePuzzlesApplication : Application() {

    // O container será inicializado no onCreate()
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializa o container, passando o contexto da aplicação
        container = AppDataContainer(this)
    }
}