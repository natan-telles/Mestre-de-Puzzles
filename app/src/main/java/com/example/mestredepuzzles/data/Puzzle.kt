package com.example.mestredepuzzles.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 📦 MODELO DE DADOS - ENTIDADE PUZZLE
 *
 * Esta classe representa um puzzle (enigma) do aplicativo "Mestre de Puzzles da Mansão".
 * É uma data class do Kotlin que funciona como uma tabela no banco de dados SQLite através do Room.
 *
 * 🎯 PROPÓSITO:
 * - Define a estrutura de dados para armazenar puzzles/enigmas
 * - Cada puzzle pode ter um título, dicas, tempo limite e status de resolução
 * - O Room (biblioteca de banco de dados do Android) converte esta classe em uma tabela SQLite
 *
 * 📚 CONCEITOS IMPORTANTES:
 * - @Entity: Marca esta classe como uma tabela do banco de dados
 * - data class: Tipo especial de classe em Kotlin otimizada para armazenar dados
 * - Nullable (?): Indica que o valor pode ser nulo (opcional)
 */
@Entity(tableName = "puzzle") // 🏷️ Nome da tabela no banco de dados será "puzzle"
data class Puzzle(
    /**
     * 🔑 ID DO PUZZLE
     * - Chave primária da tabela (identifica cada puzzle de forma única)
     * - autoGenerate = true: O banco gera automaticamente um novo ID ao inserir
     * - Valor padrão = 0: Ao criar um novo puzzle, o ID é 0 até ser salvo no banco
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * 📝 TÍTULO DO PUZZLE
     * - Campo obrigatório (não pode ser nulo)
     * - Representa o nome ou descrição principal do enigma
     * - Exemplo: "O Mistério da Porta Trancada"
     */
    @ColumnInfo(name = "title")
    val title: String,

    /**
     * 💡 DICAS DO PUZZLE
     * - Três dicas opcionais para ajudar o jogador a resolver o enigma
     * - São nullable (String?) = podem ser nulas se não fornecidas
     * - Valor padrão = null: Se não fornecidas, ficam vazias
     * - Exemplo: "Procure debaixo do tapete", "A chave está perto da janela"
     */
    @ColumnInfo(name = "hint1")
    val hint1: String? = null,

    @ColumnInfo(name = "hint2")
    val hint2: String? = null,

    @ColumnInfo(name = "hint3")
    val hint3: String? = null,

    /**
     * ⏱️ TEMPO LIMITE (em segundos)
     * - Campo opcional que define quanto tempo o jogador tem para resolver
     * - Int? = pode ser nulo se não houver limite de tempo
     * - Exemplo: 300 = 5 minutos (300 segundos)
     */
    @ColumnInfo(name = "time_limit_sec")
    val timeLimitSec: Int? = null,

    /**
     * ✅ STATUS DE RESOLUÇÃO
     * - Indica se o puzzle já foi resolvido pelo jogador
     * - Boolean: true = resolvido, false = pendente
     * - Valor padrão = false: Um novo puzzle começa como não resolvido
     * - Room converte Boolean para INTEGER no banco (0 = false, 1 = true)
     */
    @ColumnInfo(name = "solved")
    val solved: Boolean = false,

    /**
     * 🎯 NÚMERO DE TENTATIVAS
     * - Contador de quantas vezes o jogador tentou resolver o puzzle
     * - Usado para o sistema de ranking (menos tentativas = melhor)
     * - Valor padrão = 0: Começa sem tentativas
     */
    @ColumnInfo(name = "attempts")
    val attempts: Int = 0
)