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

/**
 * 🏠 TELA PRINCIPAL - LISTA DE PUZZLES E RANKING
 *
 * Esta é a tela inicial do aplicativo que exibe:
 * 1. 👑 RANKING: Top 3 puzzles resolvidos (ordenados por desempenho)
 * 2. 🧩 LISTA COMPLETA: Todos os puzzles cadastrados
 * 3. ➕ FAB: Botão flutuante para adicionar novo puzzle
 *
 * 🎯 FUNCIONALIDADES:
 * - Visualizar todos os puzzles com status (resolvido/pendente)
 * - Ver ranking dos melhores desempenhos
 * - Navegar para tela de detalhes ao clicar em puzzle
 * - Criar novo puzzle via botão FAB
 * - Scroll vertical para listas longas
 * - Tema Halloween (laranja, roxo, gradiente escuro)
 *
 * 📚 CONCEITOS DE COMPOSE:
 * - LazyColumn: Lista preguiçosa (renderiza apenas itens visíveis)
 * - Scaffold com FAB: Estrutura Material com botão flutuante
 * - Estado reativo: UI atualiza automaticamente quando dados mudam
 * - Composables reutilizáveis: RankingDisplay e PuzzleListItem
 */

/**
 * 🎨 PALETA DE CORES HALLOWEEN
 *
 * Mesma paleta usada em PuzzleDetailScreen para consistência visual.
 *
 * 💡 CORES DO TEMA:
 * - Laranja: Destaque primário (abóboras de Halloween)
 * - Roxo: Cor secundária (mistério e magia)
 * - Vermelho escuro: Alertas e perigo
 * - Preto profundo: Fundo principal (noite)
 * - Bege claro: Texto sobre fundos escuros
 */
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

/**
 * 🖼️ COMPOSABLE PRINCIPAL: TELA DE LISTA DE PUZZLES
 *
 * Este Composable é a tela inicial do app que exibe:
 * - Ranking dos puzzles resolvidos
 * - Lista completa de todos os puzzles
 * - Botão para adicionar novo puzzle
 *
 * 🎯 RESPONSABILIDADES:
 * - Exibir dados do uiState (recebido do ViewModel)
 * - Gerenciar navegação via callbacks
 * - Aplicar tema visual Halloween
 * - Organizar layout com Scaffold
 *
 * 📚 PARÂMETROS:
 *
 * @param uiState
 *        Estado atual da UI contendo:
 *        - puzzleList: Lista completa de puzzles
 *        - rankingList: Puzzles resolvidos ordenados por desempenho
 *        Vem do ViewModel via StateFlow
 *
 * @param onPuzzleClick
 *        Callback chamado quando usuário clica em um puzzle
 *        Recebe o ID do puzzle clicado
 *        Tipo: (Int) -> Unit
 *        Exemplo: { puzzleId -> navController.navigate("detail/$puzzleId") }
 *
 * @param onAddPuzzle
 *        Callback chamado quando usuário clica no botão FAB (+)
 *        Sem parâmetros, apenas notifica intenção de adicionar
 *        Tipo: () -> Unit
 *        Exemplo: { navController.navigate("detail/0") }
 *
 * 💡 ANOTAÇÕES:
 * @OptIn(ExperimentalMaterial3Api::class)
 * TopAppBar é API experimental (pode mudar em futuras versões do Material 3)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleListScreen(
    uiState: PuzzleUiState,
    onPuzzleClick: (Int) -> Unit,
    onAddPuzzle: () -> Unit
) {
    /**
     * 🎨 APLICA O TEMA HALLOWEEN
     *
     * MaterialTheme com esquema de cores customizado.
     * Todos os componentes filhos herdam essas cores.
     */
    MaterialTheme(colorScheme = HalloweenColors) {
        /**
         * 🏗️ SCAFFOLD - ESTRUTURA BASE DA TELA
         *
         * Fornece estrutura padrão Material Design:
         * - TopBar: Barra superior com título
         * - FAB: Floating Action Button (botão flutuante)
         * - Content: Conteúdo principal (via paddingValues)
         *
         * 💡 VANTAGENS:
         * - Layout consistente e profissional
         * - FAB posicionado automaticamente
         * - Padding gerenciado para não sobrepor elementos
         */
        Scaffold(
            topBar = {
                /**
                 * 🎯 BARRA SUPERIOR (APP BAR)
                 *
                 * Exibe o título do app no topo da tela.
                 * Usa cor laranja de Halloween para destaque.
                 *
                 * 💡 ACESSIBILIDADE:
                 * Sempre inclua título descritivo para leitores de tela.
                 */
                TopAppBar(
                    title = {
                        Text(
                            text = "🎃 Mestre de Puzzles da Mansão Assombrada",
                            fontWeight = FontWeight.Bold,  // Negrito para destaque
                            color = Color.Black            // Contraste com fundo laranja
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = HalloweenColors.primary  // Fundo laranja
                    )
                )
            },
            floatingActionButton = {
                /**
                 * ➕ FLOATING ACTION BUTTON (FAB)
                 *
                 * Botão circular flutuante para ação principal.
                 * Posicionado automaticamente no canto inferior direito.
                 *
                 * 💡 MATERIAL DESIGN:
                 * FAB deve ser usado para ação primária da tela.
                 * Neste caso: adicionar novo puzzle.
                 *
                 * @onClick Chama callback onAddPuzzle
                 * @containerColor Roxo místico (secundário do tema)
                 * @contentColor Branco (ícone)
                 */
                FloatingActionButton(
                    onClick = onAddPuzzle,
                    containerColor = HalloweenColors.secondary,  // Roxo místico
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Adicionar Puzzle"  // Acessibilidade
                    )
                }
            },
            containerColor = Color.Transparent  // Transparente para mostrar gradiente
        ) { paddingValues ->
            /**
             * 📦 BOX - CONTAINER COM FUNDO GRADIENTE
             *
             * Box permite empilhar elementos (gradiente atrás do conteúdo).
             *
             * 🎨 GRADIENTE VERTICAL:
             * Transição suave de cores criando atmosfera Halloween:
             * - Topo: Preto profundo (noite escura)
             * - Meio: Roxo muito escuro (névoa mística)
             * - Base: Roxo médio (aura sobrenatural)
             */
            Box(
                modifier = Modifier
                    .fillMaxSize()                        // 📏 Preenche toda a tela
                    .background(
                        brush = Brush.verticalGradient(   // 🎨 Gradiente de cima para baixo
                            colors = listOf(
                                Color(0xFF0D0D0D),        // 🌑 Preto profundo
                                Color(0xFF2C003E),        // 🌌 Roxo muito escuro
                                Color(0xFF3D155F)         // 🔮 Roxo médio
                            )
                        )
                    )
                    .padding(paddingValues)               // ⬜ Respeita padding do Scaffold
            ) {
                /**
                 * 📋 COLUMN - LAYOUT VERTICAL
                 *
                 * Organiza conteúdo verticalmente:
                 * 1. Ranking (topo)
                 * 2. Lista de puzzles (abaixo)
                 *
                 * verticalScroll:
                 * - Permite rolar o conteúdo
                 * - Importante: usa Column (não LazyColumn) pois contém LazyColumn dentro
                 * - LazyColumn não pode estar dentro de outro scroll verticalmente
                 */
                Column(
                    modifier = Modifier
                        .fillMaxSize()                    // 📏 Preenche espaço disponível
                        .padding(horizontal = 12.dp)      // 🔲 Margem lateral
                        .verticalScroll(rememberScrollState())  // 📜 Habilita scroll
                ) {
                    Spacer(modifier = Modifier.height(16.dp))  // 📏 Espaço do topo

                    // ═══════════════════════════════════════════════════════════════
                    // SEÇÃO 1: RANKING
                    // ═══════════════════════════════════════════════════════════════

                    /**
                     * 👑 TÍTULO DO RANKING
                     *
                     * Exibe título com contagem de puzzles resolvidos.
                     * ${uiState.rankingList.size}: Interpolação de string (quantidade)
                     */
                    Text(
                        text = "👑 Ranking dos Desafios Superados (${uiState.rankingList.size})",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = HalloweenColors.primary,      // Laranja Halloween
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    /**
                     * 📊 EXIBIÇÃO DO RANKING
                     *
                     * Renderização condicional:
                     * - Se lista vazia: Mostra mensagem motivacional
                     * - Se lista tem itens: Mostra RankingDisplay
                     *
                     * 💡 UX:
                     * Sempre forneça feedback quando não há dados (empty states)
                     */
                    if (uiState.rankingList.isEmpty()) {
                        /**
                         * 🕸️ EMPTY STATE - NENHUM PUZZLE RESOLVIDO
                         *
                         * Mensagem amigável quando ranking está vazio.
                         * Incentiva usuário a começar a resolver puzzles.
                         */
                        Text(
                            text = "Nenhum puzzle resolvido ainda... 🕸️\nA escuridão aguarda seus primeiros passos!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = HalloweenColors.onSurface  // Bege claro
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    } else {
                        /**
                         * 🏆 COMPONENTE DE RANKING
                         *
                         * Composable separado que exibe top 3 puzzles.
                         * Recebe lista completa, mas mostra apenas os 3 primeiros.
                         */
                        RankingDisplay(uiState.rankingList)
                        Spacer(modifier = Modifier.height(24.dp))  // Espaço antes da lista
                    }

                    // ═══════════════════════════════════════════════════════════════
                    // SEÇÃO 2: LISTA COMPLETA DE PUZZLES
                    // ═══════════════════════════════════════════════════════════════

                    /**
                     * 🧩 TÍTULO DA LISTA
                     *
                     * Exibe quantidade total de puzzles cadastrados.
                     */
                    Text(
                        text = "🧩 Todos os Puzzles (${uiState.puzzleList.size})",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = HalloweenColors.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    /**
                     * 📜 LAZY COLUMN - LISTA PREGUIÇOSA
                     *
                     * LazyColumn:
                     * - Renderiza apenas itens visíveis na tela
                     * - Economiza memória (não cria todos os Cards de uma vez)
                     * - Scroll automático (não precisa de scrollState)
                     *
                     * heightIn(max = 1000.dp):
                     * - Limita altura máxima
                     * - Necessário porque está dentro de Column com scroll
                     * - Previne conflitos de scroll
                     *
                     * 💡 PERFORMANCE:
                     * Para listas com muitos itens, LazyColumn é essencial.
                     * Imagine 10000 puzzles: LazyColumn cria só os visíveis (~10),
                     * Column normal criaria todos os 10000 de uma vez.
                     */
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 1000.dp)  // Altura máxima
                    ) {
                        /**
                         * 🔄 ITEMS - RENDERIZA LISTA
                         *
                         * items(list) { item -> ... }:
                         * - Função de extensão do LazyColumn
                         * - Cria um Composable para cada item da lista
                         * - Gerencia keys automaticamente (usa índice)
                         *
                         * @param uiState.puzzleList Lista de puzzles a exibir
                         * @lambda { puzzle -> ... } Função que cria o Composable do item
                         *
                         * 💡 RECOMPOSIÇÃO:
                         * Quando lista muda, apenas itens afetados recompõem.
                         */
                        items(uiState.puzzleList) { puzzle ->
                            /**
                             * 🃏 CARD DO PUZZLE
                             *
                             * Componente separado que exibe um puzzle.
                             * Ao clicar, chama onPuzzleClick(puzzle.id).
                             */
                            PuzzleListItem(
                                puzzle = puzzle,
                                onClick = { onPuzzleClick(puzzle.id) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))  // Espaço final
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPOSABLE AUXILIAR: EXIBIÇÃO DO RANKING
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🏆 COMPONENTE DE RANKING (TOP 3)
 *
 * Exibe os 3 melhores puzzles resolvidos com medalhas.
 *
 * 🎯 PROPÓSITO:
 * - Destacar os melhores desempenhos
 * - Criar senso de conquista e competição
 * - Visual atraente com cores e emojis
 *
 * 📊 CRITÉRIOS DE ORDENAÇÃO:
 * 1. Menor tempo limite
 * 2. Menor número de tentativas (em caso de empate)
 *
 * @param ranking Lista de puzzles resolvidos já ordenados
 *                Esperado: vem do DAO com ORDER BY time_limit_sec, attempts
 */
@Composable
fun RankingDisplay(ranking: List<Puzzle>) {
    /**
     * 🥇 PEGA APENAS OS TOP 3
     *
     * take(3):
     * - Retorna no máximo os 3 primeiros elementos
     * - Se lista tem menos de 3, retorna todos
     * - Não gera erro se lista for vazia
     *
     * Exemplo:
     * [A, B, C, D, E].take(3) → [A, B, C]
     * [A, B].take(3) → [A, B]
     * [].take(3) → []
     */
    val topRanked = ranking.take(3)

    /**
     * 📋 COLUMN - ORGANIZA MEDALHAS VERTICALMENTE
     *
     * Cada puzzle do top 3 será exibido um abaixo do outro.
     */
    Column(modifier = Modifier.fillMaxWidth()) {
        /**
         * 🔄 ITERA SOBRE OS TOP 3 COM ÍNDICE
         *
         * forEachIndexed { index, puzzle -> ... }:
         * - Loop que fornece índice (0, 1, 2) e item (puzzle)
         * - Índice usado para determinar medalha e cor
         *
         * 💡 DIFERENÇA DE items():
         * Aqui usamos forEach (não Composable items) porque:
         * - Não estamos em LazyColumn
         * - São apenas 3 itens (performance não é problema)
         * - Queremos acesso direto ao índice para medalhas
         */
        topRanked.forEachIndexed { index, puzzle ->
            /**
             * 🏅 DETERMINA MEDALHA E COR POR POSIÇÃO
             *
             * when(index):
             * - Similar a switch/case de outras linguagens
             * - Retorna Pair<emoji, cor>
             *
             * Destructuring:
             * val (emoji, color) = when...
             * - Desempacota o Pair em duas variáveis
             *
             * 🥇 1º lugar: Ouro (laranja primário)
             * 🥈 2º lugar: Prata (roxo secundário)
             * 🥉 3º lugar: Bronze (vermelho terciário)
             */
            val (emoji, color) = when (index) {
                0 -> "🥇" to HalloweenColors.primary    // 1º: Ouro laranja
                1 -> "🥈" to HalloweenColors.secondary  // 2º: Prata roxa
                else -> "🥉" to HalloweenColors.tertiary  // 3º: Bronze vermelho
            }

            /**
             * 🃏 CARD DO RANKING
             *
             * Card com borda colorida baseada na posição.
             * Exibe: posição, medalha, título, tentativas, tempo.
             */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .border(
                        BorderStroke(2.dp, color),        // Borda colorida (2dp de largura)
                        shape = MaterialTheme.shapes.medium
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = HalloweenColors.surface  // Fundo cinza escuro
                ),
                elevation = CardDefaults.cardElevation(8.dp)  // Sombra para profundidade
            ) {
                /**
                 * 🏃 ROW - ORGANIZA CONTEÚDO HORIZONTALMENTE
                 *
                 * Layout horizontal:
                 * [Medalha + Posição] [Título + Tentativas] [Tempo]
                 */
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /**
                     * 🏅 MEDALHA E POSIÇÃO
                     *
                     * Exemplo: "🥇 1º"
                     * width(60.dp): Largura fixa para alinhamento
                     */
                    Text(
                        text = "$emoji ${index + 1}º",  // index+1: 0→1º, 1→2º, 2→3º
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = color,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.width(60.dp)
                    )

                    /**
                     * 📝 TÍTULO E TENTATIVAS
                     *
                     * weight(1f):
                     * - Ocupa todo espaço restante
                     * - Empurra o tempo para a direita
                     *
                     * Column:
                     * - Título em cima
                     * - Tentativas embaixo
                     */
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
                                color = Color.Gray  // Texto secundário em cinza
                            )
                        )
                    }

                    /**
                     * ⏱️ TEMPO LIMITE
                     *
                     * Exibe tempo em segundos com emoji de relógio.
                     * ?: 0: Se tempo for null, mostra 0
                     */
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

// ═══════════════════════════════════════════════════════════════════════════
// COMPOSABLE AUXILIAR: ITEM DA LISTA DE PUZZLES
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🃏 CARD DE PUZZLE NA LISTA
 *
 * Composable reutilizável que exibe um puzzle na lista principal.
 *
 * 🎯 PROPÓSITO:
 * - Mostrar informações resumidas do puzzle
 * - Indicar visualmente se está resolvido ou pendente
 * - Permitir clique para navegar aos detalhes
 *
 * 🎨 VISUAL:
 * - Card com fundo verde se resolvido, cinza se pendente
 * - Borda roxa decorativa
 * - Status com emoji (🧙‍♂️ resolvido, 👻 pendente)
 *
 * @param puzzle Objeto Puzzle com dados a exibir
 * @param onClick Callback sem parâmetros chamado ao clicar
 */
@Composable
fun PuzzleListItem(puzzle: Puzzle, onClick: () -> Unit) {
    /**
     * 🃏 CARD CLICÁVEL
     *
     * Card:
     * - Container com bordas arredondadas e sombra
     * - Material Design padrão
     *
     * clickable(onClick):
     * - Modifier que adiciona comportamento de clique
     * - Adiciona ripple effect automaticamente
     * - Área de toque acessível (mínimo 48dp)
     *
     * 💡 COR DE FUNDO CONDICIONAL:
     * - Se resolvido: Verde escuro translúcido (indicador de sucesso)
     * - Se pendente: Cinza escuro do tema (superfície padrão)
     */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)  // Torna o Card clicável
            .border(
                BorderStroke(1.dp, HalloweenColors.secondary),
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (puzzle.solved)
                Color(0xFF1B5E20).copy(alpha = 0.2f)  // 🟢 Verde escuro translúcido
            else
                HalloweenColors.surface               // ⚫ Cinza escuro
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)  // Sombra
    ) {
        /**
         * 🏃 ROW - LAYOUT HORIZONTAL
         *
         * Organiza conteúdo lado a lado:
         * [Título + Status] [Tempo]
         */
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /**
             * 📝 COLUNA COM TÍTULO E STATUS
             *
             * weight(1f):
             * - Ocupa espaço restante
             * - Empurra o tempo para a direita
             */
            Column(modifier = Modifier.weight(1f)) {
                /**
                 * 📌 TÍTULO DO PUZZLE
                 *
                 * Texto principal com destaque (SemiBold).
                 */
                Text(
                    text = puzzle.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = HalloweenColors.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                /**
                 * 🎯 STATUS DO PUZZLE
                 *
                 * Texto condicional:
                 * - Resolvido: "🧙‍♂️ Status: RESOLVIDO (X tentativas)" em amarelo
                 * - Pendente: "👻 Status: PENDENTE" em vermelho erro
                 *
                 * 💡 UX:
                 * Cores diferentes ajudam identificação rápida do status.
                 */
                Text(
                    text = if (puzzle.solved)
                        "🧙‍♂️ Status: RESOLVIDO (${puzzle.attempts} tentativas)"
                    else
                        "👻 Status: PENDENTE",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (puzzle.solved)
                            Color(0xFFFFD54F)             // 🟡 Amarelo dourado (sucesso)
                        else
                            HalloweenColors.error        // 🔴 Vermelho (alerta)
                    )
                )
            }

            /**
             * ⏱️ TEMPO LIMITE (APENAS SE PENDENTE)
             *
             * Renderização condicional:
             * - Se não resolvido: Mostra tempo limite
             * - Se resolvido: Não mostra (já tem info de tentativas)
             *
             * 💡 UX:
             * Tempo é relevante apenas para puzzles pendentes.
             * Para resolvidos, o importante é o número de tentativas.
             */
            if (!puzzle.solved) {
                Text(
                    text = "⏳ ${puzzle.timeLimitSec ?: "N/A"}s",  // N/A se null
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.Gray  // Cor secundária discreta
                    )
                )
            }
        }
    }
}
