package com.example.mestredepuzzles.ui

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
// Importações da Camada de Dados/Lógica
import com.example.mestredepuzzles.data.Puzzle
import com.example.mestredepuzzles.ui.viewmodel.PuzzleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleDetailScreen(
    viewModel: PuzzleViewModel,
    puzzleId: Int,
    navigateBack: () -> Unit
) {
    // Busca o puzzle se o ID for válido (edição).
    // Coleta o estado global e filtra.
    val isNewPuzzle = puzzleId == 0
    val uiState = viewModel.uiState.collectAsState().value

    // Encontra o puzzle na lista
    val existingPuzzle = uiState.puzzleList.firstOrNull { it.id == puzzleId }

    // --- Estados Locais do Formulário ---
    // Usamos o 'key' para re-inicializar o estado quando o puzzleID muda
    var title by remember(puzzleId) { mutableStateOf(existingPuzzle?.title ?: "") }
    var hint1 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint1 ?: "") }
    var hint2 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint2 ?: "") }
    var hint3 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint3 ?: "") }
    var timeLimitSec by remember(puzzleId) { mutableStateOf(existingPuzzle?.timeLimitSec?.toString() ?: "") }
    var isSolved by remember(puzzleId) { mutableStateOf(existingPuzzle?.solved ?: false) }
    var attempts by remember(puzzleId) { mutableStateOf(existingPuzzle?.attempts?.toString() ?: "") }
    // ------------------------------------

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewPuzzle) "Adicionar Novo Puzzle" else "Detalhes do Puzzle") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    // Botão de Deletar (visíve l apenas na edição)
                    if (!isNewPuzzle && existingPuzzle != null) {
                        IconButton(onClick = {
                            viewModel.deletePuzzle(existingPuzzle!!)
                            navigateBack()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Deletar Puzzle")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Adiciona scroll para evitar overflow
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campos de Texto
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Puzzle*") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = hint1, onValueChange = { hint1 = it }, label = { Text("Dica 1") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = hint2, onValueChange = { hint2 = it }, label = { Text("Dica 2") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = hint3, onValueChange = { hint3 = it }, label = { Text("Dica 3") }, modifier = Modifier.fillMaxWidth())

            // Campo de Número (Tempo Limite)
            OutlinedTextField(
                value = timeLimitSec,
                onValueChange = { timeLimitSec = it.filter { char -> char.isDigit() } },
                label = { Text("Tempo Limite (segundos)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Status de Resolução
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isSolved, onCheckedChange = { isSolved = it })
                Text("Resolvido", modifier = Modifier.padding(start = 8.dp))
            }

            // Campo de Tentativas (visível apenas se resolvido)
            if (isSolved) {
                OutlinedTextField(
                    value = attempts,
                    onValueChange = { attempts = it.filter { char -> char.isDigit() } },
                    label = { Text("Número de Tentativas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Salvar/Atualizar
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

                    if (isNewPuzzle) {
                        viewModel.addPuzzle(puzzleToSave)
                    } else {
                        viewModel.updatePuzzle(puzzleToSave)
                    }
                    navigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(if (isNewPuzzle) "Criar Puzzle" else "Atualizar Puzzle")
            }
        }
    }
}