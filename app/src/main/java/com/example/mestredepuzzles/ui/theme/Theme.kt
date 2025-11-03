package com.example.mestredepuzzles.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * 🎨 THEME.KT - CONFIGURAÇÃO DO TEMA VISUAL DO APLICATIVO
 *
 * Este arquivo define o tema do app usando Material Design 3.
 * Configura cores, tipografia e shapes para toda a aplicação.
 *
 * 📚 CONCEITOS:
 * - Color Scheme: Conjunto de cores que define a aparência do app
 * - Light/Dark Theme: Temas claro e escuro
 * - Dynamic Color: Cores que se adaptam ao papel de parede (Android 12+)
 *
 * 🎯 ESTRUTURA:
 * 1. LightColorScheme: Cores para modo claro (não muito usado neste app)
 * 2. DarkColorScheme: Cores para modo escuro (tema principal Halloween)
 * 3. MestreDePuzzlesTheme: Composable que aplica o tema
 */

// ═══════════════════════════════════════════════════════════════════════════
// ESQUEMA DE CORES CLARO (LIGHT THEME)
// ═══════════════════════════════════════════════════════════════════════════

/**
 * ☀️ LIGHT COLOR SCHEME - Tema Claro
 *
 * Define cores para modo claro (fundo branco, texto escuro).
 * Este app foca no tema escuro Halloween, mas mantemos o tema claro para compatibilidade.
 *
 * 💡 USO:
 * - Usuários que preferem temas claros
 * - Ambientes com muita luz (externo, sol)
 * - Acessibilidade (alguns usuários precisam de alto contraste claro)
 *
 * 📚 CORES BASEADAS EM:
 * Purple40, PurpleGrey40, Pink40 (definidas em Color.kt)
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,          // Roxo escuro para destaque
    secondary = PurpleGrey40,    // Roxo acinzentado para acentos
    tertiary = Pink40            // Rosa para elementos terciários

    /**
     * 💡 OUTRAS CORES DISPONÍVEIS (COMENTADAS):
     *
     * Você pode customizar mais cores descomentando e ajustando:
     * - background: Cor de fundo geral
     * - surface: Cor de Cards, Dialogs
     * - onPrimary: Cor de texto sobre primary
     * - onSecondary: Cor de texto sobre secondary
     * - onTertiary: Cor de texto sobre tertiary
     * - onBackground: Cor de texto sobre background
     * - onSurface: Cor de texto sobre surface
     *
     * Valores padrão (se não especificado):
     * - background = Color(0xFFFFFBFE) - Branco levemente rosado
     * - surface = Color(0xFFFFFBFE) - Mesmo do background
     * - onPrimary = Color.White - Branco
     * - onSecondary = Color.White
     * - onTertiary = Color.White
     * - onBackground = Color(0xFF1C1B1F) - Preto levemente roxeado
     * - onSurface = Color(0xFF1C1B1F)
     */
)

// ═══════════════════════════════════════════════════════════════════════════
// ESQUEMA DE CORES ESCURO (DARK THEME) - TEMA PRINCIPAL HALLOWEEN
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🌙 DARK COLOR SCHEME - Tema Escuro Halloween
 *
 * Define cores para modo escuro (fundo preto, texto claro).
 * Este é o tema PRINCIPAL do app, com visual Halloween assombrado.
 *
 * 🎃 TEMA HALLOWEEN:
 * - Laranja: Abóboras, fogo, ação
 * - Roxo: Mistério, magia, bruxas
 * - Preto: Noite, escuridão, mansão assombrada
 * - Verde limão: Poção mágica, contraste inesperado
 *
 * 💡 CORES IMPORTADAS DE Color.kt:
 * PrimaryDark, OnPrimaryDark, SecondaryDark, etc.
 */
private val DarkColorScheme = darkColorScheme(
    /**
     * 🎃 PRIMARY - Cor Primária
     *
     * Laranja abóbora (HalloweenOrange).
     * Usada em:
     * - Botões principais
     * - FAB (Floating Action Button)
     * - TopAppBar
     * - Elementos de destaque
     */
    primary = PrimaryDark,

    /**
     * ⚪ ON PRIMARY - Texto Sobre Primária
     *
     * Branco suave (GhostWhite).
     * Cor do texto/ícones sobre elementos laranjas.
     * Garante legibilidade.
     */
    onPrimary = OnPrimaryDark,

    /**
     * 🔮 SECONDARY - Cor Secundária
     *
     * Roxo místico (DeepPurple).
     * Usada em:
     * - Botões secundários
     * - Acentos e decorações
     * - Bordas especiais
     * - Checkboxes desmarcados
     */
    secondary = SecondaryDark,

    /**
     * ⚪ ON SECONDARY - Texto Sobre Secundária
     *
     * Branco suave para texto sobre roxo.
     */
    onSecondary = OnSecondaryDark,

    /**
     * 🧪 TERTIARY - Cor Terciária
     *
     * Verde limão (cor inesperada que chama atenção).
     * Usada em:
     * - Botão de deletar
     * - Alertas especiais
     * - Elementos que precisam se destacar
     */
    tertiary = TertiaryDark,

    /**
     * 🌑 BACKGROUND - Cor de Fundo
     *
     * Preto meia-noite (MidnightBlack).
     * Fundo geral do app.
     */
    background = BackgroundDark,

    /**
     * 🃏 SURFACE - Cor de Superfícies
     *
     * Preto meia-noite (mesmo do background).
     * Usado em Cards, Dialogs, Bottom Sheets.
     *
     * 💡 POR QUE IGUAL AO BACKGROUND?
     * Visual unificado, sem contraste entre fundo e Cards.
     * Cards se destacam por sombras e bordas, não por cor diferente.
     */
    surface = SurfaceDark,

    /**
     * 📄 ON BACKGROUND - Texto Sobre Fundo
     *
     * Branco suave (GhostWhite).
     * Cor padrão do texto do body.
     */
    onBackground = OnBackgroundDark,

    /**
     * 📄 ON SURFACE - Texto Sobre Superfícies
     *
     * Branco suave (mesmo do onBackground).
     * Texto em Cards, Dialogs, etc.
     */
    onSurface = OnBackgroundDark,

    /**
     * 🎨 CORES DE CONTAINER (VERSÕES MAIS SUAVES)
     *
     * Containers são backgrounds de elementos específicos.
     * Usamos versões translúcidas (alpha) para suavizar.
     */
    primaryContainer = HalloweenOrange.copy(alpha = 0.5f),  // Laranja 50% opaco
    secondaryContainer = DeepPurple.copy(alpha = 0.7f),     // Roxo 70% opaco
    tertiaryContainer = Color(0xFF333333)                   // Cinza escuro
)

// ═══════════════════════════════════════════════════════════════════════════
// COMPOSABLE DO TEMA - APLICA CORES E TIPOGRAFIA
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🎨 MESTRE DE PUZZLES THEME - Tema Principal do App
 *
 * Composable que envolve toda a aplicação para aplicar o tema.
 * Define cores, tipografia e outros aspectos visuais.
 *
 * 🎯 RESPONSABILIDADES:
 * - Determinar se usa tema claro ou escuro
 * - Aplicar cores dinâmicas (Android 12+) se habilitado
 * - Aplicar tipografia customizada
 * - Fornecer MaterialTheme para componentes filhos
 *
 * 📚 PARÂMETROS:
 *
 * @param darkTheme
 *        Define se usa tema escuro.
 *        Padrão: isSystemInDarkTheme() - segue configuração do sistema
 *        - true: Tema escuro (Halloween)
 *        - false: Tema claro (padrão Material)
 *
 * @param dynamicColor
 *        Define se usa cores dinâmicas (Material You).
 *        Padrão: true
 *        - true: Cores se adaptam ao papel de parede (Android 12+)
 *        - false: Usa cores fixas do tema
 *
 *        💡 MATERIAL YOU:
 *        Android 12+ permite extrair cores do papel de parede do usuário.
 *        Se habilitado, ignora nosso tema Halloween.
 *        Para manter Halloween sempre, passe dynamicColor = false.
 *
 * @param content
 *        Conteúdo (UI) que receberá o tema.
 *        Todo Composable dentro de content terá acesso ao MaterialTheme.
 */
@Composable
fun MestreDePuzzlesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),  // Segue sistema por padrão
    dynamicColor: Boolean = true,                 // Habilita Material You
    content: @Composable () -> Unit               // Conteúdo a ser tematizado
) {
    /**
     * 🎨 DETERMINA O ESQUEMA DE CORES A USAR
     *
     * Lógica de prioridade:
     * 1. Se dynamicColor E Android 12+: Usa cores do sistema (Material You)
     * 2. Senão, se darkTheme: Usa DarkColorScheme (Halloween)
     * 3. Senão: Usa LightColorScheme (padrão claro)
     */
    val colorScheme = when {
        /**
         * 🎨 CASO 1: CORES DINÂMICAS (MATERIAL YOU)
         *
         * dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S:
         * - dynamicColor = true: Usuário habilitou cores dinâmicas
         * - SDK_INT >= S: Android 12 (API 31) ou superior
         *
         * LocalContext.current:
         * - Obtém contexto Android atual
         * - Necessário para acessar configurações do sistema
         *
         * dynamicDarkColorScheme / dynamicLightColorScheme:
         * - Funções do Material 3 que extraem cores do papel de parede
         * - Criam esquema de cores harmonioso baseado no wallpaper
         *
         * 💡 NOTA:
         * Se quiser forçar tema Halloween sempre, passe dynamicColor = false
         * ao chamar MestreDePuzzlesTheme.
         */
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)  // Material You escuro
            else dynamicLightColorScheme(context)            // Material You claro
        }

        /**
         * 🌙 CASO 2: TEMA ESCURO FIXO (HALLOWEEN)
         *
         * darkTheme = true e não está usando cores dinâmicas.
         * Aplica nosso DarkColorScheme customizado.
         */
        darkTheme -> DarkColorScheme

        /**
         * ☀️ CASO 3: TEMA CLARO FIXO
         *
         * darkTheme = false e não está usando cores dinâmicas.
         * Aplica nosso LightColorScheme.
         */
        else -> LightColorScheme
    }

    /**
     * 🎨 APLICA O MATERIAL THEME
     *
     * MaterialTheme:
     * - Componente raiz que fornece tema para todos os filhos
     * - Define colorScheme, typography, shapes
     *
     * @param colorScheme Esquema de cores determinado acima
     * @param typography Tipografia customizada (definida em Type.kt)
     * @param content Conteúdo que receberá o tema
     *
     * 💡 COMO FUNCIONA:
     * Todos os Composables dentro de content podem acessar:
     * - MaterialTheme.colorScheme.primary
     * - MaterialTheme.typography.bodyLarge
     * - MaterialTheme.shapes.medium
     * - etc.
     */
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}