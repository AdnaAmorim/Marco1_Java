# Boxing Game — Jogo Narrativo Interativo em Java

**UEFS — Universidade Estadual de Feira de Santana**

**Engenharia de Computação**

**EXA863 — MI — Programação | Período 2026.2**

**Projeto PBL — Fase 1**

**Dupla:** Anselmo Anjos e Adna Amorim.

> 📦 **Download rápido (executável pronto para jogar):**  
> Baixe a versão compilada (`.jar`) diretamente na página de **[Releases do Projeto](https://github.com/AdnaAmorim/Marco1_Java/releases)**.

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
- **Combate:** escolha entre Velocista, Nocauteador e Contragolpeador. O resultado é calculado a partir dos atributos e estilos; os rounds narram esse resultado com animações no terminal (com opção de pular direto para o resultado final nas configurações).
- **Progressão:** capítulos dependem de acontecimentos anteriores, locais visitados e quantidade de lutas disputadas. Algumas escolhas também dependem de afinidade e decisões anteriores.
- **Sistema de Saves e Persistência:** múltiplos slots de salvamento manuais (`saves/slot_*.xml`), salvamento automático inteligente (`saves/autosave.xml`) com detecção automática do save mais recente no Menu Inicial ("Continuar"), além de exportação/importação e tratamento de exceções de persistência (`FalhaSalvamentoException`, `FalhaCarregamentoException`, `DadosInvalidosException`).
- **Configurações e Acessibilidade:** menu de configurações com persistência em `configuracao.xml`, permitindo alternar auto-save, exibição da logo inicial e velocidade de exibição/pulo de animações de combate.
- **Progresso Global e Conquistas:** rastreamento contínuo em `progresso.xml`, registrando finais desbloqueados e conquistas da carreira (como *Primeiros Passos*).

## Organização do código

O projeto é organizado segundo o padrão MVC, com classes e responsabilidades distribuídas nos seguintes pacotes:

```text
src/main/java/com/exa863/anselmo_adna/
├── Main.java             # Ponto de entrada
├── controller/           # Controle do jogo, cenas, narrativa, combate e saves
│   ├── cenas/            # Controle de abertura e cutscenes
│   ├── SaveGameController.java   # Gerenciamento de slots e autosave
│   └── XmlFileController.java    # Serialização e desserialização XML (Jackson)
├── exception/            # Exceções customizadas de persistência e validação
├── persistencia/         # Modelos e manipuladores de configurações, progresso e conquistas
├── model/
│   ├── character/        # Jogador e personagens
│   ├── combat/           # Lutadores, estilos, rounds e resultados
│   ├── narrativa/        # Diálogos, capítulos e dependências da história
│   ├── stats/            # Atributos, itens, inventário e relacionamentos
│   └── world/            # Locais, tempo e estados do jogo (GameState)
├── view/console/         # Telas, menus navegáveis, saves e interação com o terminal
└── utils/                # Formatação de textos e caixas
```

São utilizados herança e polimorfismo nos estilos de luta e capítulos, encapsulamento dos atributos e composição entre os componentes da partida. O grafo de capítulos organiza os pré-requisitos da narrativa.

### Tecnologias

| Tecnologia | Versão no projeto | Uso |
| --- | --- | --- |
| Java | 21 | Linguagem e execução |
| Maven | Instalação externa | Compilação e gerenciamento de dependências |
| JLine | 3.29.0 | Terminal, leitura de teclas e menus interativos |
| JGraphT | 1.5.2 | Grafo de dependências entre capítulos |
| Jackson Dataformat XML | 2.17.2 | Serialização e persistência de dados em XML |
| JUnit Jupiter | 5.10.2 | Testes automatizados de unidade e integração |

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

### Acentos e caracteres especiais (UTF-8)

O jogo usa acentos, setas e caracteres especiais como `╔`, `║`, `►` e `✓`. Para exibi-los corretamente, use um terminal configurado para **UTF-8** e uma fonte com suporte a esses símbolos. UTF-8 é a codificação de texto; não confundir com UTC, que é uma referência de horário.

Se aparecerem letras trocadas ou símbolos estranhos no Windows, configure a sessão do terminal para UTF-8 e abra o JAR assim, na pasta onde ele está:

```powershell
chcp 65001
java -Dfile.encoding=UTF-8 -jar PBLGame-1.5.jar
```

Se estiver na pasta do projeto, use `target/PBLGame-1.5.jar` no comando. No Linux/macOS, configure o terminal e a localidade do sistema para UTF-8; `chcp` é exclusivo do Windows.

Se os caracteres aparecerem como quadradinhos mesmo com UTF-8, confira a fonte do terminal: mudar a codificação não adiciona símbolos que a fonte não possui. Se as molduras estiverem cortadas, aumente também o tamanho da janela.

## Como compilar e abrir pelo código-fonte

Os exemplos abaixo usam PowerShell no Windows.

### Onde baixar e como preparar o Maven

**As orientações abaixo são um resumo genérico**, com exemplos para Windows. Os caminhos, a versão disponível e a configuração podem variar entre computadores. Para instalar, **siga o [guia oficial de instalação do Apache Maven](https://maven.apache.org/install.html)**, usando as instruções correspondentes ao seu sistema operacional. O guia oficial é a referência principal; esta seção serve apenas para orientar o que será necessário.

Com o **JDK 21 já instalado**, o processo básico no Windows é:

1. Abra a [página oficial de downloads](https://maven.apache.org/download.cgi) e baixe a distribuição estável do Maven na opção **Binary zip archive**.
2. Extraia o ZIP em uma pasta permanente, como `C:\Ferramentas`. A pasta extraída terá um nome como `apache-maven-3.9.x`, conforme a versão baixada.
3. Nas **Variáveis de Ambiente** do Windows, adicione ao `Path` a pasta `bin` do Maven extraído, por exemplo `C:\Ferramentas\apache-maven-3.9.x\bin`. Substitua o exemplo pelo caminho real da sua instalação.
4. Confira se `JAVA_HOME` aponta para a pasta do **JDK 21**, sem o `\bin` no final.
5. Abra uma nova janela do PowerShell e execute `mvn -version`. A saída deve mostrar a versão do Maven e o Java 21 utilizado por ele.

Para Linux e macOS, o mesmo guia oficial apresenta as opções de instalação por gerenciador de pacotes.

O Maven só é necessário para preparar o jogo a partir do código-fonte. Quem quiser apenas jogar pode baixar o JAR diretamente na [página de releases do projeto](https://github.com/AdnaAmorim/Marco1_Java/releases) e seguir a seção **Como executar uma versão já compilada**.

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

### 3. Compile e abra o jogo

```powershell
mvn compile exec:java
```

Esse é o jeito mais simples de jogar pelo código-fonte: o Maven baixa as dependências necessárias (incluindo JLine, JGraphT e Jackson Dataformat XML), compila e inicia o jogo. A primeira execução pode demorar por causa dos downloads. Use um terminal externo.

### 4. Executar os testes automatizados

Para rodar toda a suíte de testes unitários e de integração:

```powershell
mvn test
```

### 5. Gere um JAR para compartilhar

Para reunir o jogo e todas as dependências de execução em um único arquivo executável (fat JAR):

```powershell
mvn clean package
```

O arquivo final é `target/PBLGame-1.5.jar`. O Maven Shade inclui as bibliotecas e configura a classe principal. Compartilhe esse arquivo; o arquivo com prefixo `original-`, também gerado no processo, não contém as dependências.

## Como executar uma versão já compilada

Baixe a versão publicada diretamente na página de **[Releases do GitHub](https://github.com/AdnaAmorim/Marco1_Java/releases)**.

Você só precisa baixar o arquivo **`PBLGame-1.5.jar`** (disponível na seção de *Assets* da release) e ter o **Java 21 instalado**. Não precisa de Maven, código-fonte nem pasta de dependências.

Abra um terminal externo na pasta onde salvou o JAR e execute:

```powershell
cd "C:\caminho\BoxingGame"
java -jar PBLGame-1.5.jar
```

Se ainda estiver na pasta do projeto, use `java -jar target/PBLGame-1.5.jar`.

**Abra pelo terminal, não por duplo clique:** no Windows, o duplo clique pode usar o Java sem uma janela de console, impedindo a interação. O JAR inclui as bibliotecas do jogo, mas não inclui o próprio Java. Confira `java -version` antes de abrir. O comando é o mesmo no Linux/macOS, mas a interação nesses ambientes ainda precisa de validação.

O jogo compilado não precisa de conexão com a internet para funcionar.

## Guia rápido de jogo

1. No menu inicial, selecione **Jogar (Iniciar Carreira)** ou **Continuar** (caso já exista um save ou autosave recente). Também estão disponíveis gerenciamento de saves (**Carregar partida**), **Configurações**, **Tutorial (Guia do Boxeador)**, **Créditos** e **Sair**.
2. Ao iniciar nova carreira, informe o nome do protagonista e escolha sexo e cor dos olhos.
3. Leia a introdução e avance com **Enter**.
4. No mapa, visite primeiro a **Casa** para iniciar o capítulo Origens. Novos locais aparecem conforme a história avança.
5. Use **↑ / ↓** para selecionar opções e **Enter** para confirmar. Nas conversas, leia as alternativas: elas podem alterar afinidades, atributos e caminhos futuros.
6. Treine em casa ou na academia, acompanhe saúde e energia e descanse quando necessário. Dormir avança oito horas, portanto nem sempre muda o dia imediatamente.
7. Na Academia de Boxe, escolha uma competição e uma postura. O circuito local permite obter dinheiro; os circuitos Estadual e Nacional contribuem para o acesso ao Mundial. Há um limite de uma luta de boxe por dia do jogo.
8. Durante o combate, as animações e narrativas de rounds são exibidas na íntegra (ou puladas diretamente para o resultado caso ativado no menu de configurações).
9. Volte aos locais após avançar na carreira: os capítulos são acionados ao entrar neles. A Loja vende consumíveis, e a opção **Abrir Mochila (Inventário)** permite usá-los.
10. O jogo salva seu progresso automaticamente ao passar por cutscenes importantes e ao término de combates (`autosave.xml`), além de permitir salvar manualmente pelo menu do jogo a qualquer momento em slots dedicados.
11. Para disputar o Mundial, são exigidas pelo menos duas lutas estaduais, uma nacional, R$ 3.000 de inscrição e 70 pontos de saúde e energia, além da liberação do acesso pela narrativa.
12. Ao concluir a luta final, acompanhe o desfecho, o resumo dos relacionamentos, o registro de finais conquistados e conquistas desbloqueadas. O jogo oferece retorno seguro ao menu principal.

## Problemas ao abrir

| Problema | O que conferir |
| --- | --- |
| `java` ou `mvn` não reconhecido | Instalação e `PATH`; abra um novo terminal após configurar |
| Erro de versão do Java | Compare `java -version`, `javac -version` e o Java mostrado por `mvn -version` |
| JAR não encontrado | Pasta atual e caminho informado depois de `java -jar` |
| `NoClassDefFoundError: XmlMapper` | O Maven não baixou as dependências ou executou sem compilar. Execute `mvn compile exec:java` ou rode via JAR empacotado |
| `NoClassDefFoundError` para JLine ou JGraphT | Use o JAR completo gerado por `mvn clean package`, sem o prefixo `original-` |
| Mensagem sobre ausência de manifesto principal | Confira se está usando o JAR completo atualizado, e não um artefato antigo ou com prefixo `original-` |
| Setas, molduras ou limpeza de tela não funcionam | Execute em terminal externo, amplie a janela e confira a fonte |

## Relação com o PBL — Fase 1

| Requisito | Situação no código atual |
| --- | --- |
| Menu com nova partida, carregar save, configurações, instruções, créditos e saída | Implementado (com suporte a atalho "Continuar" para o save mais recente) |
| Configuração do protagonista e introdução | Implementadas |
| Ao menos dez unidades narrativas | Dez classes de capítulos, além da introdução |
| Diálogos, escolhas e consequências | Implementados com alterações de estado e opções condicionais |
| Ao menos cinco personagens relevantes | Presentes na narrativa |
| Vínculos com pelo menos três personagens | Sistema de afinidade aplicado a múltiplos personagens |
| Pelo menos três atributos variáveis | Seis atributos |
| Recursos gerenciáveis | Dinheiro, energia, inventário e limites diários |
| Condições de acesso | Dependências de capítulos, participações em lutas e escolhas anteriores |
| Ao menos três finais e retorno ao menu | Quatro finais principais implementados; rastreamento global de finais concluídos |
| Persistência e Recuperação de Estado | Implementada em XML com múltiplos slots, autosave, tratamento de exceções robusto e configurações |
| POO e MVC | Organização arquitetural clara entre model, view, controller, persistencia e exception |
| Testes de unidade | Implementados com JUnit 5 (cobrindo regras críticas de atributos, inventário, combate, estilos de boxe, passagem de tempo, grafo da história e persistência XML/saves) |

A Fase 1 também exige **diagrama de classes em dupla**, separado e anexado ao relatório, e **relatório individual no padrão SBC, de seis a dez páginas**. Este README documenta o projeto e seu uso; não substitui esses entregáveis.

Uma compilação bem-sucedida valida a geração do artefato, mas não substitui testes das regras nem uma partida completa no terminal.
