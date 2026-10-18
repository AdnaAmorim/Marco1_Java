package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.SaveGameController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.exception.DadosInvalidosException;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.save.SaveGame;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Seleção dos slots para salvar ou carregar uma carreira.
 *
 * @author Anselmo e Adna
 */
public class SavesView implements View {
    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final SaveGameController saves = new SaveGameController();
    private final boolean salvando;

    public SavesView(Console console, SceneController sceneController,
                     GameController gameController, boolean salvando) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
        this.salvando = salvando;
    }

    @Override
    public void render() {
        try {
            while (true) {
                SaveGame[] detalhes = new SaveGame[SaveGameController.MAX_SLOTS + 2];
                CEscolha[] opcoes = new CEscolha[SaveGameController.MAX_SLOTS + (salvando ? 2 : 3)];
                for (int i = 1; i <= SaveGameController.MAX_SLOTS; i++) {
                    detalhes[i] = lerSlot(i);
                    opcoes[i - 1] = new CEscolha(rotuloSlot("Slot " + i, detalhes[i], saves.existe(i)), i);
                }
                if (!salvando) {
                    detalhes[SaveGameController.MAX_SLOTS + 1] = lerAutomatico();
                    opcoes[SaveGameController.MAX_SLOTS] = new CEscolha(rotuloSlot("Automático",
                            detalhes[SaveGameController.MAX_SLOTS + 1], saves.existeAutomatico()), SaveGameController.MAX_SLOTS + 1);
                }
                opcoes[opcoes.length - 2] = new CEscolha("Excluir save", -1);
                opcoes[opcoes.length - 1] = new CEscolha("Voltar ao Menu Principal", 0);
                int slot = new CMultiplaEscolha(console).escolha(opcoes,
                        escolha -> mostrarSlots(escolha, detalhes)).index;
                if (slot == 0) {
                    voltarAoMenu();
                    return;
                }
                if (slot == -1) {
                    excluirSave();
                    continue;
                }
                if (salvando) {
                    if (gameController.getPlayer() == null) {
                        avisar("Inicie uma carreira antes de salvar.");
                        voltarAoMenu();
                        return;
                    }
                    if (saves.existe(slot) && !confirmar("Sobrescrever o save do slot " + slot + "?")) {
                        continue;
                    }
                    try {
                        saves.salvar(slot, gameController.getGameState());
                        avisar("Partida salva no slot " + slot + ".");
                        voltarAoMenu();
                        return;
                    } catch (FalhaSalvamentoException e) {
                        avisar(e.getMessage());
                    }
                } else {
                    boolean automatico = slot == SaveGameController.MAX_SLOTS + 1;
                    SaveGame save = automatico ? lerAutomatico() : lerSlot(slot);
                    GameState estado = save == null ? null : save.getGameState();
                    if (estado == null) {
                        avisar((automatico ? saves.existeAutomatico() : saves.existe(slot))
                                ? "Save inválido, incompatível ou indisponível para leitura."
                                : "Este slot está vazio.");
                        continue;
                    }
                    if (gameController.getPlayer() != null
                            && !confirmar("Carregar esta partida descarta o progresso não salvo. Continuar?")) {
                        continue;
                    }
                    gameController.carregarGameState(estado);
                    sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                    return;
                }
            }
        } catch (IOException e) {
            avisar("Não foi possível ler a seleção dos slots.");
            voltarAoMenu();
        }
    }

    /** Slot ilegível, vazio ou inválido vira null para a tela exibir como indisponível. */
    private SaveGame lerSlot(int slot) {
        try {
            return saves.carregar(slot);
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return null;
        }
    }

    private SaveGame lerAutomatico() {
        try {
            return saves.carregarAutomatico();
        } catch (FalhaCarregamentoException | DadosInvalidosException e) {
            return null;
        }
    }

    private String rotuloSlot(String titulo, SaveGame save, boolean existe) {
        String estado = save != null ? "[SALVO] " + save.getGameState().getPlayer().getNome()
                : existe ? "[INDISPONÍVEL]" : "[VAZIO]";
        String rotulo = String.format("%-12s %s", titulo, estado);
        return rotulo.length() > 64 ? rotulo.substring(0, 61) + "..." : rotulo;
    }

    private void mostrarSlots(CEscolha selecionada, SaveGame[] detalhes) {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        linha(salvando ? "SALVAR PARTIDA - SELECIONE UM SLOT" : "CARREGAR PARTIDA - SELECIONE UM SLOT");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        int slot = selecionada.index;
        if (slot > 0) {
            boolean automatico = slot == SaveGameController.MAX_SLOTS + 1;
            mostrarSave(automatico ? "SALVAMENTO AUTOMÁTICO" : "SLOT " + slot,
                    detalhes[slot], automatico ? saves.existeAutomatico() : saves.existe(slot));
        } else {
            linha(slot == -1 ? "EXCLUIR SAVE" : "VOLTAR AO MENU");
            linha(slot == -1 ? "Escolha um save e confirme sua exclusão na próxima tela."
                    : "Retorne ao menu sem alterar os saves.");
            linha("");
            linha("");
            linha("");
        }
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        console.printlnConsole("\n  [ ▲ / ▼ ] Selecionar   [ ENTER ] Confirmar");
        console.printlnConsole("  Os detalhes acima acompanham o slot selecionado.\n");
    }

    private void excluirSave() throws IOException {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        linha("EXCLUIR SAVE - SELECIONE UM SLOT");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        List<CEscolha> opcoes = new ArrayList<>();
        for (int slot = 1; slot <= SaveGameController.MAX_SLOTS; slot++) {
            if (saves.existe(slot)) {
                mostrarSave("Slot " + slot, lerSlot(slot), true);
                opcoes.add(new CEscolha("Excluir Slot " + slot, slot));
            }
        }
        if (saves.existeAutomatico()) {
            mostrarSave("Automático", lerAutomatico(), true);
            opcoes.add(new CEscolha("Excluir salvamento automático", SaveGameController.MAX_SLOTS + 1));
        }
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");
        if (opcoes.isEmpty()) {
            avisar("Não há saves para excluir.");
            return;
        }
        opcoes.add(new CEscolha("Cancelar", 0));
        int slot = new CMultiplaEscolha(console).escolha(opcoes.toArray(CEscolha[]::new)).index;
        if (slot == 0) return;
        boolean automatico = slot == SaveGameController.MAX_SLOTS + 1;
        String nome = automatico ? "o salvamento automático" : "o save do Slot " + slot;
        if (!confirmar("Excluir definitivamente " + nome + "? Essa ação não pode ser desfeita.")) {
            return;
        }
        boolean excluido = automatico ? saves.excluirAutomatico() : saves.excluir(slot);
        avisar(excluido ? "Save excluído." : "Não foi possível excluir o save. Verifique o acesso à pasta de saves.");
    }

    private void linha(String texto) {
        console.printlnConsole("║  " + String.format("%-66.66s", texto) + "  ║");
    }

    private void mostrarSave(String titulo, SaveGame save, boolean existe) {
        if (save == null) {
            linha(titulo + "  |  " + (existe ? "INDISPONÍVEL" : "VAZIO"));
            linha(existe ? "Save incompatível, corrompido ou sem acesso para leitura."
                    : "Nenhuma partida salva neste espaço.");
            linha(existe ? "Use Excluir save para liberar este espaço."
                    : salvando ? "Confirme para salvar sua partida aqui." : "Escolha um espaço marcado como [SALVO] para carregar.");
            linha("");
            linha("");
            return;
        }
        GameState estado = save.getGameState();
        linha(titulo + "  |  SALVO");
        linha("Jogador: " + estado.getPlayer().getNome());
        linha(gameController.getNarrativaController().getCapituloAtual(estado.getPlayer()));
        linha(estado.getDataDia().getDiaFormatado() + " às " + estado.getDataDia().getHoraFormatada()
                + " | " + estado.getLocalAtual().getNome());
        linha("Salvo em: " + save.getDataSalvamento());
    }

    private boolean confirmar(String mensagem) throws IOException {
        console.clearConsole();
        console.printlnConsole("\n  " + mensagem + "\n");
        return new CMultiplaEscolha(console).escolha(new CEscolha[]{
                new CEscolha("Cancelar", 0), new CEscolha("Confirmar", 1)
        }).index == 1;
    }

    private void avisar(String mensagem) {
        console.printlnConsole("\n  " + mensagem);
        console.esperarEnter("  [ ENTER ] Continuar");
    }

    private void voltarAoMenu() {
        sceneController.trocarCena(new MenuView(console, sceneController, gameController));
    }
}
