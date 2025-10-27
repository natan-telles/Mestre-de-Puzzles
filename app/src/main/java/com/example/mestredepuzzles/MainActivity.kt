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
// Importações das Telas e ViewModel
import com.example.mestredepuzzles.ui.PuzzleDetailScreen
import com.example.mestredepuzzles.ui.PuzzleListScreen
import com.example.mestredepuzzles.ui.viewmodel.PuzzleViewModel
import com.example.mestredepuzzles.ui.viewmodel.PuzzleViewModelFactory
import com.example.mestredepuzzles.ui.theme.MestreDePuzzlesTheme // Ajuste se seu tema estiver em outro local

// Definição das Rotas de Navegação (melhoria da organização)
object PuzzlesDestinations {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail/{puzzleId}" // Rota com argumento para ID
    const val ADD_ROUTE = "detail/0" // Reutiliza a rota de detalhe com ID 0 para adicionar novo

    fun createDetailRoute(puzzleId: Int) = "detail/$puzzleId"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Obtém o Repositório do Container da Aplicação
        val repository = (application as MestreDePuzzlesApplication).container.puzzleRepository
        val viewModelFactory = PuzzleViewModelFactory(repository)

        setContent {
            MestreDePuzzlesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // 2. Instancia o ViewModel
                    val viewModel: PuzzleViewModel = viewModel(factory = viewModelFactory)
                    val uiState by viewModel.uiState.collectAsState()

                    NavHost(
                        navController = navController,
                        startDestination = PuzzlesDestinations.HOME_ROUTE
                    ) {
                        // Rota 1: Tela Principal (Lista e Ranking)
                        composable(PuzzlesDestinations.HOME_ROUTE) {
                            PuzzleListScreen(
                                uiState = uiState,
                                // Ação: Navegar para detalhes/edição
                                onPuzzleClick = { puzzleId ->
                                    navController.navigate(PuzzlesDestinations.createDetailRoute(puzzleId))
                                },
                                // Ação: Navegar para adição (ID 0)
                                onAddPuzzle = {
                                    navController.navigate(PuzzlesDestinations.ADD_ROUTE)
                                }
                            )
                        }

                        // Rota 2: Tela de Adição/Edição/Detalhes
                        composable(
                            route = PuzzlesDestinations.DETAIL_ROUTE,
                            arguments = listOf(navArgument("puzzleId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val puzzleId = backStackEntry.arguments?.getInt("puzzleId") ?: 0

                            PuzzleDetailScreen(
                                viewModel = viewModel,
                                puzzleId = puzzleId,
                                navigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}