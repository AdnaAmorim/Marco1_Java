package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.SaveGameController;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.GameState;

import java.io.IOException;
/**
 * Menu responsável pelo salvamento manual da partida.
 * Permite escolher um slot, sobrescrever saves existentes e excluir saves salvos.
 *
 * @author Anselmo e Adna
 */
public class MenuSalvar {
    /**
     * Exibe o menu de salvamento e permite ao jogador
     * escolher entre salvar, excluir um save ou voltar.
     */
    private final Console console;
    private final SaveGameController saveGameController;
    private final GameState gameState;

    public MenuSalvar(Console console, GameState gameState) {
        this.console = console;
        this.gameState = gameState;
        this.saveGameController = new SaveGameController();
    }

    public void mostrar() {

        boolean aberto = true;

        while (aberto) {

            console.clearConsole();

            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                         SALVAR PARTIDA                               ║");
            console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
            console.printlnConsole("║                                                                      ║");
            console.printlnConsole("║  ► Escolha um slot para salvar sua partida.                          ║");
            console.printlnConsole("║                                                                      ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

            console.printlnConsole("");
            console.printlnConsole("  Navegue com [ ▲ / ▼ ] e confirme com [ ENTER ]:");
            console.printlnConsole("");

            String slot1 = statusSlot(1);
            String slot2 = statusSlot(2);
            String slot3 = statusSlot(3);
            String slot4 = statusSlot(4);

            CEscolha[] opcoes = {
                    new CEscolha("Slot 1 - " + slot1, 1),
                    new CEscolha("Slot 2 - " + slot2, 2),
                    new CEscolha("Slot 3 - " + slot3, 3),
                    new CEscolha("Slot 4 - " + slot4, 4),
                    new CEscolha("Excluir um save", 5),
                    new CEscolha("Voltar", 0)
            };

            CMultiplaEscolha menu = new CMultiplaEscolha(console);

            try {

                int opcao = menu.escolha(opcoes).index;

                if (opcao == 0) {
                    aberto = false;
                    continue;
                }

                if (opcao == 5) {
                    excluirSave();
                    continue;
                }

                salvarNoSlot(opcao);

            } catch (IOException e) {

                console.printlnConsole("Erro ao ler a escolha.");
                console.esperarEnter("");
            }
        }
    }

    /**
     * Verifica se um slot está ocupado ou vazio.
     *
     * @param slotId Número do slot que será verificado.
     * @return OCUPADO se existir um save ou VAZIO caso contrário.
     */
    private String statusSlot(int slotId) {

        if (saveGameController.existe(slotId)) {
            return "OCUPADO";
        }

        return "VAZIO";
    }

    /**
     * Salva o estado atual do jogo no slot escolhido.
     * Caso o slot esteja ocupado, solicita confirmação antes de sobrescrever.
     *
     * @param slotId Número do slot escolhido.
     * @throws IOException Caso ocorra erro durante a leitura da confirmação.
     */
    private void salvarNoSlot(int slotId) throws IOException {

        if (saveGameController.existe(slotId)) {

            boolean confirmou = confirmarSobrescrita(slotId);

            if (!confirmou) {
                return;
            }
        }

        try {

            saveGameController.salvar(slotId, gameState);

            console.printlnConsole("");
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                       PARTIDA SALVA!                                 ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

        } catch (FalhaSalvamentoException e) {

            console.printlnConsole("");
            console.printlnConsole(e.getMessage());
        }

        console.esperarEnter("");
    }

    /**
     * Solicita confirmação antes de substituir um save existente.
     *
     * @param slotId Número do slot que será sobrescrito.
     * @return true se o jogador confirmar ou false caso cancele.
     * @throws IOException Caso ocorra erro durante a leitura da escolha.
     */
    private boolean confirmarSobrescrita(int slotId) throws IOException {

        console.clearConsole();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                       SOBRESCREVER SAVE                              ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  Já existe uma partida salva neste slot.                             ║");
        console.printlnConsole("║  Deseja substituir o save existente?                                 ║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

        console.printlnConsole("");

        CEscolha[] opcoes = {
                new CEscolha("Sim, sobrescrever Slot " + slotId, 1),
                new CEscolha("Não, cancelar", 0)
        };

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        int escolha = menu.escolha(opcoes).index;

        return escolha == 1;
    }


    private void excluirSave() throws IOException {

        console.clearConsole();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                          EXCLUIR SAVE                                ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  ► Escolha qual slot deseja excluir.                                 ║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

        console.printlnConsole("");
        console.printlnConsole("  Navegue com [ ▲ / ▼ ] e confirme com [ ENTER ]:");
        console.printlnConsole("");

        CEscolha[] opcoes = {
                new CEscolha("Slot 1 - " + statusSlot(1), 1),
                new CEscolha("Slot 2 - " + statusSlot(2), 2),
                new CEscolha("Slot 3 - " + statusSlot(3), 3),
                new CEscolha("Slot 4 - " + statusSlot(4), 4),
                new CEscolha("Voltar", 0)
        };

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        int slotId = menu.escolha(opcoes).index;

        if (slotId == 0) {
            return;
        }

        if (!saveGameController.existe(slotId)) {

            console.printlnConsole("");
            console.printlnConsole("Este slot está vazio.");
            console.esperarEnter("");

            return;
        }

        boolean confirmou = confirmarExclusao(slotId);

        if (!confirmou) {
            return;
        }

        boolean excluiu = saveGameController.excluir(slotId);

        if (excluiu) {

            console.printlnConsole("");
            console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
            console.printlnConsole("║                    SAVE EXCLUÍDO COM SUCESSO!                        ║");
            console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

        } else {

            console.printlnConsole("");
            console.printlnConsole("Erro ao excluir o save.");
        }

        console.esperarEnter("");
    }

    /**
     * Solicita confirmação antes de excluir permanentemente um save.
     *
     * @param slotId Número do slot que será excluído.
     * @return true se o jogador confirmar ou false caso cancele.
     * @throws IOException Caso ocorra erro durante a leitura da escolha.
     */
    private boolean confirmarExclusao(int slotId) throws IOException {

        console.clearConsole();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                         EXCLUIR SAVE                                 ║");
        console.printlnConsole("╠══════════════════════════════════════════════════════════════════════╣");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("║  Tem certeza que deseja excluir o Slot " + slotId + "?               ║");
        console.printlnConsole("║  Essa ação não poderá ser desfeita.                                  ║");
        console.printlnConsole("║                                                                      ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝");

        console.printlnConsole("");

        CEscolha[] opcoes = {
                new CEscolha("Sim, excluir", 1),
                new CEscolha("Não, cancelar", 0)
        };

        CMultiplaEscolha menu = new CMultiplaEscolha(console);

        int escolha = menu.escolha(opcoes).index;

        return escolha == 1;
    }
}