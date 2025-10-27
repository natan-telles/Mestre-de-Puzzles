package com.example.mestredepuzzles.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Importações da Camada de Dados/Lógica
import com.example.mestredepuzzles.data.Puzzle
import com.example.mestredepuzzles.ui.viewmodel.PuzzleUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleListScreen(
    uiState: PuzzleUiState,
    onPuzzleClick: (Int) -> Unit,
    onAddPuzzle: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mestre de Puzzles da Mansão") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPuzzle) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Puzzle")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            // 1. Ranking de Desempenho
            Text(
                text = "Ranking dos Desafios Superados (${uiState.rankingList.size})",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            if (uiState.rankingList.isEmpty()) {
                Text("Nenhum puzzle resolvido ainda. Avance no jogo!")
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                RankingDisplay(uiState.rankingList)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Lista de Puzzles (Todos)
            Text(
                text = "Todos os Puzzles (${uiState.puzzleList.size})",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(uiState.puzzleList) { puzzle ->
                    PuzzleListItem(
                        puzzle = puzzle,
                        onClick = { onPuzzleClick(puzzle.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RankingDisplay(ranking: List<Puzzle>) {
    val topRanked = ranking.take(3)

    Column(modifier = Modifier.fillMaxWidth()) {
        topRanked.forEachIndexed { index, puzzle ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                // Lógica de cores simples para destacar o pódio
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(
                    containerColor = when(index) {
                        0 -> MaterialTheme.colorScheme.primaryContainer
                        1 -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.tertiaryContainer
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}º",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.width(32.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = puzzle.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Tentativas: ${puzzle.attempts}", style = MaterialTheme.typography.bodySmall)
                    }
                    val time = puzzle.timeLimitSec ?: 0
                    Text(text = "${time}s", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
fun PuzzleListItem(puzzle: Puzzle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = puzzle.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (puzzle.solved) "Status: RESOLVIDO (${puzzle.attempts} tentativas)" else "Status: Pendente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (puzzle.solved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            if (!puzzle.solved) {
                Text(
                    text = "Limite: ${puzzle.timeLimitSec ?: "N/A"}s",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}