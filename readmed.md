Com certeza\! Abaixo está a documentação completa do projeto **"Mestre de Puzzles da Mansão"** em formato Markdown.

-----

# 📜 Documentação do Projeto: Mestre de Puzzles da Mansão

## 🎯 Enunciado e Objetivo

[cite\_start]O projeto "Mestre de Puzzles da Mansão" foi desenvolvido como um aplicativo de gerenciamento de enigmas, simulando um sistema de *escape room* digital[cite: 136].

[cite\_start]**Objetivo:** Criar e gerenciar puzzles, rastrear o desempenho dos jogadores e gerar um ranking de superação[cite: 136, 138].

## 🛠️ Tecnologias Utilizadas

  * **Linguagem:** Kotlin
  * **Interface:** Jetpack Compose (UI declarativa e moderna)
  * **Persistência de Dados:** SQLite, implementado via Room
  * **Arquitetura:** MVVM (Model-View-ViewModel)
  * **Concorrência:** Kotlin Coroutines e Flow (para dados reativos)

## 🏗️ Arquitetura e Estrutura do Código

O projeto segue o padrão MVVM com uma clara separação de responsabilidades em camadas, promovendo código limpo, testável e manutenível.

### 1\. Camada de Dados (`.data`)

Responsável pela persistência e acesso bruto aos dados do puzzle.

| Arquivo | Função |
| :--- | :--- |
| **`Puzzle.kt`** | [cite\_start]**Entity**[cite: 139]: Modelo de dados Kotlin que mapeia a tabela `puzzle` no SQLite. [cite\_start]Contém campos como `title`, `timeLimitSec`, `solved`, e `attempts`[cite: 142, 144, 145, 146]. |
| **`PuzzleDao.kt`** | **Data Access Object:** Interface Room que define o CRUD (`@Insert`, `@Update`, `@Delete`) e as consultas de leitura (Flow), incluindo a lógica de ordenação do ranking. |
| **`AppDatabase.kt`** | **Database:** Classe abstrata principal do Room que gerencia as *Entities* e fornece a instância do `PuzzleDao`. |
| **`PuzzleRepository.kt`** | **Repository:** Camada intermediária que fornece uma API limpa ao ViewModel, isolando a lógica de acesso a dados (Room). |

### 2\. Camada de Lógica/App (`.ui.viewmodel` e Pacote Raiz)

Responsável por inicializar a arquitetura e gerenciar o estado da UI.

| Arquivo | Função |
| :--- | :--- |
| **`MestreDePuzzlesApplication.kt`** | **Application + Container:** Inicializa o `AppDatabase` e o `PuzzleRepository` como *singletons* globais. É registrado no `AndroidManifest.xml`. |
| **`PuzzleViewModel.kt`** | **ViewModel:** Gerencia o estado da aplicação (`PuzzleUiState`) através de um `StateFlow` reativo, combinando a lista principal e o ranking. Possui métodos para manipular os dados (e.g., `markPuzzleAsSolved`). |
| **`PuzzleViewModelFactory.kt`** | **Factory:** Classe utilitária que permite instanciar o `PuzzleViewModel` injetando o `PuzzleRepository` necessário. |

### 3\. Camada de Interface (`.ui`)

Responsável por exibir o estado (`PuzzleUiState`) e capturar as interações do usuário.

| Arquivo | Função |
| :--- | :--- |
| **`MainActivity.kt`** | **Activity/Navigation Host:** Ponto de entrada que configura o `ViewModel` e o `NavHost` para gerenciar as rotas. |
| **`PuzzleListScreen.kt`** | [cite\_start]**Tela Principal:** Exibe a lista completa de Puzzles e o Ranking de desempenho, ordenado por menor tempo e menor número de tentativas[cite: 138]. |
| **`PuzzleDetailScreen.kt`** | **Tela de Formulário:** Usada para Adicionar novos Puzzles e Editar/Detalhar puzzles existentes. Permite atualizar o `solved` e o `attempts` do enigma. |

## 🧩 Funcionalidades Implementadas

O projeto entrega os seguintes recursos essenciais:

1.  [cite\_start]**Criação/Edição de Puzzles:** Formulário para registrar o título, dicas progressivas e tempo limite[cite: 142, 143, 144].
2.  **Lista Principal:** Exibição de todos os enigmas cadastrados com seu status (`Pendente` ou `Resolvido`).
3.  [cite\_start]**Registro de Desempenho:** Campo para marcar o puzzle como `solved` e registrar o número de `attempts` (tentativas)[cite: 145, 146].
4.  [cite\_start]**Ranking de Desempenho:** Consulta otimizada na base de dados que filtra puzzles resolvidos e os ordena pelo menor tempo (`time_limit_sec` ASC) e menor número de tentativas (`attempts` ASC)[cite: 138].

## 📄 Definição da Tabela Principal (SQLite DDL)

[cite\_start]A tabela principal do banco de dados, conforme sugerido no enunciado[cite: 139], com os campos de metadados:

```sql
CREATE TABLE puzzle (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 title TEXT NOT NULL,
 [cite_start]hint1 TEXT, hint2 TEXT, hint3 TEXT,  -- Dicas progressivas [cite: 143]
 [cite_start]time_limit_sec INTEGER,             -- Tempo limite em segundos [cite: 144]
 [cite_start]solved INTEGER DEFAULT 0,           -- Status de resolução (0=Falso, 1=Verdadeiro) [cite: 145]
 [cite_start]attempts INTEGER DEFAULT 0          -- Número de tentativas [cite: 146]
);
```