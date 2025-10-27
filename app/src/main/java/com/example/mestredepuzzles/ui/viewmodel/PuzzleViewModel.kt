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
 * Define o estado da interface do usuário (UI State) para a tela principal.
 */
data class PuzzleUiState(
    val puzzleList: List<Puzzle> = emptyList(),
    val rankingList: List<Puzzle> = emptyList()
)

class PuzzleViewModel(private val puzzleRepository: PuzzleRepository) : ViewModel() {

    val uiState: StateFlow<PuzzleUiState> = combine(
        puzzleRepository.getAllPuzzles(),
        puzzleRepository.getRanking()
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

    fun addPuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.insert(puzzle)
        }
    }

    fun updatePuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.update(puzzle)
        }
    }

    fun deletePuzzle(puzzle: Puzzle) {
        viewModelScope.launch {
            puzzleRepository.delete(puzzle)
        }
    }

    fun markPuzzleAsSolved(puzzle: Puzzle, attempts: Int) {
        val updatedPuzzle = puzzle.copy(solved = true, attempts = attempts)
        viewModelScope.launch {
            puzzleRepository.update(updatedPuzzle)
        }
    }
}

class PuzzleViewModelFactory(private val repository: PuzzleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PuzzleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PuzzleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
