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
| Visual | Interface gráfica responsiva em JavaFX 21 (FXML + CSS), arte pixel art, vídeo de fundo animado, modais e ícones SVG |
| Som | Trilha sonora em loop, vinheta de abertura e efeitos sonoros de interface com controle independente de volume (música e efeitos) |
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
- **Configurações e Acessibilidade:** menu de configurações com persistência em `configuracao.xml`, permitindo alternar auto-save, pular animações de combate e cutscenes, além de ajustar o volume da música e dos efeitos sonoros em steps de 10%.
- **Progresso Global e Conquistas:** rastreamento contínuo em `progresso.xml`, registrando finais desbloqueados e conquistas da carreira (como *Primeiros Passos*).

## Organização do código

O projeto é organizado segundo o padrão MVC, com classes e responsabilidades distribuídas nos seguintes pacotes:

```text
src/main/
├── java/com/exa863/anselmo_adna/
│   ├── Main.java                 # Ponto de entrada agnóstico da aplicação
│   ├── Application.java          # Inicialização da janela JavaFX (Stage, ícone e fonte)
│   ├── controller/               # Regras do jogo, cenas, narrativa, combate e saves
│   │   ├── fx/                   # Controllers JavaFX (SceneManager, AudioManager, IconManager e telas)
│   │   │   └── modais/           # Controllers de modais reutilizáveis (ex.: ConfirmacaoController)
│   │   ├── cenas/                # Controle de abertura e cutscenes
│   │   ├── SaveGameController.java   # Gerenciamento de slots e autosave
│   │   └── XmlFileController.java    # Serialização e desserialização XML (Jackson)
│   ├── exception/                # Exceções customizadas de persistência e validação
│   ├── persistencia/             # Modelos e manipuladores de configurações, progresso e conquistas
│   ├── model/
│   │   ├── character/            # Jogador e personagens
│   │   ├── combat/               # Lutadores, estilos, rounds e resultados
│   │   ├── narrativa/            # Diálogos, capítulos e dependências da história
│   │   ├── stats/                # Atributos, itens, inventário e relacionamentos
│   │   └── world/                # Locais, tempo e estados do jogo (GameState)
│   ├── view/
│   │   ├── fx/                   # Enums de mapeamento (Tela, Som, Musica, Icone)
│   │   └── console/              # Interface textual da Fase 1
│   └── utils/                    # Formatação de textos e caixas
└── resources/
    ├── views/                    # Telas e modais em FXML (inclui views/modais/)
    ├── styles/                   # Estilos CSS (global.css + CSS específico por tela)
    ├── audios/                   # Trilha sonora (.mp3) e efeitos sonoros (.wav)
    ├── videos/                   # Vídeo de fundo animado (.mp4)
    ├── fonts/                    # Fonte pixel art (upheavtt.ttf)
    └── images/                   # Logo, ícone da janela e ícones SVG pixel art
```

São utilizados herança e polimorfismo nos estilos de luta e capítulos, encapsulamento dos atributos e composição entre os componentes da partida. O grafo de capítulos organiza os pré-requisitos da narrativa.

### Tecnologias

| Tecnologia | Versão no projeto | Uso |
| --- | --- | --- |
| Java | 21 | Linguagem e execução |
| JavaFX (Controls, FXML, Media) | 21.0.5 | Interface gráfica, telas FXML, CSS, áudio e vídeo |
| Maven | Instalação externa | Compilação, execução (`javafx-maven-plugin`) e empacotamento |
| JLine | 3.29.0 | Suporte ao terminal da Fase 1 |
| JGraphT | 1.5.2 | Grafo de dependências entre capítulos |
| Jackson Dataformat XML | 2.17.2 | Serialização e persistência de dados em XML |
| JUnit Jupiter | 5.10.2 | Testes automatizados de unidade e integração |

Não é utilizada uma engine de jogos.

## Requisitos para executar

- **Java 21 (JDK 21):** necessário para compilar e executar a aplicação (inclui `java` e `javac`).
- **Apache Maven:** necessário para baixar as dependências do JavaFX, compilar e rodar pelo código-fonte.
- **Internet na primeira execução:** o Maven baixa automaticamente as dependências (JavaFX 21, Jackson, JGraphT, JUnit, etc.) para o cache local.
- **Ambiente gráfico desktop:** janela redimensionável com resolução base **16:9 (`960 x 540`)** e mínimo de **`640 x 360`**.

## Como compilar e abrir pelo código-fonte

Os exemplos abaixo usam PowerShell no Windows.

### Onde baixar e como preparar o JDK 21 e o Maven

#### Opção rápida no Windows (via Scoop — sem precisar de Administrador)

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
Invoke-RestMethod -Uri https://get.scoop.sh | Invoke-Expression
scoop bucket add java
scoop install temurin21-jdk maven
```

Depois de instalar, **feche e abra o PowerShell novamente** para carregar `JAVA_HOME` e `PATH`.

#### Instalação manual

Se preferir instalar manualmente, siga o [guia oficial de instalação do Apache Maven](https://maven.apache.org/install.html):

1. Instale o **JDK 21** e configure `JAVA_HOME` apontando para a pasta do JDK (sem `\bin` no final).
2. Baixe o **Binary zip archive** na [página oficial do Maven](https://maven.apache.org/download.cgi), extraia em uma pasta fixa (ex.: `C:\Ferramentas\apache-maven-3.9.x`) e adicione a pasta `bin` ao `Path` do Windows.
3. Abra um novo terminal e confira as versões.

### 1. Confira as instalações

```powershell
java -version
javac -version
mvn -version
```

Confira se o Java e o compilador são da versão **21** e se o Maven está usando esse mesmo JDK.

### 2. Entre na pasta do projeto

```powershell
cd "C:\caminho\Marco1_Java"
```

### 3. Execute o jogo (JavaFX)

```powershell
mvn javafx:run
```

O Maven baixa as dependências necessárias (incluindo JavaFX Controls, FXML e Media), compila e abre a janela gráfica do jogo. A primeira execução pode demorar um pouco por causa dos downloads iniciais.

#### Rodando direto pela IDE

- **VS Code:** instale o **Extension Pack for Java** (Microsoft) e aperte **`F5`** (ou **`Ctrl+Shift+B`** para rodar `mvn javafx:run`).
- **IntelliJ IDEA:** abra a aba **Maven** (lateral direita) → **PBLGame → Plugins → javafx** → duplo clique em **`javafx:run`**.

### 4. Executar os testes automatizados

```powershell
mvn test
```

### 5. Gerar um JAR executável para compartilhar

```powershell
mvn clean package
```

O arquivo final é `target/PBLGame-2.0.jar` (fat JAR gerado pelo Maven Shade com todas as dependências). Compartilhe esse arquivo — o arquivo com prefixo `original-` não contém as dependências.

## Como executar uma versão já compilada

Baixe o arquivo **`PBLGame-2.0.jar`** na página de **[Releases do GitHub](https://github.com/AdnaAmorim/Marco1_Java/releases)** (necessário ter **Java 21** instalado):

```powershell
java -jar PBLGame-2.0.jar
```

Se estiver na pasta do projeto após `mvn clean package`, execute `java -jar target/PBLGame-2.0.jar`. O jogo compilado não precisa de internet para funcionar.

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
| `JAVA_HOME is not defined correctly` | Configure `JAVA_HOME` apontando para o JDK 21 (ou instale via Scoop) e reabra o terminal |
| Erro de versão do Java | Compare `java -version`, `javac -version` e o Java mostrado por `mvn -version` |
| `NoClassDefFoundError: javafx/...` | Rode `mvn compile` para baixar as dependências do JavaFX e sincronize o projeto na IDE (`Java: Update Project` no VS Code) |
| `Unsupported JavaFX configuration (unnamed module)` | Aviso normal ao executar fora do `--module-path` (como no `F5` ou fat JAR); não afeta o funcionamento |
| JAR não encontrado | Pasta atual e caminho informado depois de `java -jar` |
| `NoClassDefFoundError` ao rodar o JAR | Use o JAR completo gerado por `mvn clean package`, sem o prefixo `original-` |

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
