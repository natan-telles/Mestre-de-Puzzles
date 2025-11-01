package com.example.mestredepuzzles.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mestredepuzzles.data.Puzzle
import com.example.mestredepuzzles.ui.viewmodel.PuzzleUiState

// 🎃 Paleta personalizada de Halloween
private val HalloweenColors = darkColorScheme(
    primary = Color(0xFFFF9800),         // Laranja abóbora
    onPrimary = Color.Black,
    secondary = Color(0xFF8E24AA),       // Roxo místico
    onSecondary = Color.White,
    tertiary = Color(0xFFB71C1C),        // Vermelho escuro
    background = Color(0xFF0D0D0D),      // Preto quase puro
    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFFFF3E0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleListScreen(
    uiState: PuzzleUiState,
    onPuzzleClick: (Int) -> Unit,
    onAddPuzzle: () -> Unit
) {
    MaterialTheme(colorScheme = HalloweenColors) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "🎃 Mestre de Puzzles da Mansão Assombrada",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = HalloweenColors.primary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddPuzzle,
                    containerColor = HalloweenColors.secondary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Adicionar Puzzle")
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            // Fundo com gradiente de Halloween 👻
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D0D0D),
                                Color(0xFF2C003E),
                                Color(0xFF3D155F)
                            )
                        )
                    )
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // 👑 Ranking
                    Text(
                        text = "👑 Ranking dos Desafios Superados (${uiState.rankingList.size})",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = HalloweenColors.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (uiState.rankingList.isEmpty()) {
                        Text(
                            text = "Nenhum puzzle resolvido ainda... 🕸️\nA escuridão aguarda seus primeiros passos!",
                            style = MaterialTheme.typography.bodyMedium.copy(color = HalloweenColors.onSurface),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                        )
                    } else {
                        RankingDisplay(uiState.rankingList)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // 🧩 Lista de puzzles
                    Text(
                        text = "🧩 Todos os Puzzles (${uiState.puzzleList.size})",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = HalloweenColors.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 1000.dp)
                    ) {
                        items(uiState.puzzleList) { puzzle ->
                            PuzzleListItem(
                                puzzle = puzzle,
                                onClick = { onPuzzleClick(puzzle.id) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
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
            val (emoji, color) = when (index) {
                0 -> "🥇" to HalloweenColors.primary
                1 -> "🥈" to HalloweenColors.secondary
                else -> "🥉" to HalloweenColors.tertiary
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(BorderStroke(2.dp, color), shape = MaterialTheme.shapes.medium),
                colors = CardDefaults.cardColors(containerColor = HalloweenColors.surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$emoji ${index + 1}º",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = color,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.width(60.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = puzzle.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = HalloweenColors.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Tentativas: ${puzzle.attempts}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray
                            )
                        )
                    }
                    val time = puzzle.timeLimitSec ?: 0
                    Text(
                        text = "⏱️ ${time}s",
                        style = MaterialTheme.typography.titleMedium.copy(color = color)
                    )
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
            .border(BorderStroke(1.dp, HalloweenColors.secondary), shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(
            containerColor = if (puzzle.solved)
                Color(0xFF1B5E20).copy(alpha = 0.2f) // Verde escuro translúcido
            else
                HalloweenColors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = HalloweenColors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = if (puzzle.solved)
                        "🧙‍♂️ Status: RESOLVIDO (${puzzle.attempts} tentativas)"
                    else
                        "👻 Status: PENDENTE",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (puzzle.solved)
                            Color(0xFFFFD54F)
                        else
                            HalloweenColors.error
                    )
                )
            }
            if (!puzzle.solved) {
                Text(
                    text = "⏳ ${puzzle.timeLimitSec ?: "N/A"}s",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                )
            }
        }
    }
}
