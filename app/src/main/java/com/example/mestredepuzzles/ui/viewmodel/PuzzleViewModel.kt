package com.example.mestredepuzzles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mestredepuzzles.data.Puzzle
import com.example.mestredepuzzles.data.PuzzleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 📊 UI STATE - Estado reativo da interface
 *
 * Centraliza dados observáveis pela UI. Padrão UDF (Unidirectional Data Flow).
 */
data class PuzzleUiState(
    val puzzleList: List<Puzzle> = emptyList(),    // 📋 Lista completa
    val rankingList: List<Puzzle> = emptyList()    // 🏆 Ranking de resolvidos
)

/**
 * 🧠 VIEWMODEL - Gerencia lógica de negócio e estado da UI
 *
 * Padrão MVVM: Model (Repo) ← ViewModel ← View (Composables)
 * Sobrevive a rotações de tela, destruído ao sair do app.
 */
class PuzzleViewModel(private val puzzleRepository: PuzzleRepository) : ViewModel() {

    /**
     * 🌊 StateFlow que combina dois Flows e emite PuzzleUiState
     *
     * combine() executa quando qualquer Flow muda → UI recompõe automaticamente
     * WhileSubscribed(5000L) = fica ativo 5s após último observador sair
     */
    val uiState: StateFlow<PuzzleUiState> = combine(
        puzzleRepository.getAllPuzzles(),  // Flow 1: Todos os puzzles
        puzzleRepository.getRanking()      // Flow 2: Ranking
    ) { allPuzzles, rankingPuzzles ->
        PuzzleUiState(
            puzzleList = allPuzzles,
            rankingList = rankingPuzzles
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PuzzleUiState()
    )

    // ──── AÇÕES DO USUÁRIO (chamadas pelos Composables) ────

    // ➕ Adiciona novo puzzle (ID=0, banco gera ID real)
    fun addPuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.insert(puzzle)
        }
    }

    // ✏️ Atualiza puzzle existente (mantém o mesmo ID)
    fun updatePuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.update(puzzle)
        }
    }

    // 🗑️ Deleta puzzle (irreversível!)
    fun deletePuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.delete(puzzle)
        }
    }

    // ✅ Marca como resolvido e registra tentativas (aparece no ranking)
    fun markPuzzleAsSolved(puzzle: Puzzle, attempts: Int) {
        val updatedPuzzle = puzzle.copy(solved = true, attempts = attempts)
        viewModelScope.launch {
            puzzleRepository.update(updatedPuzzle)
        }
    }
}

/**
 * 🏭 FACTORY - Cria ViewModels com dependências customizadas
 *
 * Permite injetar Repository no construtor do ViewModel.
 * Uso: viewModel(factory = PuzzleViewModelFactory(repository))
 */
class PuzzleViewModelFactory(private val repository: PuzzleRepository) : ViewModelProvider.Factory {
    // Cria ViewModel injetando o Repository
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PuzzleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PuzzleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
