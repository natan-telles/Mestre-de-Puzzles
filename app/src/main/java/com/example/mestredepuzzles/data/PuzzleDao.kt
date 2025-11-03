package com.example.mestredepuzzles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 🗄️ DAO (DATA ACCESS OBJECT) - INTERFACE DE ACESSO AO BANCO DE DADOS
 *
 * O DAO é o componente responsável por definir TODAS as operações de banco de dados.
 * É como um "gerente" que sabe como inserir, atualizar, deletar e buscar puzzles.
 *
 * 🎯 PROPÓSITO:
 * - Define métodos para manipular a tabela "puzzle" no banco SQLite
 * - O Room gera automaticamente o código SQL necessário para cada método
 * - Trabalha com coroutines (suspend) e Flow para operações assíncronas
 *
 * 📚 CONCEITOS IMPORTANTES:
 * - @Dao: Marca esta interface como um Data Access Object do Room
 * - suspend: Indica funções assíncronas que podem ser pausadas (coroutines)
 * - Flow: Fluxo reativo que emite dados automaticamente quando o banco muda
 * - @Query: Define consultas SQL personalizadas
 */
@Dao
interface PuzzleDao {

    /**
     * ➕ INSERIR NOVO PUZZLE
     *
     * Adiciona um novo puzzle ao banco de dados.
     *
     * @param puzzle O objeto Puzzle a ser inserido
     * @suspend Esta função é assíncrona e deve ser chamada em uma coroutine
     *
     * 💡 ESTRATÉGIA DE CONFLITO:
     * - OnConflictStrategy.REPLACE: Se já existir um puzzle com o mesmo ID,
     *   substitui o antigo pelo novo (útil para sincronização)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(puzzle: Puzzle)

    /**
     * ✏️ ATUALIZAR PUZZLE EXISTENTE
     *
     * Modifica um puzzle que já existe no banco.
     * Usado para alterar título, dicas, status de resolução, tentativas, etc.
     *
     * @param puzzle O puzzle com dados atualizados (o ID deve corresponder ao existente)
     * @suspend Função assíncrona
     *
     * 💡 COMO FUNCIONA:
     * - O Room identifica o puzzle pelo ID
     * - Atualiza apenas os campos que mudaram
     */
    @Update
    suspend fun update(puzzle: Puzzle)

    /**
     * 🗑️ DELETAR PUZZLE
     *
     * Remove permanentemente um puzzle do banco de dados.
     *
     * @param puzzle O puzzle a ser deletado (identifica pelo ID)
     * @suspend Função assíncrona
     */
    @Delete
    suspend fun delete(puzzle: Puzzle)

    /**
     * 🔍 BUSCAR UM PUZZLE ESPECÍFICO POR ID
     *
     * Retorna um único puzzle baseado no seu ID.
     *
     * @param id O identificador único do puzzle
     * @return Flow<Puzzle> Fluxo reativo que emite o puzzle sempre que ele mudar
     *
     * 💡 FLOW:
     * - Diferente de suspend, o Flow continua "observando" mudanças
     * - Se o puzzle for atualizado no banco, o Flow emite o novo valor automaticamente
     * - Útil para telas de detalhes que precisam atualizar em tempo real
     *
     * 📝 SQL GERADO:
     * SELECT * FROM puzzle WHERE id = :id
     */
    @Query("SELECT * FROM puzzle WHERE id = :id")
    fun getPuzzle(id: Int): Flow<Puzzle>

    /**
     * 📋 BUSCAR TODOS OS PUZZLES
     *
     * Retorna a lista completa de puzzles ordenada do mais recente ao mais antigo.
     *
     * @return Flow<List<Puzzle>> Fluxo que emite a lista sempre que há mudanças
     *
     * 💡 ORDENAÇÃO:
     * - ORDER BY id DESC: Ordena por ID de forma decrescente
     * - DESC = Descending (decrescente): [5, 4, 3, 2, 1]
     * - Puzzles mais novos (ID maior) aparecem primeiro
     *
     * 📝 SQL GERADO:
     * SELECT * FROM puzzle ORDER BY id DESC
     */
    @Query("SELECT * FROM puzzle ORDER BY id DESC")
    fun getAllPuzzles(): Flow<List<Puzzle>>

    /**
     * 🏆 BUSCAR RANKING DE PUZZLES RESOLVIDOS
     *
     * Retorna apenas puzzles resolvidos, ordenados por desempenho:
     * 1º critério: Tempo limite (menor tempo = melhor)
     * 2º critério: Número de tentativas (menos tentativas = melhor)
     *
     * @return Flow<List<Puzzle>> Fluxo com a lista ranqueada
     *
     * 💡 LÓGICA DO RANKING:
     * - WHERE solved = 1: Filtra apenas puzzles resolvidos (solved = true)
     * - ORDER BY time_limit_sec ASC: Ordena por tempo (ASC = crescente, menor primeiro)
     * - ORDER BY attempts ASC: Em caso de empate no tempo, ordena por tentativas
     *
     * 📊 EXEMPLO DE RESULTADO:
     * 1º lugar: Puzzle resolvido em 60s com 1 tentativa
     * 2º lugar: Puzzle resolvido em 60s com 3 tentativas
     * 3º lugar: Puzzle resolvido em 120s com 2 tentativas
     *
     * 📝 SQL GERADO:
     * SELECT * FROM puzzle WHERE solved = 1 ORDER BY time_limit_sec ASC, attempts ASC
     */
    @Query("SELECT * FROM puzzle WHERE solved = 1 ORDER BY time_limit_sec ASC, attempts ASC")
    fun getRanking(): Flow<List<Puzzle>>
}