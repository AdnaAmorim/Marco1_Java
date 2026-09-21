# Boxing Game — Jogo Narrativo Interativo em Java

**UEFS — Universidade Estadual de Feira de Santana**

**Engenharia de Computação**

**EXA863 — MI — Programação | Período 2026.2**

**Projeto PBL — Fase 1**

**Dupla:** Anselmo Anjos e Adna Amorim.

## Sobre o projeto

Boxing Game é um jogo narrativo de terminal que combina drama esportivo, escolhas e elementos de RPG. O jogador assume o papel de um jovem de 18 anos que, após perder os pais e crescer com os avós, decide seguir o sonho do pai e construir uma carreira no boxe.

A jornada passa por academias, lutas clandestinas e campeonatos. Além de preparar o corpo para o ringue, o protagonista precisa lidar com amizades, romances, rivalidades e propostas de um empresário ligado à corrupção no esporte. Suas decisões alteram atributos, recursos, relacionamentos e possibilidades posteriores da história.

O objetivo é chegar ao Campeonato Mundial e enfrentar Anthony, decidindo até onde vale a pena ir para conquistar o cinturão e preservar o legado da família.

| Aspecto | Proposta |
| --- | --- |
| Gênero | Drama esportivo e aventura narrativa com elementos de RPG |
| Ambientação | Universo contemporâneo de boxe, entre Cidade Natal, Cidade A e Cidade B |
| Protagonista | Jovem boxeador com nome, sexo e cor dos olhos configurados pelo jogador |
| Estrutura | Introdução, dez capítulos, exploração livre entre os locais disponíveis e passagem de dias e horas |
| Visual | Interface textual com arte ASCII/Unicode, molduras, menus por teclado e animações no terminal |
| Som | Não há reprodução de áudio; os sons fazem parte das descrições narrativas |
| Público-alvo | Pessoas interessadas em histórias de esporte, escolhas e gestão de personagem; a narrativa aborda violência e corrupção |

## Personagens e caminhos narrativos

Os personagens relevantes incluem Olivia, estudante de fisioterapia; Mestre Smith, treinador veterano; Chris, lutador surdo e veloz; Victor Noor, empresário do submundo das apostas; James, analista de desempenho; Alexandra, jornalista; e Anthony, rival e campeão mundial.

As escolhas permitem construir afinidade com personagens como Olivia, Chris, Mestre Smith, James e Noor. Decisões anteriores também liberam opções de romance, apoio estratégico e investigação.

O desfecho combina o resultado da luta mundial com as decisões sobre Noor. Existem quatro finais principais: dois desfechos trágicos ligados ao acordo, a conquista do título mundial e a continuidade do legado após a derrota. A investigação e os relacionamentos acrescentam informações ao encerramento.

## Mecânicas

- **Atributos:** saúde e energia de 0 a 100; força, agilidade, resistência e inteligência de 0 a 10. Os estilos de combate aplicam modificadores temporários.
- **Treinamento:** atividades em casa e na academia, com gasto de energia, passagem de tempo e limite compartilhado de cinco treinos por dia.
- **Descanso:** dormir avança oito horas e restaura saúde e energia. Há chance de perder um ponto de habilidade ao descansar.
- **Economia:** dinheiro para treinos, viagens, compras e inscrições em campeonatos; prêmios obtidos nas lutas.
- **Inventário:** alimentos, suplementos e itens especiais da narrativa.
- **Combate:** escolha entre Velocista, Nocauteador e Contragolpeador. O resultado é calculado a partir dos atributos e estilos; os rounds narram esse resultado, sem comandos de ataque durante a luta.
- **Progressão:** capítulos dependem de acontecimentos anteriores, locais visitados e quantidade de lutas disputadas. Algumas escolhas também dependem de afinidade e decisões anteriores.

## Organização do código

O projeto é organizado segundo o padrão MVC, com classes e responsabilidades distribuídas nos seguintes pacotes:

```text
src/main/java/com/exa863/anselmo_adna/
├── Main.java             # Ponto de entrada
├── controller/           # Controle do jogo, cenas, narrativa e combate
│   └── cenas/            # Controle de abertura e cutscenes
├── model/
│   ├── character/        # Jogador e personagens
│   ├── combat/           # Lutadores, estilos, rounds e resultados
│   ├── narrativa/        # Diálogos, capítulos e dependências da história
│   ├── stats/            # Atributos, itens, inventário e relacionamentos
│   └── world/            # Locais, tempo e estados do jogo
├── view/console/         # Telas, menus e interação com o terminal
└── utils/                # Formatação de textos e caixas
```

São utilizados herança e polimorfismo nos estilos de luta e capítulos, encapsulamento dos atributos e composição entre os componentes da partida. O grafo de capítulos organiza os pré-requisitos da narrativa. Algumas regras de treino, compras e recompensas ainda estão nas telas e podem ser separadas melhor dos componentes de apresentação.

### Tecnologias

| Tecnologia | Versão no projeto | Uso |
| --- | --- | --- |
| Java | 21 | Linguagem e execução |
| Maven | Instalação externa | Compilação e gerenciamento de dependências |
| JLine | 3.29.0 | Terminal, leitura de teclas e menus |
| JGraphT | 1.5.2 | Grafo de dependências entre capítulos |
| JUnit Jupiter | 5.10.2 | Dependência de testes; testes ainda não implementados |

Não é utilizada uma engine de jogos.

## Requisitos para executar

- **Java 21:** para compilar, instale um **JDK 21**, que inclui `java` e `javac`. Para apenas jogar uma distribuição já compilada, basta um ambiente de execução compatível com Java 21.
- **Apache Maven:** necessário para compilar e preparar a distribuição a partir do código-fonte. O repositório não contém um Maven Wrapper completo.
- **Internet na preparação inicial:** o Maven precisa baixar as dependências e os plugins que ainda não estiverem no cache local.
- **Terminal interativo externo**, com suporte a sequências ANSI, caracteres Unicode e teclas direcionais.
- Janela com pelo menos **80 colunas e 32 linhas**. Uma janela maior melhora a leitura dos textos.

### Atenção ao terminal

**Não execute pelo console Run/Debug do IntelliJ IDEA ou por consoles semelhantes de IDEs.** Esses consoles não oferecem o comportamento de terminal interativo exigido pelo jogo, e a leitura das setas, limpeza de tela e formatação podem falhar.

No Windows, abra o **Windows Terminal com PowerShell** e execute os comandos por lá. O painel Terminal de uma IDE é diferente do console Run/Debug, mas sua compatibilidade não está garantida nesta versão; para jogar, utilize uma janela externa.

Use uma fonte monoespaçada que exiba os caracteres das molduras. A tela inicial verifica as dimensões da janela e permite continuar com Enter mesmo abaixo do mínimo, mas o conteúdo pode ficar desalinhado ou cortado.

## Como compilar e abrir pelo código-fonte

Os exemplos abaixo usam PowerShell no Windows.

### Onde baixar e como preparar o Maven

Com o **JDK 21 já instalado**, siga o [guia oficial de instalação do Apache Maven](https://maven.apache.org/install.html). No Windows, o caminho básico é:

1. Abra a [página oficial de downloads](https://maven.apache.org/download.cgi) e baixe a distribuição estável do Maven na opção **Binary zip archive**.
2. Extraia o ZIP em uma pasta permanente, como `C:\Ferramentas`. A pasta extraída terá um nome como `apache-maven-3.9.x`, conforme a versão baixada.
3. Nas **Variáveis de Ambiente** do Windows, adicione ao `Path` a pasta `bin` do Maven extraído, por exemplo `C:\Ferramentas\apache-maven-3.9.x\bin`. Substitua o exemplo pelo caminho real da sua instalação.
4. Confira se `JAVA_HOME` aponta para a pasta do **JDK 21**, sem o `\bin` no final.
5. Abra uma nova janela do PowerShell e execute `mvn -version`. A saída deve mostrar a versão do Maven e o Java 21 utilizado por ele.

Para Linux e macOS, o mesmo guia oficial apresenta as opções de instalação por gerenciador de pacotes.

O Maven só é necessário para preparar o jogo a partir do código-fonte. Quem recebeu o JAR e a pasta de dependências já prontos pode seguir diretamente a seção **Como executar uma versão já compilada**.

### 1. Confira as instalações

Depois de instalar o JDK e o Maven e disponibilizá-los no `PATH`, abra uma nova janela do terminal:

```powershell
java -version
javac -version
mvn -version
```

Confira se o Java e o compilador são da versão 21 e se o Maven está usando esse mesmo JDK. Caso necessário, ajuste `JAVA_HOME` para a pasta do JDK 21 e inclua sua pasta `bin` no `PATH`.

### 2. Entre na pasta do projeto

Baixe ou clone o repositório e entre na pasta que contém o arquivo `pom.xml`. Substitua o caminho do exemplo pelo caminho real:

```powershell
cd "C:\caminho\Marco1_Java"
```

### 3. Compile e copie as dependências

```powershell
mvn clean package dependency:copy-dependencies -DincludeScope=runtime
```

Esse comando compila o código, gera o JAR e copia as bibliotecas necessárias para `target/dependency`. A primeira execução pode demorar por causa dos downloads.

O resultado esperado é:

```text
target/
├── PBLGame-1.0-SNAPSHOT.jar
└── dependency/
    └── ... bibliotecas necessárias à execução
```

### 4. Abra o jogo

Ainda na pasta do projeto, execute:

```powershell
java -Dfile.encoding=UTF-8 -cp "target/PBLGame-1.0-SNAPSHOT.jar;target/dependency/*" com.exa863.anselmo_adna.Main
```

No Linux/macOS, o separador do classpath é `:`:

```sh
java -Dfile.encoding=UTF-8 -cp "target/PBLGame-1.0-SNAPSHOT.jar:target/dependency/*" com.exa863.anselmo_adna.Main
```

O comando para outros sistemas é fornecido como referência; o funcionamento interativo nesses ambientes não foi validado nesta documentação.

## Como executar uma versão já compilada

Quem recebe a distribuição completa **não precisa instalar Maven nem compilar o código**. É necessário ter Java compatível com a versão 21 e receber tanto o JAR do jogo quanto as dependências.

Para preparar essa distribuição, compile conforme a seção anterior e reúna estes arquivos em uma pasta:

```text
BoxingGame/
├── PBLGame-1.0-SNAPSHOT.jar
└── dependency/
    └── ... todos os JARs de target/dependency
```

Essa pasta pode ser compactada e compartilhada. Após extrair **todo o conteúdo**, abra um terminal externo, entre nela e execute:

```powershell
cd "C:\caminho\BoxingGame"
java -Dfile.encoding=UTF-8 -cp "PBLGame-1.0-SNAPSHOT.jar;dependency/*" com.exa863.anselmo_adna.Main
```

**Não basta dar dois cliques no JAR ou usar `java -jar`.** A configuração atual não gera um JAR executável com `Main-Class` no manifesto nem incorpora as dependências. Por isso, os comandos acima informam a classe principal e a pasta das bibliotecas explicitamente.

Depois que a distribuição estiver completa, o jogo não precisa de conexão com a internet para funcionar.

## Guia rápido de jogo

1. No menu inicial, selecione **Jogar (Iniciar Carreira)**. Também estão disponíveis tutorial, créditos e saída.
2. Informe o nome do protagonista e escolha sexo e cor dos olhos.
3. Leia a introdução e avance com **Enter**.
4. No mapa, visite primeiro a **Casa** para iniciar o capítulo Origens. Novos locais aparecem conforme a história avança.
5. Use **↑ / ↓** para selecionar opções e **Enter** para confirmar. Nas conversas, leia as alternativas: elas podem alterar afinidades, atributos e caminhos futuros.
6. Treine em casa ou na academia, acompanhe saúde e energia e descanse quando necessário. Dormir avança oito horas, portanto nem sempre muda o dia imediatamente.
7. Na Academia de Boxe, escolha uma competição e uma postura. O circuito local permite obter dinheiro; os circuitos Estadual e Nacional contribuem para o acesso ao Mundial. Há um limite de uma luta de boxe por dia do jogo.
8. Volte aos locais após avançar na carreira: os capítulos são acionados ao entrar neles. A Loja vende consumíveis, e a opção **Abrir Mochila (Inventário)** permite usá-los.
9. Para disputar o Mundial, são exigidas pelo menos duas lutas estaduais, uma nacional, R$ 3.000 de inscrição e 70 pontos de saúde e energia, além da liberação do acesso pela narrativa.
10. Ao concluir a luta final, acompanhe o desfecho e o resumo dos relacionamentos. O jogo oferece retorno ao menu principal.

## Estado atual e limitações

- **Modo de desenvolvimento ativo:** `Console.MODO_DEV` está definido como `true`. O protagonista começa com R$ 20.000 e habilidades no máximo, e a narração animada das lutas é pulada. Para jogar com os valores iniciais normais, altere essa opção para `false` em `src/main/java/com/exa863/anselmo_adna/view/console/Console.java` **antes de compilar**. Isso não elimina todos os problemas conhecidos abaixo.
- **Sem salvamento/carregamento:** o progresso existe apenas em memória e é perdido quando o programa termina. Esse requisito da proposta geral ainda está pendente.
- **Sem testes de unidade implementados:** o JUnit está declarado no Maven, mas não há uma suíte de testes em `src/test`. Uma compilação bem-sucedida não comprova a validação das regras do jogo.
- **Empates:** há uma falha conhecida na geração da narração que pode interromper a partida quando uma luta termina empatada, inclusive no modo de desenvolvimento.
- **Sessões longas:** as trocas de tela acumulam chamadas e podem causar estouro da pilha de execução.
- **Contagem de lutas:** os requisitos de progressão consideram participações, inclusive derrotas, embora alguns diálogos descrevam essas participações como vitórias.
- **Treino no máximo:** escolher um atributo já no limite na academia ainda consome o valor da entrada e uma tentativa diária.
- **Abrir uma Academia:** a opção mostra os requisitos, mas a construção ainda não foi implementada.
- **Apresentação:** telas usam larguras fixas; nomes longos, fontes incompatíveis e janelas pequenas podem prejudicar o alinhamento. Não há interface gráfica nem áudio.

### Problemas ao abrir

| Problema | O que conferir |
| --- | --- |
| `java` ou `mvn` não reconhecido | Instalação e `PATH`; abra um novo terminal após configurar |
| Erro de versão do Java | Compare `java -version`, `javac -version` e o Java mostrado por `mvn -version` |
| Classe principal não encontrada | Pasta atual, nome do JAR e comando completo com `-cp` |
| `NoClassDefFoundError` para JLine ou JGraphT | Presença de todos os JARs na pasta `dependency` e inclusão dela no classpath |
| Mensagem sobre ausência de manifesto principal | Use o comando com `-cp` e `com.exa863.anselmo_adna.Main`, em vez de `java -jar` |
| Setas, molduras ou limpeza de tela não funcionam | Execute em terminal externo, amplie a janela e confira a fonte |

## Relação com o PBL — Fase 1

| Requisito | Situação no código atual |
| --- | --- |
| Menu com nova partida, instruções, créditos e saída | Implementado |
| Configuração do protagonista e introdução | Implementadas |
| Ao menos dez unidades narrativas | Dez classes de capítulos, além da introdução |
| Diálogos, escolhas e consequências | Implementados com alterações de estado e opções condicionais |
| Ao menos cinco personagens relevantes | Presentes na narrativa |
| Vínculos com pelo menos três personagens | Sistema de afinidade aplicado a múltiplos personagens |
| Pelo menos três atributos variáveis | Seis atributos |
| Recursos gerenciáveis | Dinheiro, energia, inventário e limites diários |
| Condições de acesso | Dependências de capítulos, participações em lutas e escolhas anteriores |
| Ao menos três finais e retorno ao menu | Quatro finais principais implementados; fluxo completo ainda requer validação em execução |
| POO e MVC | Organização presente, com regras ainda concentradas em algumas views |
| Testes de unidade | Pendentes |

A Fase 1 também exige **diagrama de classes em dupla**, separado e anexado ao relatório, e **relatório individual no padrão SBC, de seis a dez páginas**. Este README documenta o projeto e seu uso; não substitui esses entregáveis.

Os procedimentos de execução foram descritos a partir do `pom.xml` e da estrutura do código. Não foram executados builds ou testes interativos para a elaboração deste documento.
