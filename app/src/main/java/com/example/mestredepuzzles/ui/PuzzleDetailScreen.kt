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

/**
 * 📄 TELA DE DETALHES/EDIÇÃO/CRIAÇÃO DE PUZZLE
 *
 * Esta tela serve múltiplos propósitos:
 * 1. ➕ CRIAR novo puzzle (quando puzzleId = 0)
 * 2. ✏️ EDITAR puzzle existente (quando puzzleId > 0)
 * 3. 🗑️ DELETAR puzzle (botão na top bar)
 *
 * 🎯 FUNCIONALIDADES:
 * - Formulário com campos para título, dicas, tempo limite
 * - Checkbox para marcar como resolvido
 * - Campo de tentativas (só aparece se resolvido)
 * - Validação (título obrigatório)
 * - Tema visual Halloween (laranja, roxo, gradiente escuro)
 *
 * 📚 CONCEITOS DE COMPOSE:
 * - State Hoisting: Estados locais gerenciados por remember
 * - Recomposição: UI atualiza automaticamente quando estado muda
 * - Material Design 3: Usa componentes modernos (Scaffold, TextField, etc)
 */

/**
 * 🎨 PALETA DE CORES HALLOWEEN
 *
 * Define o esquema de cores personalizado para esta tela.
 * Usa Material Design 3 Color Scheme para consistência.
 *
 * 💡 TEMA HALLOWEEN:
 * - Laranja: Cor de abóboras e fogo
 * - Roxo: Cor mística e de bruxaria
 * - Vermelho escuro: Cor de perigo e sangue
 * - Preto: Cor da noite e mistério
 *
 * 📝 darkColorScheme():
 * Cria um esquema de cores para modo escuro (dark theme).
 */
private val HalloweenColors = darkColorScheme(
    primary = Color(0xFFFF9800),         // 🎃 Laranja abóbora (cor principal para botões e destaques)
    onPrimary = Color.Black,             // ⚫ Texto preto em cima da cor primária
    secondary = Color(0xFF8E24AA),       // 🔮 Roxo místico (cor secundária para acentos)
    onSecondary = Color.White,           // ⚪ Texto branco em cima da cor secundária
    tertiary = Color(0xFFB71C1C),        // 🩸 Vermelho escuro (cor terciária para alertas/deletar)
    background = Color(0xFF0D0D0D),      // 🌑 Preto quase puro (fundo geral)
    surface = Color(0xFF1C1C1C),         // 🪨 Cinza muito escuro (superfícies como cards)
    onSurface = Color(0xFFFFF3E0)        // 📄 Bege claro (texto em cima de superfícies)
)

/**
 * 🖼️ COMPOSABLE PRINCIPAL: TELA DE DETALHES DO PUZZLE
 *
 * Este é o Composable raiz da tela de detalhes.
 * Gerencia todo o formulário de criação/edição de puzzles.
 *
 * 🎯 RESPONSABILIDADES:
 * - Determinar se é criação (ID=0) ou edição (ID>0)
 * - Buscar puzzle existente do estado do ViewModel
 * - Gerenciar estados locais dos campos (título, dicas, etc)
 * - Validar dados antes de salvar
 * - Comunicar ações ao ViewModel (salvar, deletar)
 *
 * 📚 PARÂMETROS:
 *
 * @param viewModel
 *        ViewModel que gerencia dados e operações de banco
 *        Usado para acessar lista de puzzles e executar ações (add/update/delete)
 *
 * @param puzzleId
 *        ID do puzzle a editar, ou 0 para criar novo
 *        Vem como argumento da navegação
 *
 * @param navigateBack
 *        Callback para voltar à tela anterior
 *        Chamado após salvar ou clicar em "Voltar"
 *        Tipo: () -> Unit (função sem parâmetros e sem retorno)
 *
 * 💡 ANOTAÇÕES:
 * @OptIn(ExperimentalMaterial3Api::class)
 * Necessário porque TopAppBar é API experimental do Material 3.
 * Indica que estamos cientes que a API pode mudar em versões futuras.
 *
 * @Composable
 * Marca função como Composable (pode usar outros Composables e gerenciar estado).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleDetailScreen(
    viewModel: PuzzleViewModel,
    puzzleId: Int,
    navigateBack: () -> Unit
) {
    // ═══════════════════════════════════════════════════════════════
    // 1️⃣ DETERMINAR MODO: CRIAR OU EDITAR
    // ═══════════════════════════════════════════════════════════════

    /**
     * 🆕 VERIFICA SE É CRIAÇÃO DE NOVO PUZZLE
     *
     * Por convenção, puzzleId = 0 significa "criar novo"
     * porque IDs reais começam em 1 (auto-incremento do banco).
     */
    val isNewPuzzle = puzzleId == 0

    /**
     * 📊 OBSERVA O ESTADO ATUAL DA UI
     *
     * collectAsState():
     * - Observa o StateFlow do ViewModel
     * - Recompõe quando o estado muda
     * - .value extrai o valor atual do State
     *
     * 💡 FLUXO REATIVO:
     * Banco muda → ViewModel emite novo uiState → Tela recompõe
     */
    val uiState = viewModel.uiState.collectAsState().value

    /**
     * 🔍 BUSCA O PUZZLE EXISTENTE (SE ESTIVER EDITANDO)
     *
     * firstOrNull { ... }:
     * - Procura primeiro puzzle que atende condição
     * - Retorna null se não encontrar
     *
     * Se isNewPuzzle = true, existingPuzzle será sempre null
     * Se isNewPuzzle = false e puzzle existir, existingPuzzle terá os dados
     */
    val existingPuzzle = uiState.puzzleList.firstOrNull { it.id == puzzleId }

    // ═══════════════════════════════════════════════════════════════
    // 2️⃣ ESTADOS LOCAIS DOS CAMPOS DO FORMULÁRIO
    // ═══════════════════════════════════════════════════════════════

    /**
     * 📝 ESTADO LOCAL: TÍTULO DO PUZZLE
     *
     * remember(puzzleId):
     * - Cria estado que persiste entre recomposições
     * - Key: puzzleId → Se puzzleId mudar, recria o estado
     * - Importante para garantir que campos zerem ao navegar para novo puzzle
     *
     * mutableStateOf():
     * - Cria estado mutável observável
     * - Quando muda, trigger recomposição
     *
     * by:
     * - Delegate que permite usar title diretamente (sem .value)
     *
     * Valor inicial:
     * - Se editando: usa título do puzzle existente
     * - Se criando: string vazia
     * - Elvis operator (?:) garante valor padrão se existingPuzzle?.title for null
     */
    var title by remember(puzzleId) { mutableStateOf(existingPuzzle?.title ?: "") }

    /**
     * 💡 ESTADO LOCAL: DICAS DO PUZZLE
     *
     * Três campos opcionais para dicas que ajudam o jogador.
     * Lógica similar ao título, mas podem ser null.
     */
    var hint1 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint1 ?: "") }
    var hint2 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint2 ?: "") }
    var hint3 by remember(puzzleId) { mutableStateOf(existingPuzzle?.hint3 ?: "") }

    /**
     * ⏱️ ESTADO LOCAL: TEMPO LIMITE (em segundos)
     *
     * Armazenado como String para facilitar input do teclado.
     * Será convertido para Int ao salvar.
     *
     * .toString():
     * - Converte Int? para String
     * - Se null, toString() gera "null" → Usamos ?: "" para string vazia
     */
    var timeLimitSec by remember(puzzleId) { mutableStateOf(existingPuzzle?.timeLimitSec?.toString() ?: "") }

    /**
     * ✅ ESTADO LOCAL: STATUS DE RESOLUÇÃO
     *
     * Boolean indicando se puzzle foi resolvido.
     * Controla visibilidade do campo de tentativas.
     */
    var isSolved by remember(puzzleId) { mutableStateOf(existingPuzzle?.solved ?: false) }

    /**
     * 🎯 ESTADO LOCAL: NÚMERO DE TENTATIVAS
     *
     * Quantas vezes o jogador tentou resolver.
     * Só relevante se isSolved = true.
     */
    var attempts by remember(puzzleId) { mutableStateOf(existingPuzzle?.attempts?.toString() ?: "") }

    // ═══════════════════════════════════════════════════════════════
    // 3️⃣ ESTRUTURA DA UI
    // ═══════════════════════════════════════════════════════════════

    /**
     * 🎨 APLICA O TEMA HALLOWEEN
     *
     * MaterialTheme com colorScheme customizado.
     * Todos os componentes filhos herdam essas cores.
     */
    MaterialTheme(colorScheme = HalloweenColors) {
        /**
         * 🏗️ SCAFFOLD - ESTRUTURA BASE DA TELA
         *
         * Scaffold é um container que fornece estrutura Material Design:
         * - TopBar (barra superior com título e ações)
         * - Content (conteúdo principal da tela)
         * - FAB, BottomBar, etc (opcionais, não usados aqui)
         *
         * 💡 VANTAGENS:
         * - Layout consistente com Material Design
         * - Gerencia padding automaticamente (content não fica atrás da TopBar)
         * - Facilita organização visual
         */
        Scaffold(
            // ═══════════════════════════════════════════════════════════════
            // TOP BAR: BARRA SUPERIOR COM TÍTULO E AÇÕES
            // ═══════════════════════════════════════════════════════════════
            topBar = {
                /**
                 * 🎯 BARRA SUPERIOR (APP BAR)
                 *
                 * Exibe:
                 * - Botão de voltar (navigationIcon)
                 * - Título da tela (title)
                 * - Botão de deletar (actions) - só aparece ao editar
                 *
                 * Material Design recomenda sempre ter:
                 * - Título descritivo
                 * - Ícone de navegação em telas que não são a principal
                 */
                TopAppBar(
                    title = {
                        /**
                         * 📝 TÍTULO DINÂMICO
                         *
                         * Muda baseado no modo:
                         * - "Novo Desafio da Mansão" → Criando novo puzzle
                         * - "Editar Puzzle Assombrado" → Editando existente
                         *
                         * Emojis tornam a interface mais lúdica e visual.
                         */
                        Text(
                            if (isNewPuzzle) "🎃 Novo Desafio da Mansão" else "🧩 Editar Puzzle Assombrado",
                            fontWeight = FontWeight.Bold,  // Negrito para destaque
                            color = Color.Black            // Preto contrasta com fundo laranja
                        )
                    },
                    navigationIcon = {
                        /**
                         * ⬅️ BOTÃO DE VOLTAR
                         *
                         * IconButton:
                         * - Botão circular para ícones
                         * - Área de toque acessível (48dp mínimo)
                         *
                         * Icons.Filled.ArrowBack:
                         * - Ícone padrão de "voltar" do Material Design
                         * - Familiar aos usuários Android
                         *
                         * onClick: Executa callback navigateBack passado como parâmetro
                         */
                        IconButton(onClick = navigateBack) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Voltar",  // Acessibilidade (leitores de tela)
                                tint = Color.Black              // Cor do ícone
                            )
                        }
                    },
                    actions = {
                        /**
                         * 🗑️ BOTÃO DE DELETAR (CONDICIONAL)
                         *
                         * Só aparece se:
                         * - !isNewPuzzle: Não está criando novo (não faz sentido deletar algo que não existe)
                         * - existingPuzzle != null: Puzzle existe no banco
                         *
                         * 💡 LÓGICA:
                         * if (condição) { Composable } → Renderização condicional
                         */
                        if (!isNewPuzzle && existingPuzzle != null) {
                            IconButton(onClick = {
                                /**
                                 * 🗑️ AÇÃO: DELETAR PUZZLE
                                 *
                                 * 1. Chama viewModel.deletePuzzle()
                                 * 2. ViewModel deleta do banco via Repository
                                 * 3. Navega de volta imediatamente
                                 *
                                 * ⚠️ NOTA:
                                 * Idealmente deveria mostrar confirmação antes de deletar.
                                 * Exercício: Adicionar AlertDialog de confirmação!
                                 */
                                viewModel.deletePuzzle(existingPuzzle)
                                navigateBack()  // Volta para lista
                            }) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Deletar Puzzle",
                                    tint = HalloweenColors.tertiary  // Vermelho escuro (cor de perigo)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = HalloweenColors.primary  // Fundo laranja
                    )
                )
            },
            containerColor = Color.Transparent  // Transparente para gradiente aparecer
        ) { paddingValues ->
            /**
             * 📦 BOX - CONTAINER COM FUNDO GRADIENTE
             *
             * Box:
             * - Container simples que empilha filhos um sobre o outro
             * - Permite sobreposição de elementos
             * - Aqui usamos para colocar gradiente atrás do formulário
             *
             * 🎨 GRADIENTE VERTICAL:
             * Brush.verticalGradient():
             * - Cria transição suave entre cores
             * - De cima para baixo: Preto → Roxo escuro → Roxo médio
             * - Efeito místico/assombrado perfeito para tema Halloween
             */
            Box(
                modifier = Modifier
                    .fillMaxSize()                        // 📏 Preenche toda a tela
                    .background(
                        brush = Brush.verticalGradient(   // 🎨 Gradiente de cima para baixo
                            colors = listOf(
                                Color(0xFF0D0D0D),        // 🌑 Topo: Preto profundo
                                Color(0xFF2C003E),        // 🌌 Meio: Roxo muito escuro
                                Color(0xFF3D155F)         // 🔮 Base: Roxo médio
                            )
                        )
                    )
                    .padding(paddingValues)               // ⬜ Respeita padding do Scaffold (não fica atrás da TopBar)
            ) {
                /**
                 * 📋 COLUMN - COLUNA VERTICAL COM FORMULÁRIO
                 *
                 * Column:
                 * - Layout que organiza filhos verticalmente
                 * - Cada elemento aparece abaixo do anterior
                 * - Perfeito para formulários
                 *
                 * verticalScroll:
                 * - Permite rolar quando conteúdo é maior que tela
                 * - Importante para dispositivos pequenos ou modo paisagem
                 * - rememberScrollState(): Lembra posição do scroll entre recomposições
                 */
                Column(
                    modifier = Modifier
                        .padding(16.dp)                   // 🔲 Margem interna (espaço das bordas)
                        .fillMaxSize()                    // 📏 Preenche espaço disponível
                        .verticalScroll(rememberScrollState()),  // 📜 Habilita rolagem vertical
                    horizontalAlignment = Alignment.CenterHorizontally  // ⬅️➡️ Centraliza filhos horizontalmente
                ) {
                    Spacer(modifier = Modifier.height(8.dp))  // 📏 Espaço vertical vazio

                    /**
                     * 📝 TEXTO DESCRITIVO
                     *
                     * Mensagem que muda baseada no modo:
                     * - Criando: Incentiva criar enigma sombrio
                     * - Editando: Incentiva manter a maldição viva
                     *
                     * Texto centralizado com cor do tema.
                     */
                    Text(
                        text = if (isNewPuzzle)
                            "Crie um novo enigma sombrio 👻"
                        else
                            "Modifique o desafio e mantenha a maldição viva 🕸️",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = HalloweenColors.onSurface,   // Bege claro
                            textAlign = TextAlign.Center
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // ═══════════════════════════════════════════════════════════════
                    // CAMPOS DO FORMULÁRIO
                    // ═══════════════════════════════════════════════════════════════

                    /**
                     * 📝 CAMPO: TÍTULO (OBRIGATÓRIO)
                     *
                     * ThemedTextField é um Composable customizado (definido no fim do arquivo).
                     *
                     * @value title → Valor atual do campo (vinculado ao estado)
                     * @onValueChange { title = it } → Quando usuário digita, atualiza o estado
                     *
                     * 💡 TWO-WAY DATA BINDING:
                     * Estado → UI: title muda, campo atualiza
                     * UI → Estado: usuário digita, title atualiza
                     */
                    ThemedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Título do Puzzle*"  // * indica obrigatório
                    )

                    /**
                     * 💡 CAMPOS: DICAS (OPCIONAIS)
                     *
                     * Três campos para dicas que ajudam o jogador.
                     * Não são obrigatórios (sem *).
                     */
                    ThemedTextField(value = hint1, onValueChange = { hint1 = it }, label = "Dica 1")
                    ThemedTextField(value = hint2, onValueChange = { hint2 = it }, label = "Dica 2")
                    ThemedTextField(value = hint3, onValueChange = { hint3 = it }, label = "Dica 3")

                    /**
                     * ⏱️ CAMPO: TEMPO LIMITE (OPCIONAL, NUMÉRICO)
                     *
                     * @keyboardType = KeyboardType.Number:
                     * - Abre teclado numérico no dispositivo
                     * - Facilita entrada de números
                     *
                     * .filter { c -> c.isDigit() }:
                     * - Permite apenas dígitos (0-9)
                     * - Previne entrada de letras ou símbolos
                     *
                     * 💡 VALIDAÇÃO:
                     * Validação básica aqui, conversão para Int ao salvar (toIntOrNull).
                     */
                    ThemedTextField(
                        value = timeLimitSec,
                        onValueChange = { timeLimitSec = it.filter { c -> c.isDigit() } },
                        label = "Tempo Limite (segundos)",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    /**
                     * ☑️ CHECKBOX: MARCAR COMO RESOLVIDO
                     *
                     * Row com Checkbox e Text:
                     * - Organiza checkbox e label horizontalmente
                     * - Padrão comum em UIs
                     *
                     * @checked isSolved → Estado atual
                     * @onCheckedChange { isSolved = it } → Atualiza estado ao clicar
                     *
                     * 💡 EFEITO:
                     * Quando isSolved muda para true, campo de tentativas aparece.
                     */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSolved,
                            onCheckedChange = { isSolved = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = HalloweenColors.primary,      // Laranja quando marcado
                                uncheckedColor = HalloweenColors.secondary   // Roxo quando desmarcado
                            )
                        )
                        Text(
                            "Resolvido",
                            color = HalloweenColors.onSurface,
                            modifier = Modifier.padding(start = 8.dp)  // Espaço entre checkbox e texto
                        )
                    }

                    /**
                     * 🎯 CAMPO CONDICIONAL: NÚMERO DE TENTATIVAS
                     *
                     * if (isSolved) { ... }:
                     * - Renderização condicional
                     * - Campo só aparece se puzzle está marcado como resolvido
                     *
                     * 💡 UX:
                     * Faz sentido só perguntar tentativas se o puzzle foi resolvido.
                     * Evita confusão e simplifica UI.
                     */
                    if (isSolved) {
                        ThemedTextField(
                            value = attempts,
                            onValueChange = { attempts = it.filter { c -> c.isDigit() } },
                            label = "Número de Tentativas",
                            keyboardType = KeyboardType.Number
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ═══════════════════════════════════════════════════════════════
                    // BOTÃO DE SALVAR
                    // ═══════════════════════════════════════════════════════════════

                    /**
                     * 💾 BOTÃO: CRIAR/ATUALIZAR PUZZLE
                     *
                     * Texto do botão muda baseado no modo:
                     * - "💀 Criar Puzzle" → Modo criação
                     * - "🕷️ Atualizar Puzzle" → Modo edição
                     */
                    Button(
                        onClick = {
                            /**
                             * 💾 LÓGICA DE SALVAMENTO
                             *
                             * 1️⃣ VALIDAÇÃO: Verifica se título não está vazio
                             * 2️⃣ PREPARAÇÃO: Cria objeto Puzzle com dados do formulário
                             * 3️⃣ PERSISTÊNCIA: Salva no banco via ViewModel
                             * 4️⃣ NAVEGAÇÃO: Volta para tela anterior
                             */

                            // ── VALIDAÇÃO ──────────────────────────────────────
                            /**
                             * ⚠️ VALIDAÇÃO SIMPLES: TÍTULO OBRIGATÓRIO
                             *
                             * isBlank():
                             * - Retorna true se string é vazia ou só tem espaços
                             * - Mais robusto que isEmpty() (que não detecta "   ")
                             *
                             * return@Button:
                             * - Sai da lambda onClick sem executar resto do código
                             * - Impede salvar puzzle sem título
                             *
                             * 💡 MELHORIA FUTURA:
                             * Mostrar mensagem de erro ao usuário (Snackbar ou Toast)
                             */
                            if (title.isBlank()) return@Button

                            // ── PREPARAÇÃO DOS DADOS ───────────────────────────
                            /**
                             * 🔨 CONSTRUÇÃO DO OBJETO PUZZLE
                             *
                             * Cria instância de Puzzle com dados do formulário.
                             *
                             * @id:
                             * - Se editando: mantém ID existente (existingPuzzle?.id)
                             * - Se criando: usa 0 (banco gerará novo ID automaticamente)
                             *
                             * @title:
                             * - Sempre obrigatório (já validado acima)
                             *
                             * @hint1, hint2, hint3:
                             * - takeIf { it.isNotBlank() } → Converte string vazia em null
                             * - Banco armazena null em vez de strings vazias (mais limpo)
                             *
                             * @timeLimitSec:
                             * - toIntOrNull() → Converte String para Int
                             * - Se conversão falhar (string vazia), retorna null
                             *
                             * @solved:
                             * - Valor direto do checkbox
                             *
                             * @attempts:
                             * - toIntOrNull() ?: 0 → Converte para Int ou usa 0 se falhar
                             */
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

                            // ── PERSISTÊNCIA ───────────────────────────────────
                            /**
                             * 💾 SALVA NO BANCO DE DADOS
                             *
                             * Modo criação: addPuzzle()
                             * - Insere novo registro no banco
                             * - Banco gera ID automaticamente
                             *
                             * Modo edição: updatePuzzle()
                             * - Atualiza registro existente
                             * - Identifica pelo ID
                             *
                             * 🔄 FLUXO APÓS SALVAR:
                             * ViewModel → Repository → DAO → Banco SQLite
                             * → Flow emite mudança → uiState atualiza → PuzzleListScreen recompõe
                             */
                            if (isNewPuzzle) viewModel.addPuzzle(puzzleToSave)
                            else viewModel.updatePuzzle(puzzleToSave)

                            // ── NAVEGAÇÃO ──────────────────────────────────────
                            /**
                             * ⬅️ VOLTA PARA TELA ANTERIOR
                             *
                             * Executa callback navigateBack passado como parâmetro.
                             * Geralmente é { navController.popBackStack() }.
                             *
                             * 💡 UX:
                             * Após salvar, usuário retorna à lista onde verá o puzzle atualizado.
                             */
                            navigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()               // Botão ocupa largura total
                            .height(50.dp),               // Altura fixa de 50dp
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HalloweenColors.secondary,  // Fundo roxo
                            contentColor = Color.White                    // Texto branco
                        )
                    ) {
                        /**
                         * 📝 TEXTO DO BOTÃO (DINÂMICO)
                         *
                         * Emojis e texto mudam baseado no modo:
                         * - Criação: "💀 Criar Puzzle"
                         * - Edição: "🕷️ Atualizar Puzzle"
                         */
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

// ═══════════════════════════════════════════════════════════════════════════
// COMPOSABLE AUXILIAR: CAMPO DE TEXTO TEMÁTICO
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🎨 CAMPO DE TEXTO ESTILIZADO COM TEMA HALLOWEEN
 *
 * Composable reutilizável que encapsula um OutlinedTextField com estilo customizado.
 * Evita repetição de código (todos os campos têm o mesmo visual).
 *
 * 🎯 PROPÓSITO:
 * - Centralizar estilização dos campos de texto
 * - Manter consistência visual
 * - Facilitar manutenção (mudar estilo em um só lugar)
 *
 * 📚 CONCEITOS:
 * - Composable reutilizável (como um componente customizado)
 * - Encapsulamento de estilo
 * - Parâmetros configuráveis
 *
 * @param value
 *        Valor atual do campo (vinculado ao estado do pai)
 *
 * @param onValueChange
 *        Callback chamado quando usuário digita
 *        Recebe o novo texto e deve atualizar o estado
 *        Tipo: (String) -> Unit
 *
 * @param label
 *        Texto do label (placeholder animado)
 *        Exemplo: "Título do Puzzle*"
 *
 * @param keyboardType
 *        Tipo de teclado a exibir
 *        Padrão: KeyboardType.Text (teclado normal)
 *        Pode ser: Number, Email, Phone, etc
 */
@Composable
fun ThemedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    /**
     * 📝 OUTLINED TEXT FIELD - CAMPO COM BORDA
     *
     * OutlinedTextField:
     * - Variante de TextField com borda ao redor
     * - Borda muda de cor ao focar (UX feedback)
     * - Label anima para cima quando campo é focado
     *
     * Material Design 3: Estilo moderno e acessível
     */
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            /**
             * 🏷️ LABEL ANIMADO
             *
             * Texto que:
             * - Aparece dentro do campo quando vazio
             * - Anima para cima e fica menor quando campo é focado
             * - Cor muda baseado no estado (focado/não focado)
             */
            Text(
                label,
                color = HalloweenColors.primary  // Laranja Halloween
            )
        },
        modifier = Modifier
            .fillMaxWidth()                       // Ocupa largura total disponível
            .padding(vertical = 6.dp)             // Espaço vertical entre campos
            .border(
                /**
                 * 🔲 BORDA SECUNDÁRIA DECORATIVA
                 *
                 * BorderStroke:
                 * - Adiciona borda extra ao redor do campo
                 * - 1dp de largura, cor roxo secundário
                 * - Efeito visual: campo "brilha" com duas bordas
                 *
                 * shape: Bordas arredondadas (definidas no tema)
                 */
                BorderStroke(1.dp, HalloweenColors.secondary),
                shape = MaterialTheme.shapes.medium  // Bordas levemente arredondadas
            ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            /**
             * 🎨 CORES CUSTOMIZADAS DO CAMPO
             *
             * Material 3 permite customizar cor de cada elemento:
             *
             * @focusedBorderColor: Borda quando campo está ativo (laranja)
             * @unfocusedBorderColor: Borda quando campo está inativo (roxo)
             * @focusedLabelColor: Label quando focado (laranja)
             * @cursorColor: Cor do cursor piscante (laranja)
             * @focusedTextColor: Cor do texto digitado quando focado (bege)
             * @unfocusedTextColor: Cor do texto quando não focado (bege)
             *
             * 💡 UX:
             * Cores diferentes para estados diferentes ajudam o usuário
             * a entender onde está o foco.
             */
            focusedBorderColor = HalloweenColors.primary,      // 🎃 Laranja ao focar
            unfocusedBorderColor = HalloweenColors.secondary,  // 🔮 Roxo sem focar
            focusedLabelColor = HalloweenColors.primary,       // 🎃 Label laranja ao focar
            cursorColor = HalloweenColors.primary,             // 🎃 Cursor laranja
            focusedTextColor = HalloweenColors.onSurface,      // 📄 Texto bege ao focar
            unfocusedTextColor = HalloweenColors.onSurface     // 📄 Texto bege sempre
        )
    )
}
