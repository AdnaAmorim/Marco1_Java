package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.cenas.CutsceneController;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.TipoEmissor;
import com.exa863.anselmo_adna.model.stats.Relacionamentos;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tela de cutscene e diálogos da história.
 * Mostra as falas dos personagens, exibe as opções de escolha e avisa quando a amizade sobe ou desce.
 *
 * @author Anselmo e Adna
 */
public class CutsceneView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final CutsceneController cutsceneController;
    private final View proximaCena;

    public CutsceneView(Console console, SceneController sceneController,
                        CutsceneController cutsceneController, View proximaCena) {
        this.console = console;
        this.sceneController = sceneController;
        this.cutsceneController = cutsceneController;
        this.proximaCena = proximaCena;
    }

    public CutsceneController getCutsceneController() {
        return cutsceneController;
    }

    @Override
    public void render() {
        console.clearConsole();
        imprimirCabecalho();

        List<Dialogo> dialogos = cutsceneController.getDialogos();
        for (Dialogo dialogo : dialogos) {
            processarDialogo(dialogo);
        }

        cutsceneController.finalizarCapitulo();

        console.printlnConsole("");
        console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
        console.printlnConsole("       │       [ ENTER ]  Capítulo Concluído : Continuar        │");
        console.printlnConsole("       └────────────────────────────────────────────────────────┘");
        console.esperarEnter("");

        if (sceneController != null && proximaCena != null) {
            sceneController.trocarCena(proximaCena);
        }
    }

    private void imprimirCabecalho() {
        String titulo = cutsceneController.getCapitulo().getTitulo().toUpperCase();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║" + centralizar("HISTÓRIA : " + titulo, 70) + "║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
    }

    private void processarDialogo(Dialogo dialogo) {
        if (dialogo.isEscolha()) {
            exibirEscolha(dialogo);
        } else {
            exibirFala(dialogo);
        }
    }

    private void exibirFala(Dialogo dialogo) {
        String linha;
        if (dialogo.getTipoEmissor() == TipoEmissor.NARRADOR) {
            linha = "  ◈ [Narrador] " + dialogo.getTexto();
        } else if (dialogo.getTipoEmissor() == TipoEmissor.SISTEMA) {
            linha = "  ✦ [Sistema] " + dialogo.getTexto();
        } else {
            linha = "  ► " + dialogo.getNomeEmissor() + ": " + dialogo.getTexto();
        }

        console.printDigitado(linha);
        console.esperarEnter("  [ENTER]");
        console.printlnConsole("");
    }

    private void exibirEscolha(Dialogo escolhaDialogo) {
        console.printlnConsole("\n  ► " + escolhaDialogo.getTexto() + "\n");

        List<Dialogo.Opcao> opcoes = escolhaDialogo.getOpcoes();
        CEscolha[] escolhasUI = new CEscolha[opcoes.size()];

        for (int i = 0; i < opcoes.size(); i++) {
            escolhasUI[i] = new CEscolha(opcoes.get(i).getTexto(), i);
        }

        CMultiplaEscolha menu = new CMultiplaEscolha(console);
        try {
            CEscolha selecionada = menu.escolha(escolhasUI);
            Dialogo.Opcao opcaoEscolhida = opcoes.get(selecionada.index);

            console.printlnConsole("\n  [✓ Escolha]: " + opcaoEscolhida.getTexto() + "\n");

            Player player = cutsceneController.getPlayer();
            Map<Integer, Integer> afinidadesAntes = capturarAfinidades(player);

            cutsceneController.processarEscolha(opcaoEscolhida);

            exibirVariacoesDeReputacao(player, afinidadesAntes);

            for (Dialogo falaDesfecho : opcaoEscolhida.getDesfecho()) {
                exibirFala(falaDesfecho);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mapeia os níveis de amizade/afinidade prévios para calcular variações pós-escolha.
     */
    private Map<Integer, Integer> capturarAfinidades(Player player) {
        Map<Integer, Integer> afinidades = new HashMap<>();
        if (player == null) {
            return afinidades;
        }
        for (Relacionamentos relacionamento : player.getRelacionamentos()) {
            if (relacionamento != null && relacionamento.getPersonagem() != null) {
                afinidades.put(relacionamento.getPersonagem().getId(), relacionamento.getNivelAmizade());
            }
        }
        return afinidades;
    }

    /**
     * Compara os níveis de afinidade e exibe as notificações de reputação alterada.
     */
    private void exibirVariacoesDeReputacao(Player player, Map<Integer, Integer> afinidadesAntes) {
        if (player == null) {
            return;
        }

        for (Relacionamentos relacionamento : player.getRelacionamentos()) {
            if (relacionamento == null || relacionamento.getPersonagem() == null) {
                continue;
            }

            Personagem personagem = relacionamento.getPersonagem();
            int depois = relacionamento.getNivelAmizade();
            int antes = afinidadesAntes.getOrDefault(personagem.getId(), 0);
            int delta = depois - antes;

            if (delta == 0) {
                continue;
            }

            if (delta > 0) {
                console.printlnConsole("  ✦ [Reputação] Você ganhou reputação com " + personagem.getNome() + " (+" + delta + ")");
            } else {
                console.printlnConsole("  ✦ [Reputação] Você perdeu reputação com " + personagem.getNome() + " (" + delta + ")");
            }
        }
        console.printlnConsole("");
    }

    private String centralizar(String texto, int largura) {
        if (texto.length() >= largura) {
            return texto.substring(0, largura);
        }
        int espacosTotais = largura - texto.length();
        int espacosEsquerda = espacosTotais / 2;
        int espacosDireita = espacosTotais - espacosEsquerda;
        return " ".repeat(espacosEsquerda) + texto + " ".repeat(espacosDireita);
    }
}