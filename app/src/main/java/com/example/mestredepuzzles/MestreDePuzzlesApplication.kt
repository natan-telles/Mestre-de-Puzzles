package com.example.mestredepuzzles

import android.app.Application
import androidx.room.Room
import com.example.mestredepuzzles.data.AppDatabase
import com.example.mestredepuzzles.data.PuzzleRepository

/**
 * 📦 INTERFACE DO CONTAINER DE DEPENDÊNCIAS
 *
 * Define o contrato para o container que fornece dependências (objetos) para toda a aplicação.
 * É como um "armazém central" de onde pegamos os objetos que precisamos.
 *
 * 🎯 PROPÓSITO:
 * - Definir quais dependências estarão disponíveis no app
 * - Facilitar testes (podemos criar implementações fake desta interface)
 * - Centralizar criação de objetos (Singleton pattern)
 *
 * 💡 INJEÇÃO DE DEPENDÊNCIAS MANUAL:
 * Este projeto usa injeção manual (sem bibliotecas como Hilt ou Dagger).
 * Para apps grandes, considere usar Hilt para automação.
 *
 * 📚 CONCEITO DE INTERFACE:
 * A interface define "O QUE" existe, não "COMO" é criado.
 * A implementação (AppDataContainer) define o "COMO".
 */
interface AppContainer {
    /**
     * 🏪 Repositório de Puzzles
     *
     * Qualquer parte do app pode acessar esta propriedade para obter o Repository.
     * É a única instância (Singleton) que existe no app.
     */
    val puzzleRepository: PuzzleRepository
}

/**
 * 🏗️ IMPLEMENTAÇÃO DO CONTAINER DE DEPENDÊNCIAS
 *
 * Esta classe cria e gerencia as instâncias reais dos objetos necessários.
 * Usa o padrão Lazy Initialization: objetos só são criados quando primeiro acessados.
 *
 * 🎯 PROPÓSITO:
 * - Criar o banco de dados Room uma única vez (Singleton)
 * - Criar o Repository e injetar o DAO nele
 * - Garantir que todos usem as mesmas instâncias
 *
 * 💡 LAZY INITIALIZATION:
 * by lazy { ... } = cria o objeto apenas na primeira vez que é acessado
 * Benefícios: economiza memória e melhora performance de inicialização
 *
 * @param applicationContext Contexto da aplicação Android (necessário para criar o banco)
 */
class AppDataContainer(private val applicationContext: Application) : AppContainer {

    /**
     * 🗄️ INSTÂNCIA ÚNICA DO BANCO DE DADOS
     *
     * Cria o banco de dados SQLite usando Room.
     *
     * 💡 by lazy:
     * - O banco só é criado quando alguém chama database pela primeira vez
     * - Após criado, a mesma instância é reutilizada sempre
     * - Thread-safe: múltiplas threads não criam múltiplos bancos
     *
     * 🔧 Room.databaseBuilder():
     * @param context Contexto da aplicação (onde salvar o banco)
     * @param klass Classe do banco (AppDatabase::class.java)
     * @param name Nome do arquivo físico no dispositivo ("puzzle_database")
     *
     * 📁 LOCALIZAÇÃO:
     * O banco é salvo em: /data/data/com.example.mestredepuzzles/databases/puzzle_database
     */
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "puzzle_database"  // 📄 Nome do arquivo do banco de dados no dispositivo
        ).build()
    }

    /**
     * 🏪 INSTÂNCIA ÚNICA DO REPOSITÓRIO
     *
     * Cria o Repository e injeta o DAO do banco de dados nele.
     *
     * 💡 INJEÇÃO DE DEPENDÊNCIA:
     * Repository precisa de um DAO para funcionar.
     * Aqui "injetamos" o DAO: PuzzleRepository(database.puzzleDao())
     *
     * 🔄 FLUXO DE CRIAÇÃO:
     * 1. Primeira vez que puzzleRepository é acessado
     * 2. lazy executa e chama database.puzzleDao()
     * 3. Isso força criação do database (se ainda não existe)
     * 4. DAO é obtido e passado para o Repository
     * 5. Repository é criado e retornado
     * 6. Próximas vezes: retorna a mesma instância
     */
    override val puzzleRepository: PuzzleRepository by lazy {
        PuzzleRepository(database.puzzleDao())
    }
}

/**
 * 🚀 CLASSE PRINCIPAL DA APLICAÇÃO ANDROID
 *
 * Esta classe é o ponto de entrada da aplicação.
 * É criada ANTES de qualquer Activity e existe durante TODA a vida do app.
 *
 * 🎯 PROPÓSITO:
 * - Inicializar recursos globais (banco de dados, configurações, etc)
 * - Fornecer acesso ao container de dependências para todo o app
 * - Executar código que precisa rodar apenas uma vez
 *
 * 💡 CICLO DE VIDA:
 * onCreate() → App roda → onTerminate() → App fecha
 *
 * 📱 REGISTRO NO MANIFEST:
 * No AndroidManifest.xml, esta classe é registrada como:
 * <application android:name=".MestreDePuzzlesApplication" ...>
 * Isso diz ao Android para usar esta classe como Application customizada.
 *
 * 🌍 ESCOPO GLOBAL:
 * Qualquer parte do app pode acessar esta classe assim:
 * (application as MestreDePuzzlesApplication).container.puzzleRepository
 */
class MestreDePuzzlesApplication : Application() {

    /**
     * 📦 CONTAINER DE DEPENDÊNCIAS
     *
     * lateinit = será inicializado depois, mas antes de ser usado
     *
     * ⚠️ IMPORTANTE:
     * Não inicialize aqui diretamente (var container = ...)
     * Deve ser inicializado no onCreate() quando o contexto está pronto
     *
     * 💡 ACESSO:
     * Em qualquer Activity/ViewModel, faça:
     * val repository = (application as MestreDePuzzlesApplication).container.puzzleRepository
     */
    lateinit var container: AppContainer

    /**
     * 🎬 MÉTODO CHAMADO QUANDO A APLICAÇÃO É CRIADA
     *
     * Este método executa UMA ÚNICA VEZ quando o app é iniciado.
     * É o melhor lugar para inicializar recursos globais.
     *
     * 🔄 FLUXO:
     * 1. Android cria a Application
     * 2. onCreate() é chamado
     * 3. Container é inicializado
     * 4. Activities podem agora acessar o container
     *
     * 💡 BOAS PRÁTICAS:
     * - Não faça operações longas aqui (não bloquear inicialização)
     * - Não faça chamadas de rede aqui
     * - Apenas inicialize objetos necessários para todo o app
     */
    override fun onCreate() {
        super.onCreate()
        // 📦 Cria o container de dependências, passando o contexto da aplicação
        container = AppDataContainer(this)
    }
}