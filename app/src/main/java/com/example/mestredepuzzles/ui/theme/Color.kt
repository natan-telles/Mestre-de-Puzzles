package com.example.mestredepuzzles.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 🎨 PALETA DE CORES DO APLICATIVO
 *
 * Este arquivo define todas as cores usadas no app.
 * Centralizar cores facilita manutenção e consistência visual.
 *
 * 📚 CONCEITOS:
 * - Color(0xFFRRGGBB): Formato hexadecimal ARGB
 * - 0xFF = Alpha (opacidade) máxima (100% opaca)
 * - RRGGBB = Red, Green, Blue (valores de 00 a FF)
 *
 * 💡 NAMING CONVENTION:
 * - Sufixo 80: Cores para tema claro (Light theme)
 * - Sufixo 40: Cores para tema escuro (Dark theme)
 * - 80/40 se refere ao "tint" (tonalidade) no Material Design 3
 */

// ═══════════════════════════════════════════════════════════════════════════
// CORES PADRÃO DO MATERIAL DESIGN 3
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🟣 PURPLE 80 - Roxo Claro
 *
 * Usado em temas claros como cor primária.
 * 0xFFD0BCFF: Roxo claro pastel (lavanda)
 */
val Purple80 = Color(0xFFD0BCFF)

/**
 * ⚫ PURPLE GREY 80 - Roxo Acinzentado Claro
 *
 * Usado como cor secundária em temas claros.
 * Tom neutro que complementa o roxo.
 */
val PurpleGrey80 = Color(0xFFCCC2DC)

/**
 * 🌸 PINK 80 - Rosa Claro
 *
 * Usado como cor terciária (acentos) em temas claros.
 */
val Pink80 = Color(0xFFEFB8C8)

/**
 * 🟣 PURPLE 40 - Roxo Escuro
 *
 * Usado em temas escuros como cor primária.
 * 0xFF6650a4: Roxo médio/escuro
 */
val Purple40 = Color(0xFF6650a4)

/**
 * ⚫ PURPLE GREY 40 - Roxo Acinzentado Escuro
 *
 * Usado como cor secundária em temas escuros.
 */
val PurpleGrey40 = Color(0xFF625b71)

/**
 * 🌸 PINK 40 - Rosa Escuro
 *
 * Usado como cor terciária em temas escuros.
 */
val Pink40 = Color(0xFF7D5260)

// ═══════════════════════════════════════════════════════════════════════════
// CORES CUSTOMIZADAS - TEMA HALLOWEEN
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🎃 HALLOWEEN ORANGE - Laranja Abóbora
 *
 * Cor principal do tema Halloween.
 * Representa abóboras, fogo, outono.
 *
 * 0xFFE65100: Laranja intenso e vibrante
 * - FF = Opaco (100%)
 * - E6 = Red (230/255)
 * - 51 = Green (81/255)
 * - 00 = Blue (0/255)
 */
val HalloweenOrange = Color(0xFFE65100)

/**
 * 🔮 DEEP PURPLE - Roxo Profundo
 *
 * Cor secundária do tema Halloween.
 * Representa mistério, magia, bruxaria.
 *
 * 0xFF4A148C: Roxo muito escuro e místico
 */
val DeepPurple = Color(0xFF4A148C)

/**
 * 🌑 MIDNIGHT BLACK - Preto Meia-Noite
 *
 * Cor de fundo principal.
 * Não é preto puro (#000000) para não cansar os olhos.
 *
 * 0xFF1A1A1A: Preto levemente acinzentado
 * - Mais confortável para leitura prolongada
 * - Permite contraste com preto verdadeiro se necessário
 */
val MidnightBlack = Color(0xFF1A1A1A)

/**
 * 👻 GHOST WHITE - Branco Fantasma
 *
 * Cor para texto sobre fundos escuros.
 * Não é branco puro para não criar contraste excessivo.
 *
 * 0xFFF5F5F5: Branco levemente acinzentado (off-white)
 * - Mais suave para os olhos
 * - Mantém legibilidade sem ser agressivo
 */
val GhostWhite = Color(0xFFF5F5F5)

// ═══════════════════════════════════════════════════════════════════════════
// CORES APLICADAS AO TEMA ESCURO (DARK THEME)
// ═══════════════════════════════════════════════════════════════════════════

/**
 * 🎨 PRIMARY DARK - Cor Primária do Tema Escuro
 *
 * Usada para:
 * - Botões principais
 * - FAB (Floating Action Button)
 * - Destaques importantes
 * - TopAppBar
 *
 * Valor: HalloweenOrange (laranja abóbora)
 */
val PrimaryDark = HalloweenOrange

/**
 * 🎨 SECONDARY DARK - Cor Secundária do Tema Escuro
 *
 * Usada para:
 * - Botões secundários
 * - Acentos e decorações
 * - Bordas especiais
 *
 * Valor: DeepPurple (roxo místico)
 */
val SecondaryDark = DeepPurple

/**
 * 🎨 TERTIARY DARK - Cor Terciária do Tema Escuro
 *
 * Usada para:
 * - Elementos de alerta
 * - Botão de deletar
 * - Avisos importantes
 *
 * 0xFFD4E058: Verde limão (contraste "assustador")
 * - Cor incomum que chama atenção
 * - Complementa o esquema Halloween de forma inesperada
 */
val TertiaryDark = Color(0xFFD4E058)

/**
 * 🎨 BACKGROUND DARK - Cor de Fundo
 *
 * Valor: MidnightBlack
 * Usado como cor de fundo geral do app em modo escuro.
 */
val BackgroundDark = MidnightBlack

/**
 * 🎨 SURFACE DARK - Cor de Superfícies
 *
 * Usado para Cards, Dialogs, Bottom Sheets, etc.
 * Mesma cor do background para visual unificado.
 *
 * Valor: MidnightBlack
 */
val SurfaceDark = MidnightBlack

/**
 * 🎨 ON PRIMARY DARK - Cor do Texto Sobre Primária
 *
 * Cor do texto/ícones sobre elementos com cor primária.
 * Exemplo: Texto em botões laranjas.
 *
 * Valor: GhostWhite (branco suave)
 * Garante legibilidade sobre laranja.
 */
val OnPrimaryDark = GhostWhite

/**
 * 🎨 ON SECONDARY DARK - Cor do Texto Sobre Secundária
 *
 * Cor do texto/ícones sobre elementos com cor secundária.
 * Exemplo: Texto em botões roxos.
 *
 * Valor: GhostWhite
 */
val OnSecondaryDark = GhostWhite

/**
 * 🎨 ON BACKGROUND DARK - Cor do Texto Sobre Fundo
 *
 * Cor padrão do texto sobre o fundo escuro.
 * Usada para todo texto do body, parágrafos, etc.
 *
 * Valor: GhostWhite
 * Alto contraste garante legibilidade.
 */
val OnBackgroundDark = GhostWhite