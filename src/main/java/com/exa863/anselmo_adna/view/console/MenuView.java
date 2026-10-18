package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.SaveGameController;
import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.persistencia.Configuracao;
import com.exa863.anselmo_adna.persistencia.ConfiguracaoXML;
import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.view.console.art.MenuInicialASCII;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.exa863.anselmo_adna.model.GameState;

/**
 * Menu inicial ou menu da partida, conforme a existência de um protagonista.
 *
 * @author Anselmo e Adna
 */
public class MenuView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final Configuracao config = new Configuracao();

    public MenuView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;

        ConfiguracaoXML xml = new ConfiguracaoXML();
        try {
            xml.carregar(config);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            console.printlnConsole(e.getMessage());
        }
    }

    @Override
    public void render() {
        console.clearConsole();
        console.printlnConsole(MenuInicialASCII.tituloArt);
        console.printlnConsole("");
        console.printlnConsole("  Navegue com [ ▲ / ▼ ] e confirme a sua escolha com [ ENTER ]:\n");

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        List<CEscolha> opcoes = new ArrayList<>();
        if (gameController.getPlayer() != null) {
            opcoes.add(new CEscolha("Retomar partida atual", 5));
            opcoes.add(new CEscolha("Salvar partida", 6));
            opcoes.add(new CEscolha("Carregar partida", 4));
            opcoes.add(new CEscolha("Tutorial (Guia do Boxeador)", 1));
            opcoes.add(new CEscolha("Ir para o menu inicial", 7));
        } else {
            SaveGameController.SaveRecente recente = new SaveGameController().carregarMaisRecente();
            if (recente != null) {
                String nome = recente.save().getGameState().getPlayer().getNome();
                String capitulo = gameController.getNarrativaController()
                        .getCapituloAtual(recente.save().getGameState().getPlayer());
                opcoes.add(new CEscolha("Continuar (" + String.format("%.20s", nome)
                        + " | " + capitulo + " | " + recente.getNomeSlot() + ")", 8));
            }
            opcoes.add(new CEscolha("Jogar (Iniciar Carreira)", 0));
            opcoes.add(new CEscolha("Carregar partida", 4));
            opcoes.add(new CEscolha("Tutorial (Guia do Boxeador)", 1));
            opcoes.add(new CEscolha("Configurações", 9));
            opcoes.add(new CEscolha("Créditos & Autoria", 2));
            opcoes.add(new CEscolha("Sair do Jogo", 3));
        }

        try {
            CEscolha escolha = menu.escolha(opcoes.toArray(CEscolha[]::new));

            switch (escolha.index) {
                case 8 -> {
                    SaveGameController.SaveRecente recente = new SaveGameController().carregarMaisRecente();
                    if (recente == null) {
                        console.esperarEnter("  Nenhum save válido disponível. [ ENTER ] Voltar");
                        sceneController.trocarCena(this);
                        return;
                    }
                    gameController.carregarGameState(recente.save().getGameState());
                    sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                }
                case 0 -> {
                    gameController.carregarGameState(new GameState());
                    sceneController.trocarCena(new CriacaoPersonagemView(console, sceneController, gameController));
                }
                case 4 -> sceneController.trocarCena(new SavesView(console, sceneController, gameController, false));
                case 5 -> sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                case 6 -> sceneController.trocarCena(new SavesView(console, sceneController, gameController, true));
                case 7 -> {
                    console.clearConsole();
                    console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
                    console.printlnConsole("║                       VOLTAR AO MENU INICIAL                         ║");
                    console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
                    console.printlnConsole("\n  Deseja sair da partida sem salvar?");
                    console.printlnConsole("  O progresso desde o último save será perdido.\n");
                    CEscolha confirmacao = menu.escolha(new CEscolha[]{
                            new CEscolha("Cancelar e voltar", 0),
                            new CEscolha("Sair sem salvar", 1)
                    });
                    if (confirmacao.index == 1) {
                        gameController.carregarGameState(new GameState());
                    }
                    sceneController.trocarCena(this);
                }
                case 1 -> {
                    console.clearConsole();
                    console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
                    console.printlnConsole("║                     GUIA DO BOXEADOR - TUTORIAL                      ║");
                    console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► [1] CICLO DE VIDA E ROTINA                                        ║");
                    console.printlnConsole("║    • O tempo e os dias avançam a cada atividade realizada.           ║");
                    console.printlnConsole("║    • Treinar consome Energia e aprimora seus atributos físicos.      ║");
                    console.printlnConsole("║    • Dormir em casa restaura suas forças para a próxima jornada.     ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► [2] SEUS ATRIBUTOS DE COMBATE                                     ║");
                    console.printlnConsole("║    • Saúde & Energia : Bases vitais no dia a dia e nos ringues.      ║");
                    console.printlnConsole("║    • Força           : Potência e impacto devastador dos golpes.     ║");
                    console.printlnConsole("║    • Agilidade       : Velocidade de ataque, esquivas e reflexos.    ║");
                    console.printlnConsole("║    • Resistência     : Absorção de dano e fôlego nas lutas.          ║");
                    console.printlnConsole("║    • Inteligência    : Leitura tática dos oponentes e estratégias.   ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► [3] EXPLORAÇÃO & HISTÓRIA (CAPÍTULOS)                             ║");
                    console.printlnConsole("║    • Viaje pelo Mapa entre cidades, academias e arenas.              ║");
                    console.printlnConsole("║    • Entrar em locais específicos aciona os Capítulos da História.   ║");
                    console.printlnConsole("║    • Suas escolhas definem alianças, contratos e o desfecho final!   ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► [4] ECONOMIA & ITENS                                              ║");
                    console.printlnConsole("║    • Visite a Loja para comprar alimentos e remédios revigorantes.   ║");
                    console.printlnConsole("║    • Guarde dinheiro para pagar passagens de viagem entre cidades.   ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
                    console.printlnConsole("");
                    console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
                    console.printlnConsole("       │          [ ENTER ]  Voltar ao Menu Principal           │");
                    console.printlnConsole("       └────────────────────────────────────────────────────────┘");
                    console.esperarEnter("");
                    sceneController.trocarCena(this);
                }
                case 2 -> {
                    console.clearConsole();
                    console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
                    console.printlnConsole("║                              CRÉDITOS                                ║");
                    console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► DESENVOLVIMENTO & AUTORIA:                                        ║");
                    console.printlnConsole("║    • Anselmo Anjos                                                   ║");
                    console.printlnConsole("║    • Adna Amorim                                                     ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► DISCIPLINA & PROJETO ACADÊMICO:                                   ║");
                    console.printlnConsole("║    • 2026.2 - EXA 863 - TP04 - MI - Programação                      ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("║  ► COMPONENTES & BIBLIOTECAS:                                        ║");
                    console.printlnConsole("║    • AsciiBox (Feito por Anselmo) : Molduras e caixas em ASCII       ║");
                    console.printlnConsole("║    • JLine (v3.29.0)    : Terminal interativo e leitura de teclas    ║");
                    console.printlnConsole("║    • JGraphT (v1.5.2)   : Modelagem em grafo para árvore narrativa   ║");
                    console.printlnConsole("║    • JUnit 5 (v5.10.2)  : Framework de testes e validação de regras  ║");
                    console.printlnConsole("║                                                                      ║");
                    console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
                    console.printlnConsole("");
                    console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
                    console.printlnConsole("       │          [ ENTER ]  Voltar ao Menu Principal           │");
                    console.printlnConsole("       └────────────────────────────────────────────────────────┘");
                    console.esperarEnter("");
                    sceneController.trocarCena(this);
                }
                case 9 -> {
                    MenuConfiguracao menuConfig = new MenuConfiguracao(console, config);
                    menuConfig.mostrar();
                    sceneController.trocarCena(this);
                }
                case 3 -> {
                    console.clearConsole();
                    console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
                    console.printlnConsole("║         Obrigado por jogar Boxing Game! Até a próxima luta!          ║");
                    console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
                    console.printlnConsole("");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
