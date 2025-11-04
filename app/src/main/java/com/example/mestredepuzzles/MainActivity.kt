package com.example.mestredepuzzles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mestredepuzzles.ui.PuzzleDetailScreen
import com.example.mestredepuzzles.ui.PuzzleListScreen
import com.example.mestredepuzzles.ui.viewmodel.PuzzleViewModel
import com.example.mestredepuzzles.ui.viewmodel.PuzzleViewModelFactory
import com.example.mestredepuzzles.ui.theme.MestreDePuzzlesTheme

/**
 * 🗺️ ROTAS DE NAVEGAÇÃO
 *
 * Centraliza endereços das telas (evita strings espalhadas no código).
 */
object PuzzlesDestinations {
    const val HOME_ROUTE = "home"                           // Tela principal
    const val DETAIL_ROUTE = "detail/{puzzleId}"            // Tela de detalhes/edição
    const val ADD_ROUTE = "detail/0"                        // Cria novo (ID=0)

    // Gera rota dinâmica: "detail/123"
    fun createDetailRoute(puzzleId: Int) = "detail/$puzzleId"
}

/**
 * 🎬 ACTIVITY PRINCIPAL - Single Activity Architecture
 *
 * Toda navegação via Compose Navigation (troca Composables, não Activities).
 * Configura navegação, ViewModel compartilhado e tema.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtém Repository e cria ViewModelFactory
        val repository = (application as MestreDePuzzlesApplication).container.puzzleRepository
        val viewModelFactory = PuzzleViewModelFactory(repository)
        // Configura UI com Jetpack Compose
        setContent {
            // Aplica tema visual Halloween
            MestreDePuzzlesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavController gerencia pilha de navegação
                    val navController = rememberNavController()

                    // ViewModel compartilhado entre telas (sobrevive rotação)
                    val viewModel: PuzzleViewModel = viewModel(factory = viewModelFactory)

                    // Observa estado reativo do ViewModel
                    val uiState by viewModel.uiState.collectAsState()

                    // Grafo de navegação (define rotas e telas)
                    NavHost(
                        navController = navController,
                        startDestination = PuzzlesDestinations.HOME_ROUTE
                    ) {
                        // ──── ROTA 1: Tela Principal ────
                        composable(PuzzlesDestinations.HOME_ROUTE) {
                            PuzzleListScreen(
                                uiState = uiState,
                                // Navega para detalhes ao clicar em puzzle
                                onPuzzleClick = { puzzleId ->
                                    navController.navigate(PuzzlesDestinations.createDetailRoute(puzzleId))
                                },
                                // Navega para criar novo (ID=0)
                                onAddPuzzle = {
                                    navController.navigate(PuzzlesDestinations.ADD_ROUTE)
                                }
                            )
                        }

                        // ──── ROTA 2: Tela de Detalhes/Edição ────
                        composable(
                            route = PuzzlesDestinations.DETAIL_ROUTE,
                            arguments = listOf(
                                // Define argumento "puzzleId" como Int
                                navArgument("puzzleId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            // Extrai ID da rota: "detail/42" → 42
                            val puzzleId = backStackEntry.arguments?.getInt("puzzleId") ?: 0

                            PuzzleDetailScreen(
                                viewModel = viewModel,
                                puzzleId = puzzleId,
                                // Volta para tela anterior
                                navigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}