package com.example.mestredepuzzles.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 📝 TYPE.KT - CONFIGURAÇÃO DE TIPOGRAFIA DO APLICATIVO
 *
 * Este arquivo define todos os estilos de texto usados no app.
 * Centralizar tipografia garante consistência e facilita manutenção.
 *
 * 🎯 MATERIAL DESIGN 3 TYPOGRAPHY:
 * Material 3 define uma escala de tipos de texto:
 * - Display: Títulos muito grandes (displayLarge, displayMedium, displaySmall)
 * - Headline: Títulos grandes (headlineLarge, headlineMedium, headlineSmall)
 * - Title: Títulos médios (titleLarge, titleMedium, titleSmall)
 * - Body: Texto do corpo (bodyLarge, bodyMedium, bodySmall)
 * - Label: Labels e botões (labelLarge, labelMedium, labelSmall)
 *
 * 📚 CONCEITOS:
 * - TextStyle: Define aparência do texto (fonte, tamanho, peso, espaçamento)
 * - FontFamily: Família de fontes (Default = Roboto no Android)
 * - FontWeight: Peso da fonte (Normal, Bold, Light, etc)
 * - fontSize: Tamanho em sp (scale-independent pixels)
 * - lineHeight: Altura da linha (espaçamento vertical entre linhas)
 * - letterSpacing: Espaçamento entre letras
 *
 * 💡 UNIDADES:
 * - sp (scale-independent pixels): Usado para texto
 *   - Se adapta às preferências de tamanho de fonte do usuário
 *   - Acessibilidade: usuários podem aumentar fonte nas configurações
 * - dp (density-independent pixels): Usado para layouts, margens, etc
 */

// ═══════════════════════════════════════════════════════════════════════════
// TIPOGRAFIA DO MATERIAL DESIGN 3
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 📚 TYPOGRAPHY - Conjunto de Estilos de Texto
 *
 * Typography é um objeto que contém todos os estilos de texto do app.
 * Cada propriedade define um estilo específico usado por componentes Material.
 *
 * 💡 COMO USAR:
 * Text(
 *     "Hello",
 *     style = MaterialTheme.typography.bodyLarge  // Aplica estilo bodyLarge
 * )
 *
 * 🎯 CUSTOMIZAÇÃO:
 * Atualmente, apenas bodyLarge está customizado.
 * Outros estilos usam valores padrão do Material Design 3.
 *
 * Para customizar mais estilos, descomente o código no final e ajuste.
 */
val Typography = Typography(
    /**
     * 📝 BODY LARGE - Texto de Corpo Grande
     *
     * Usado para:
     * - Parágrafos principais
     * - Texto de leitura prolongada
     * - Conteúdo descritivo
     *
     * 🔧 CONFIGURAÇÃO:
     * @property fontFamily FontFamily.Default
     *           - Roboto no Android (fonte padrão do sistema)
     *           - Você pode importar fontes customizadas se desejar
     *
     * @property fontWeight FontWeight.Normal
     *           - Peso normal (400)
     *           - Outros: Light (300), Bold (700), ExtraBold (800)
     *
     * @property fontSize 16.sp
     *           - Tamanho médio confortável para leitura
     *           - Material 3 recomenda 16sp para corpo de texto
     *
     * @property lineHeight 24.sp
     *           - Espaço entre linhas
     *           - 24sp = 1.5x o fontSize (16sp)
     *           - Melhora legibilidade (texto não fica "apertado")
     *
     * @property letterSpacing 0.5.sp
     *           - Espaçamento entre letras
     *           - 0.5sp = levemente espaçado
     *           - Melhora legibilidade em telas
     *
     * 💡 POR QUE ESSES VALORES?
     * São valores padrão recomendados pelo Material Design 3.
     * Resultado de estudos de legibilidade e acessibilidade.
     */
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,  // Roboto (padrão Android)
        fontWeight = FontWeight.Normal,   // Peso normal (400)
        fontSize = 16.sp,                 // Tamanho confortável
        lineHeight = 24.sp,               // Espaçamento entre linhas
        letterSpacing = 0.5.sp            // Espaçamento entre letras
    )

    /**
     * 💡 OUTROS ESTILOS DISPONÍVEIS (ATUALMENTE USANDO PADRÕES)
     *
     * Você pode descomentar e customizar estes estilos se precisar:
     *
     * ───────────────────────────────────────────────────────────────
     * DISPLAY - Títulos Enormes (usado raramente)
     * ───────────────────────────────────────────────────────────────
     * displayLarge = TextStyle(...)    // 57sp - Muito grande
     * displayMedium = TextStyle(...)   // 45sp
     * displaySmall = TextStyle(...)    // 36sp
     *
     * ───────────────────────────────────────────────────────────────
     * HEADLINE - Títulos Grandes
     * ───────────────────────────────────────────────────────────────
     * headlineLarge = TextStyle(...)   // 32sp - Título principal
     * headlineMedium = TextStyle(...)  // 28sp
     * headlineSmall = TextStyle(...)   // 24sp - Usado em TopAppBar
     *
     * ───────────────────────────────────────────────────────────────
     * TITLE - Títulos Médios
     * ───────────────────────────────────────────────────────────────
     * titleLarge = TextStyle(          // 22sp - Título de Card
     *     fontFamily = FontFamily.Default,
     *     fontWeight = FontWeight.Normal,
     *     fontSize = 22.sp,
     *     lineHeight = 28.sp,
     *     letterSpacing = 0.sp
     * ),
     * titleMedium = TextStyle(...)     // 16sp - Subtítulo
     * titleSmall = TextStyle(...)      // 14sp
     *
     * ───────────────────────────────────────────────────────────────
     * BODY - Texto do Corpo
     * ───────────────────────────────────────────────────────────────
     * bodyLarge = ... (já definido acima)
     * bodyMedium = TextStyle(...)      // 14sp - Texto secundário
     * bodySmall = TextStyle(...)       // 12sp - Texto pequeno
     *
     * ───────────────────────────────────────────────────────────────
     * LABEL - Labels e Botões
     * ───────────────────────────────────────────────────────────────
     * labelLarge = TextStyle(...)      // 14sp - Texto de botão
     * labelMedium = TextStyle(...)     // 12sp - Label pequeno
     * labelSmall = TextStyle(          // 11sp - Muito pequeno
     *     fontFamily = FontFamily.Default,
     *     fontWeight = FontWeight.Medium,
     *     fontSize = 11.sp,
     *     lineHeight = 16.sp,
     *     letterSpacing = 0.5.sp
     * )
     */
)

/**
 * 🎨 COMO ADICIONAR FONTES CUSTOMIZADAS
 *
 * Para usar fontes customizadas (não Roboto):
 *
 * 1. Adicione arquivos .ttf ou .otf em res/font/
 *    Exemplo: res/font/creepster_regular.ttf (fonte Halloween)
 *
 * 2. Crie uma FontFamily:
 *    val CreepsterFont = FontFamily(
 *        Font(R.font.creepster_regular, FontWeight.Normal)
 *    )
 *
 * 3. Use na Typography:
 *    headlineLarge = TextStyle(
 *        fontFamily = CreepsterFont,
 *        fontSize = 32.sp,
 *        ...
 *    )
 *
 * 💡 FONTES HALLOWEEN GRATUITAS:
 * - Creepster (Google Fonts)
 * - Eater (Google Fonts)
 * - Nosifer (Google Fonts)
 * - Butcherman (Google Fonts)
 *
 * Baixe de: https://fonts.google.com
 */
