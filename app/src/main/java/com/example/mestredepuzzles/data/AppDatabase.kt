package com.example.mestredepuzzles.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 🏢 CLASSE DE CONFIGURAÇÃO DO BANCO DE DADOS
 *
 * Esta classe abstrata é o ponto central de configuração do Room Database.
 * Ela define quais entidades (tabelas) existem e como acessá-las através dos DAOs.
 *
 * 🎯 PROPÓSITO:
 * - Configurar o banco de dados SQLite usando a biblioteca Room
 * - Definir a versão do banco (importante para migrações futuras)
 * - Expor os DAOs (interfaces de acesso aos dados) para o resto do app
 *
 * 📚 CONCEITOS IMPORTANTES:
 * - @Database: Marca esta classe como o banco de dados principal do app
 * - RoomDatabase: Classe base que o Room usa para gerenciar o SQLite
 * - abstract: Não implementamos esta classe, o Room gera o código automaticamente
 *
 * 🔧 PARÂMETROS DA ANOTAÇÃO @Database:
 *
 * @param entities = [Puzzle::class]
 *        Lista de todas as entidades (tabelas) do banco
 *        Atualmente temos apenas a tabela "puzzle"
 *        Se adicionar novas tabelas, inclua aqui: [Puzzle::class, User::class, etc]
 *
 * @param version = 1
 *        Versão atual do schema (estrutura) do banco de dados
 *        ⚠️ IMPORTANTE: Sempre que mudar a estrutura das tabelas, incremente este número
 *        Exemplo: adicionar nova coluna = mudar para version = 2
 *        Mudanças de versão requerem MIGRAÇÕES para não perder dados
 *
 * @param exportSchema = false
 *        Define se o Room deve exportar o schema do banco para um arquivo JSON
 *        false = não exporta (adequado para apps pequenos/médios)
 *        true = exporta (útil para controle de versão e debugging em apps grandes)
 */
@Database(
    entities = [Puzzle::class],     // 📦 Tabelas do banco de dados
    version = 1,                     // 🔢 Versão do schema
    exportSchema = false             // 📄 Não exportar schema para JSON
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 🎯 ACESSO AO DAO DE PUZZLES
     *
     * Este método abstrato retorna a implementação do PuzzleDao.
     * O Room gera automaticamente a implementação concreta desta interface.
     *
     * 💡 COMO USAR:
     * val database: AppDatabase = // ... obter instância do banco
     * val dao = database.puzzleDao()
     * dao.insert(puzzle) // Usar os métodos do DAO
     *
     * 🔄 FLUXO:
     * AppDatabase → puzzleDao() → PuzzleDao → Operações SQL
     */
    abstract fun puzzleDao(): PuzzleDao
}