package com.example.mestredepuzzles.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// Importações da Camada de Dados/Lógica
import com.example.mestredepuzzles.data.Puzzle
import com.example.mestredepuzzles.ui.viewmodel.PuzzleViewModel

// 🎃 Paleta personalizada (mesma da tela de lista)
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
fun PuzzleDetailScreen(
    viewModel: PuzzleViewModel,
    puzzleId: Int,
    navigateBack: () -> Unit
) {
    val isNewPuzzle = puzzleId == 0
    val uiState = viewModel.uiState.collectAsState().value
    val existingPuzzle = uiState.puzzleList.firstOrNull { it.id == puzzleId }

    // Estados Locais
    var title by remember(puzzleId) { mutableStateOf(existingPuzzle?.title ?: "") }
    var hint1 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint1 ?: "") }
    var hint2 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint2 ?: "") }
    var hint3 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint3 ?: "") }
    var timeLimitSec by remember(puzzleId) { mutableStateOf(existingPuzzle?.timeLimitSec?.toString() ?: "") }
    var isSolved by remember(puzzleId) { mutableStateOf(existingPuzzle?.solved ?: false) }
    var attempts by remember(puzzleId) { mutableStateOf(existingPuzzle?.attempts?.toString() ?: "") }

    MaterialTheme(colorScheme = HalloweenColors) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (isNewPuzzle) "🎃 Novo Desafio da Mansão" else "🧩 Editar Puzzle Assombrado",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        if (!isNewPuzzle && existingPuzzle != null) {
                            IconButton(onClick = {
                                viewModel.deletePuzzle(existingPuzzle)
                                navigateBack()
                            }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Deletar Puzzle",
                                    tint = HalloweenColors.tertiary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = HalloweenColors.primary
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
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
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isNewPuzzle)
                            "Crie um novo enigma sombrio 👻"
                        else
                            "Modifique o desafio e mantenha a maldição viva 🕸️",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = HalloweenColors.onSurface,
                            textAlign = TextAlign.Center
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Campos
                    ThemedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Título do Puzzle*"
                    )
                    ThemedTextField(value = hint1, onValueChange = { hint1 = it }, label = "Dica 1")
                    ThemedTextField(value = hint2, onValueChange = { hint2 = it }, label = "Dica 2")
                    ThemedTextField(value = hint3, onValueChange = { hint3 = it }, label = "Dica 3")

                    ThemedTextField(
                        value = timeLimitSec,
                        onValueChange = { timeLimitSec = it.filter { c -> c.isDigit() } },
                        label = "Tempo Limite (segundos)",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSolved,
                            onCheckedChange = { isSolved = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = HalloweenColors.primary,
                                uncheckedColor = HalloweenColors.secondary
                            )
                        )
                        Text(
                            "Resolvido",
                            color = HalloweenColors.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (isSolved) {
                        ThemedTextField(
                            value = attempts,
                            onValueChange = { attempts = it.filter { c -> c.isDigit() } },
                            label = "Número de Tentativas",
                            keyboardType = KeyboardType.Number
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (title.isBlank()) return@Button
                            val puzzleToSave = Puzzle(
                                id = existingPuzzle?.id ?: 0,
                                title = title,
                                hint1 = hint1.takeIf { it.isNotBlank() },
                                hint2 = hint2.takeIf { it.isNotBlank() },
                                hint3 = hint3.takeIf { it.isNotBlank() },
                                timeLimitSec = timeLimitSec.toIntOrNull(),
                                solved = isSolved,
                                attempts = attempts.toIntOrNull() ?: 0
                            )
                            if (isNewPuzzle) viewModel.addPuzzle(puzzleToSave)
                            else viewModel.updatePuzzle(puzzleToSave)
                            navigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HalloweenColors.secondary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            if (isNewPuzzle) "💀 Criar Puzzle" else "🕷️ Atualizar Puzzle",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// 🧙‍♂️ Campo de texto estilizado com tema de Halloween
@Composable
fun ThemedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = HalloweenColors.primary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                BorderStroke(1.dp, HalloweenColors.secondary),
                shape = MaterialTheme.shapes.medium
            ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HalloweenColors.primary,
            unfocusedBorderColor = HalloweenColors.secondary,
            focusedLabelColor = HalloweenColors.primary,
            cursorColor = HalloweenColors.primary,
            focusedTextColor = HalloweenColors.onSurface,
            unfocusedTextColor = HalloweenColors.onSurface
        )
    )
}
