package com.example.mestredepuzzles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
// Importações da Camada de Dados
import com.example.mestredepuzzles.data.Puzzle
import com.example.mestredepuzzles.data.PuzzleRepository
// Coroutines
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 📊 UI STATE - ESTADO DA INTERFACE DO USUÁRIO
 *
 * Esta data class representa TODO o estado da interface que pode mudar ao longo do tempo.
 * É o "contrato" entre o ViewModel e as telas (Composables).
 *
 * 🎯 PROPÓSITO:
 * - Centralizar todos os dados que a UI precisa exibir
 * - Facilitar recomposições automáticas (quando dados mudam, UI atualiza)
 * - Tornar o estado previsível e fácil de testar
 *
 * 💡 PADRÃO UDF (Unidirectional Data Flow):
 * Dados fluem em uma única direção: ViewModel → UI State → Composables
 *
 * @property puzzleList Lista completa de todos os puzzles (para tela principal)
 * @property rankingList Lista filtrada de puzzles resolvidos, ordenados (para ranking)
 */
data class PuzzleUiState(
    val puzzleList: List<Puzzle> = emptyList(),  // 📋 Lista vazia por padrão (antes de carregar do banco)
    val rankingList: List<Puzzle> = emptyList()   // 🏆 Ranking vazio por padrão
)

/**
 * 🧠 VIEWMODEL - LÓGICA DE NEGÓCIO E GERENCIAMENTO DE ESTADO
 *
 * O ViewModel é o "cérebro" da aplicação que:
 * - Armazena e gerencia o estado da UI
 * - Expõe dados para as telas através de StateFlow
 * - Executa operações de negócio (adicionar, atualizar, deletar puzzles)
 * - Sobrevive a mudanças de configuração (rotação de tela, etc)
 *
 * 🎯 PROPÓSITO:
 * - Separar lógica de negócio da UI (Composables só exibem, não processam)
 * - Gerenciar operações assíncronas (acesso ao banco de dados)
 * - Manter estado consistente durante todo o ciclo de vida da Activity
 *
 * 📚 PADRÃO MVVM (Model-View-ViewModel):
 * Model (Repository/DAO) ← ViewModel (este arquivo) ← View (Composables)
 *
 * 🔄 CICLO DE VIDA:
 * - ViewModel sobrevive a recriações da Activity (ex: rotação de tela)
 * - É destruído apenas quando o usuário sai do app permanentemente
 * - viewModelScope: Escopo de coroutines atrelado ao ciclo de vida do ViewModel
 *
 * @param puzzleRepository Repositório que fornece acesso aos dados
 */
class PuzzleViewModel(private val puzzleRepository: PuzzleRepository) : ViewModel() {

    /**
     * 🌊 STATE FLOW - ESTADO OBSERVÁVEL DA UI
     *
     * uiState é um StateFlow que emite o estado atual da UI.
     * Composables observam este StateFlow e recompõem automaticamente quando muda.
     *
     * 💡 COMO FUNCIONA:
     * 1. combine() combina dois Flows (getAllPuzzles + getRanking)
     * 2. Sempre que qualquer um dos Flows emite novo valor, combine executa
     * 3. Cria um novo PuzzleUiState com os dados atualizados
     * 4. stateIn() converte o Flow em StateFlow (Flow com valor inicial)
     *
     * 🔧 PARÂMETROS DO stateIn():
     *
     * @param scope viewModelScope
     *        Escopo de coroutines que cancela automaticamente quando ViewModel é destruído
     *
     * @param started SharingStarted.WhileSubscribed(5000L)
     *        Estratégia de compartilhamento:
     *        - WhileSubscribed: Fica ativo apenas enquanto há observadores (Composables)
     *        - 5000L: Espera 5 segundos após último observador sair antes de parar
     *        - Economiza recursos: não busca dados se ninguém está olhando
     *
     * @param initialValue PuzzleUiState()
     *        Valor inicial (listas vazias) enquanto carrega dados do banco
     *
     * 📊 EXEMPLO DE FLUXO:
     * Banco muda → Flow emite → combine() processa → StateFlow atualiza → UI recompõe
     */
    val uiState: StateFlow<PuzzleUiState> = combine(
        puzzleRepository.getAllPuzzles(),  // 📋 Flow 1: Todos os puzzles
        puzzleRepository.getRanking()      // 🏆 Flow 2: Ranking
    ) { allPuzzles, rankingPuzzles ->
        // 🔄 Esta lambda executa sempre que qualquer Flow emite novo valor
        PuzzleUiState(
            puzzleList = allPuzzles,
            rankingList = rankingPuzzles
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PuzzleUiState()
    )

    // ═══════════════════════════════════════════════════════════════
    // AÇÕES DO USUÁRIO (CHAMADAS PELOS COMPOSABLES)
    // ═══════════════════════════════════════════════════════════════

    /**
     * ➕ ADICIONAR NOVO PUZZLE
     *
     * Chamado quando o usuário cria um novo puzzle na tela de detalhes.
     *
     * @param puzzle Novo puzzle a ser adicionado (com id = 0, será auto-gerado)
     *
     * 💡 FLUXO:
     * 1. UI chama addPuzzle()
     * 2. viewModelScope.launch inicia coroutine
     * 3. Repository insere no banco
     * 4. Room detecta mudança
     * 5. Flow getAllPuzzles() emite nova lista
     * 6. uiState atualiza automaticamente
     * 7. UI recompõe com novo puzzle na lista
     */
    fun addPuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.insert(puzzle)
        }
    }

    /**
     * ✏️ ATUALIZAR PUZZLE EXISTENTE
     *
     * Chamado quando o usuário edita um puzzle na tela de detalhes.
     *
     * @param puzzle Puzzle com dados modificados (mesmo ID)
     *
     * 💡 USOS COMUNS:
     * - Editar título ou dicas
     * - Mudar tempo limite
     * - Marcar como resolvido
     */
    fun updatePuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.update(puzzle)
        }
    }

    /**
     * 🗑️ DELETAR PUZZLE
     *
     * Chamado quando o usuário clica no botão de deletar na tela de detalhes.
     *
     * @param puzzle Puzzle a ser removido permanentemente
     *
     * ⚠️ ATENÇÃO:
     * Operação irreversível! A UI deve pedir confirmação antes de chamar.
     */
    fun deletePuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.delete(puzzle)
        }
    }

    /**
     * ✅ MARCAR PUZZLE COMO RESOLVIDO
     *
     * Chamado quando o jogador completa um puzzle com sucesso.
     * Atualiza o status para resolvido e registra o número de tentativas.
     *
     * @param puzzle Puzzle que foi resolvido
     * @param attempts Número de tentativas que o jogador precisou
     *
     * 💡 LÓGICA:
     * 1. Cria cópia do puzzle com solved=true e attempts atualizados
     * 2. Usa copy() (função de data class) para criar nova instância
     * 3. Salva no banco através do Repository
     * 4. Puzzle aparecerá automaticamente no ranking
     */
    fun markPuzzleAsSolved(puzzle: Puzzle, attempts: Int) {
        val updatedPuzzle = puzzle.copy(solved = true, attempts = attempts)
        viewModelScope.launch {
            puzzleRepository.update(updatedPuzzle)
        }
    }
}

/**
 * 🏭 FACTORY - FÁBRICA DE VIEWMODELS
 *
 * O ViewModelProvider precisa de uma Factory para criar ViewModels com parâmetros customizados.
 * Por padrão, ViewModels só podem ter construtor vazio, mas precisamos passar o Repository.
 *
 * 🎯 PROPÓSITO:
 * - Permitir injeção de dependências no ViewModel (passar o Repository)
 * - Criar instâncias do ViewModel com os parâmetros corretos
 *
 * 💡 USO:
 * val viewModelFactory = PuzzleViewModelFactory(repository)
 * val viewModel: PuzzleViewModel = viewModel(factory = viewModelFactory)
 *
 * @param repository Repositório que será injetado no ViewModel
 */
class PuzzleViewModelFactory(private val repository: PuzzleRepository) : ViewModelProvider.Factory {
    /**
     * 🔨 CRIAR INSTÂNCIA DO VIEWMODEL
     *
     * Método chamado pelo sistema Android para criar o ViewModel.
     *
     * @param modelClass Classe do ViewModel a ser criada
     * @return Instância do ViewModel com Repository injetado
     * @throws IllegalArgumentException se tentar criar ViewModel desconhecido
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se está pedindo para criar PuzzleViewModel
        if (modelClass.isAssignableFrom(PuzzleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PuzzleViewModel(repository) as T  // Cria com Repository injetado
        }
        // Se for outra classe, erro
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
