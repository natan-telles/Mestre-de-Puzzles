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
import com.example.mestredepuzzles.ui.theme.MestreDePuzzlesTheme

/**
 * 🗺️ DESTINOS DE NAVEGAÇÃO
 *
 * Object que centraliza todas as rotas de navegação do app.
 * É uma boa prática manter rotas organizadas em um único lugar.
 *
 * 🎯 PROPÓSITO:
 * - Evitar strings hardcoded espalhadas pelo código
 * - Facilitar manutenção (mudar rota em um só lugar)
 * - Prevenir erros de digitação em rotas
 *
 * 💡 CONCEITO DE ROTAS:
 * Rotas são como "endereços" de telas no app.
 * Similar a URLs em sites: /home, /detail/123, etc.
 */
object PuzzlesDestinations {
    /**
     * 🏠 ROTA DA TELA PRINCIPAL
     *
     * Tela que mostra a lista de puzzles e o ranking.
     * É a primeira tela exibida quando o app abre.
     */
    const val HOME_ROUTE = "home"

    /**
     * 📄 ROTA DA TELA DE DETALHES/EDIÇÃO
     *
     * {puzzleId} é um argumento dinâmico (placeholder).
     * Exemplos de rotas geradas:
     * - "detail/1" → Editar puzzle com ID 1
     * - "detail/5" → Editar puzzle com ID 5
     * - "detail/0" → Criar novo puzzle
     *
     * 💡 CONVENÇÃO:
     * ID = 0 significa "criar novo" (não existe puzzle com ID 0)
     * ID > 0 significa "editar existente"
     */
    const val DETAIL_ROUTE = "detail/{puzzleId}"

    /**
     * ➕ ROTA PARA ADICIONAR NOVO PUZZLE
     *
     * Reutiliza a tela de detalhes, mas com ID fixo em 0.
     * É uma constante de conveniência para facilitar navegação.
     */
    const val ADD_ROUTE = "detail/0"

    /**
     * 🔨 FUNÇÃO AUXILIAR PARA CRIAR ROTA DINÂMICA
     *
     * Substitui o placeholder {puzzleId} por um ID real.
     *
     * @param puzzleId ID do puzzle a editar
     * @return String da rota completa: "detail/123"
     *
     * 💡 EXEMPLO DE USO:
     * navController.navigate(createDetailRoute(42))
     * // Navega para "detail/42"
     */
    fun createDetailRoute(puzzleId: Int) = "detail/$puzzleId"
}

/**
 * 🎬 ACTIVITY PRINCIPAL DO APLICATIVO
 *
 * Esta é a única Activity do app (arquitetura Single Activity).
 * Toda a navegação acontece via Jetpack Compose Navigation, trocando Composables.
 *
 * 🎯 PROPÓSITO:
 * - Configurar o sistema de navegação
 * - Inicializar o ViewModel compartilhado
 * - Definir o tema visual do app
 * - Conectar todas as telas (Composables)
 *
 * 📚 CONCEITOS IMPORTANTES:
 * - ComponentActivity: Versão moderna de Activity com suporte a Compose
 * - Single Activity: Um app inteiro com apenas uma Activity (padrão moderno)
 * - Jetpack Compose: Framework declarativo para criar UI (substitui XML)
 *
 * 🔄 CICLO DE VIDA:
 * onCreate() → App inicia → onDestroy() → App fecha
 */
class MainActivity : ComponentActivity() {
    /**
     * 🎬 MÉTODO CHAMADO QUANDO A ACTIVITY É CRIADA
     *
     * Este método executa quando o usuário abre o app ou retorna após minimizá-lo.
     *
     * @param savedInstanceState Estado salvo (para restaurar após rotação, etc)
     *                           Null na primeira criação
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ═══════════════════════════════════════════════════════════════
        // 1️⃣ OBTER O REPOSITÓRIO E CRIAR VIEWMODEL FACTORY
        // ═══════════════════════════════════════════════════════════════

        /**
         * 🏪 OBTÉM O REPOSITÓRIO DO CONTAINER DA APLICAÇÃO
         *
         * (application as MestreDePuzzlesApplication):
         * - application: Propriedade herdada de ComponentActivity
         * - Cast para nossa classe customizada que tem o container
         *
         * .container.puzzleRepository:
         * - Acessa o container de dependências
         * - Obtém a instância única do Repository
         *
         * 💡 FLUXO:
         * Application → Container → Repository → ViewModel → Composables
         */
        val repository = (application as MestreDePuzzlesApplication).container.puzzleRepository

        /**
         * 🏭 CRIA A FACTORY PARA O VIEWMODEL
         *
         * Necessário porque PuzzleViewModel tem parâmetros no construtor.
         * A Factory sabe como criar o ViewModel com o Repository injetado.
         */
        val viewModelFactory = PuzzleViewModelFactory(repository)

        // ═══════════════════════════════════════════════════════════════
        // 2️⃣ CONFIGURAR A UI COM JETPACK COMPOSE
        // ═══════════════════════════════════════════════════════════════

        /**
         * 🎨 DEFINE O CONTEÚDO DA UI
         *
         * setContent { ... }:
         * - Substitui o antigo setContentView(R.layout.activity_main)
         * - Tudo dentro é código Compose (declarativo)
         * - Bloco é um @Composable lambda
         */
        setContent {
            /**
             * 🎨 APLICA O TEMA VISUAL DO APP
             *
             * MestreDePuzzlesTheme:
             * - Define cores (Halloween: laranja, roxo, preto)
             * - Define tipografia (fontes, tamanhos)
             * - Define shapes (bordas arredondadas, etc)
             * - Componentes filhos herdam essas configurações
             */
            MestreDePuzzlesTheme {
                /**
                 * 📄 SURFACE - CONTAINER BASE
                 *
                 * Surface é como um "canvas" base para desenhar a UI.
                 * Define cor de fundo e preenche toda a tela.
                 *
                 * @modifier fillMaxSize() → Ocupa toda a tela
                 * @color MaterialTheme.colorScheme.background → Cor do tema
                 */
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ═══════════════════════════════════════════════════════════════
                    // 3️⃣ CONFIGURAR NAVEGAÇÃO E VIEWMODEL
                    // ═══════════════════════════════════════════════════════════════

                    /**
                     * 🧭 CONTROLLER DE NAVEGAÇÃO
                     *
                     * NavController gerencia a pilha de navegação.
                     * É como o "diretor" que controla qual tela mostrar.
                     *
                     * 💡 PILHA DE NAVEGAÇÃO:
                     * [Home] → Clica em puzzle → [Home, Detail] → Volta → [Home]
                     *
                     * rememberNavController():
                     * - Cria e "lembra" o controller entre recomposições
                     * - Sobrevive a mudanças de estado, mas não a recriações da Activity
                     */
                    val navController = rememberNavController()

                    /**
                     * 🧠 INSTANCIA O VIEWMODEL
                     *
                     * viewModel(factory = viewModelFactory):
                     * - Cria ou recupera ViewModel existente
                     * - ViewModel sobrevive a rotações de tela
                     * - Usa a Factory para injetar dependências
                     *
                     * 💡 ESCOPO:
                     * ViewModel é escopo da Activity, não da função Composable.
                     * Todas as telas compartilham o mesmo ViewModel.
                     */
                    val viewModel: PuzzleViewModel = viewModel(factory = viewModelFactory)

                    /**
                     * 📊 OBSERVA O ESTADO DA UI
                     *
                     * collectAsState():
                     * - Converte StateFlow em State (Compose)
                     * - Recompõe automaticamente quando uiState muda
                     *
                     * by:
                     * - Delegate que permite acessar uiState.value como uiState
                     * - Sintaxe mais limpa
                     *
                     * ⚠️ NOTA:
                     * uiState não é usado aqui na MainActivity, mas poderia ser
                     * passado para Composables se necessário
                     */
                    val uiState by viewModel.uiState.collectAsState()

                    // ═══════════════════════════════════════════════════════════════
                    // 4️⃣ DEFINIR GRAFO DE NAVEGAÇÃO (ROTAS E TELAS)
                    // ═══════════════════════════════════════════════════════════════

                    /**
                     * 🗺️ GRAFO DE NAVEGAÇÃO
                     *
                     * NavHost é o container que exibe a tela atual baseada na rota.
                     * É como um "switch" que decide qual Composable mostrar.
                     *
                     * @navController Controller que gerencia navegação
                     * @startDestination Rota inicial (tela que abre primeiro)
                     *
                     * 💡 COMO FUNCIONA:
                     * 1. NavHost observa navController.currentDestination
                     * 2. Compara com as rotas definidas em composable { ... }
                     * 3. Exibe o Composable correspondente
                     * 4. Quando navega, troca o Composable exibido
                     */
                    NavHost(
                        navController = navController,
                        startDestination = PuzzlesDestinations.HOME_ROUTE
                    ) {
                        // ───────────────────────────────────────────────────────────
                        // ROTA 1: TELA PRINCIPAL (HOME)
                        // ───────────────────────────────────────────────────────────

                        /**
                         * 🏠 COMPOSABLE DA TELA PRINCIPAL
                         *
                         * Exibido quando a rota atual é "home".
                         * Mostra a lista de puzzles e o ranking.
                         *
                         * @route "home" → Identifica esta tela
                         */
                        composable(PuzzlesDestinations.HOME_ROUTE) {
                            PuzzleListScreen(
                                uiState = uiState,  // 📊 Passa o estado atual
                                /**
                                 * 🔗 CALLBACK: NAVEGAR PARA DETALHES
                                 *
                                 * Chamado quando usuário clica em um puzzle da lista.
                                 *
                                 * @param puzzleId ID do puzzle clicado
                                 *
                                 * 💡 FLUXO:
                                 * Usuário clica → onPuzzleClick(42) → navController.navigate("detail/42")
                                 */
                                onPuzzleClick = { puzzleId ->
                                    navController.navigate(PuzzlesDestinations.createDetailRoute(puzzleId))
                                },
                                /**
                                 * ➕ CALLBACK: NAVEGAR PARA ADICIONAR
                                 *
                                 * Chamado quando usuário clica no botão FAB (➕).
                                 *
                                 * 💡 FLUXO:
                                 * Usuário clica no FAB → onAddPuzzle() → navController.navigate("detail/0")
                                 */
                                onAddPuzzle = {
                                    navController.navigate(PuzzlesDestinations.ADD_ROUTE)
                                }
                            )
                        }

                        // ───────────────────────────────────────────────────────────
                        // ROTA 2: TELA DE DETALHES/EDIÇÃO/ADIÇÃO
                        // ───────────────────────────────────────────────────────────

                        /**
                         * 📄 COMPOSABLE DA TELA DE DETALHES
                         *
                         * Exibido quando a rota é "detail/{puzzleId}".
                         * Serve para 3 propósitos:
                         * 1. Ver detalhes de um puzzle (só visualização)
                         * 2. Editar puzzle existente (puzzleId > 0)
                         * 3. Criar novo puzzle (puzzleId = 0)
                         *
                         * @route "detail/{puzzleId}" → Rota com argumento
                         * @arguments Define tipo e nome do argumento
                         */
                        composable(
                            route = PuzzlesDestinations.DETAIL_ROUTE,
                            arguments = listOf(
                                /**
                                 * 🏷️ DEFINE ARGUMENTO DA ROTA
                                 *
                                 * navArgument("puzzleId"):
                                 * - Nome do argumento (deve corresponder ao {puzzleId} na rota)
                                 *
                                 * type = NavType.IntType:
                                 * - Tipo de dado (Integer)
                                 * - Navigation converte string "42" → Int 42 automaticamente
                                 *
                                 * 💡 OUTROS TIPOS:
                                 * - NavType.StringType → String
                                 * - NavType.BoolType → Boolean
                                 * - NavType.LongType → Long
                                 */
                                navArgument("puzzleId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            /**
                             * 📦 EXTRAI O ARGUMENTO DA ROTA
                             *
                             * backStackEntry:
                             * - Entrada atual na pilha de navegação
                             * - Contém informações sobre a rota (argumentos, etc)
                             *
                             * .arguments?.getInt("puzzleId"):
                             * - Pega o valor do argumento "puzzleId"
                             * - ? → Safe call (retorna null se arguments for null)
                             * - ?: 0 → Se null, usa 0 como padrão
                             *
                             * 💡 EXEMPLOS:
                             * Rota "detail/42" → puzzleId = 42
                             * Rota "detail/0" → puzzleId = 0
                             */
                            val puzzleId = backStackEntry.arguments?.getInt("puzzleId") ?: 0

                            /**
                             * 📄 EXIBE A TELA DE DETALHES
                             *
                             * @viewModel ViewModel compartilhado (acessa dados do banco)
                             * @puzzleId ID do puzzle a editar/criar
                             * @navigateBack Callback para voltar à tela anterior
                             */
                            PuzzleDetailScreen(
                                viewModel = viewModel,
                                puzzleId = puzzleId,
                                /**
                                 * ⬅️ CALLBACK: VOLTAR
                                 *
                                 * Chamado quando usuário clica em "Voltar" ou salva o puzzle.
                                 *
                                 * navController.popBackStack():
                                 * - Remove a tela atual da pilha
                                 * - Retorna à tela anterior (Home)
                                 *
                                 * 💡 PILHA:
                                 * [Home, Detail] → popBackStack() → [Home]
                                 */
                                navigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}