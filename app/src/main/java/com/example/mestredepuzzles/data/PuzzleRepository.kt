package com.example.mestredepuzzles.data

import kotlinx.coroutines.flow.Flow

/**
 * 🏪 REPOSITÓRIO - CAMADA DE ABSTRAÇÃO DE DADOS
 *
 * O Repositório é uma camada intermediária entre o ViewModel (lógica de UI) e o DAO (banco de dados).
 * Ele encapsula a origem dos dados, tornando o código mais limpo e testável.
 *
 * 🎯 PROPÓSITO:
 * - Fornecer uma API limpa para o ViewModel acessar dados
 * - Ocultar detalhes de implementação (o ViewModel não precisa saber sobre Room)
 * - Facilitar testes (podemos simular o repositório sem banco real)
 * - Centralizar lógica de acesso a dados (se no futuro buscarmos de API, mudamos só aqui)
 *
 * 📚 PADRÃO REPOSITORY:
 * É um padrão de design que separa a lógica de negócio da lógica de acesso a dados.
 *
 * 🔄 FLUXO DE DADOS:
 * UI (Composable) ← ViewModel ← Repository ← DAO ← Banco de Dados SQLite
 *
 * 💡 BENEFÍCIOS:
 * - Se trocarmos Room por outra solução, só mudamos o Repository
 * - Se adicionarmos cache ou API, implementamos aqui sem afetar o ViewModel
 * - Testes ficam mais fáceis (podemos criar um FakeRepository para testes)
 *
 * @param puzzleDao Instância do DAO que faz as operações reais no banco
 */
class PuzzleRepository(private val puzzleDao: PuzzleDao) {

    /**
     * 📋 BUSCAR TODOS OS PUZZLES
     *
     * Retorna um Flow com a lista completa de puzzles.
     *
     * @return Flow<List<Puzzle>> Lista reativa de todos os puzzles
     *
     * 💡 O Flow observa mudanças no banco automaticamente:
     * - Quando um puzzle é adicionado, a lista atualiza sozinha
     * - Quando um puzzle é deletado, a lista atualiza sozinha
     * - Não precisa chamar a função novamente manualmente
     */
    fun getAllPuzzles(): Flow<List<Puzzle>> = puzzleDao.getAllPuzzles()

    /**
     * 🏆 BUSCAR RANKING DE PUZZLES RESOLVIDOS
     *
     * Retorna um Flow com puzzles resolvidos ordenados por desempenho.
     *
     * @return Flow<List<Puzzle>> Lista reativa do ranking
     *
     * 📊 Ordenação:
     * 1. Menor tempo limite primeiro
     * 2. Menor número de tentativas em caso de empate
     */
    fun getRanking(): Flow<List<Puzzle>> = puzzleDao.getRanking()

    /**
     * 🔍 BUSCAR UM PUZZLE ESPECÍFICO
     *
     * Retorna um Flow com um único puzzle baseado no ID.
     *
     * @param id Identificador único do puzzle
     * @return Flow<Puzzle> Puzzle reativo que atualiza se for modificado
     *
     * 💡 USO TÍPICO:
     * Usado na tela de detalhes para exibir e editar um puzzle específico
     */
    fun getPuzzle(id: Int): Flow<Puzzle> = puzzleDao.getPuzzle(id)

    // ═══════════════════════════════════════════════════════════════
    // FUNÇÕES DE ESCRITA (MODIFICAM O BANCO DE DADOS)
    // ═══════════════════════════════════════════════════════════════

    /**
     * ➕ INSERIR NOVO PUZZLE
     *
     * Adiciona um novo puzzle ao banco de dados.
     *
     * @param puzzle O objeto Puzzle a ser salvo
     * @suspend Função assíncrona que deve ser chamada em uma coroutine
     *
     * 💡 EXEMPLO DE USO NO VIEWMODEL:
     * viewModelScope.launch {
     *     repository.insert(novoPuzzle)
     * }
     */
    suspend fun insert(puzzle: Puzzle) = puzzleDao.insert(puzzle)

    /**
     * ✏️ ATUALIZAR PUZZLE EXISTENTE
     *
     * Modifica os dados de um puzzle já salvo.
     *
     * @param puzzle Puzzle com dados atualizados (o ID deve ser o mesmo)
     * @suspend Função assíncrona
     *
     * 💡 USOS COMUNS:
     * - Marcar puzzle como resolvido
     * - Incrementar número de tentativas
     * - Alterar título ou dicas
     */
    suspend fun update(puzzle: Puzzle) = puzzleDao.update(puzzle)

    /**
     * 🗑️ DELETAR PUZZLE
     *
     * Remove permanentemente um puzzle do banco.
     *
     * @param puzzle O puzzle a ser deletado
     * @suspend Função assíncrona
     *
     * ⚠️ ATENÇÃO:
     * Esta operação não pode ser desfeita!
     * Considere adicionar confirmação na UI antes de chamar esta função.
     */
    suspend fun delete(puzzle: Puzzle) = puzzleDao.delete(puzzle)
}