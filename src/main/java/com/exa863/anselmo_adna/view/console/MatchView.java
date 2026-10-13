package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.Round;
import com.exa863.anselmo_adna.view.View;
import com.exa863.anselmo_adna.view.console.art.LutasASCII;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.controller.GameController;

import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import java.util.function.Consumer;

public class MatchView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final Match match;
    private final Lutador lutador1;
    private final Lutador lutador2;
    private final View viewRetorno;
    private final Consumer<ResultadoLuta> aoFinalizar;

    public MatchView(Console console, SceneController sceneController, GameController gameController, Match match,
                     Lutador lutador1, Lutador lutador2) {
        this(console, sceneController, gameController, match, lutador1, lutador2, null, null);
    }

    public MatchView(Console console, SceneController sceneController, GameController gameController, Match match,
                     Lutador lutador1, Lutador lutador2, View viewRetorno, Consumer<ResultadoLuta> aoFinalizar) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
        this.match = match;
        this.lutador1 = lutador1;
        this.lutador2 = lutador2;
        this.viewRetorno = viewRetorno;
        this.aoFinalizar = aoFinalizar;
    }

    @Override
    public void render() {
        if (Console.MODO_DEV) {
            console.clearConsole();
            imprimirHUD(null);
            console.printlnConsole("\n--- RESULTADO OFICIAL [MODO DEV ATIVO - ANIMAÇÕES PULADAS] ---");
            console.printlnConsole(match.getResultadoLuta().getDescricao() + "\n");

            if (aoFinalizar != null) {
                aoFinalizar.accept(match.getResultadoLuta());
            }

            console.esperarEnter("\n[Pressione ENTER para continuar]");

            if (sceneController != null) {
                View destino = (viewRetorno != null) ? viewRetorno : new MapaView(console, sceneController, gameController);
                sceneController.trocarCena(destino);
            }
            return;
        }

        for (Round round : match) {
            console.clearConsole();

            anunciarRound(round);

            java.util.List<String> eventos = round.getEventos();
            for (String evento : eventos) {
                animarEspera();
                console.printDigitado(evento);
            }

            if (!round.isUltimo()) {
                animarFimDeRound();
                console.printlnConsole(LutasASCII.fimDeRound);
                console.printlnConsole("- Fim do Round " + round.getNumero()
                        + "! Os lutadores vão para seus córners e respiram fundo.");
                animarDing();
            } else {
                console.esperarEnter("\n[Pressione ENTER para continuar]");
            }
        }

        console.clearConsole();
        imprimirHUD(null);
        console.printlnConsole("\n--- RESULTADO OFICIAL ---");
        console.printlnConsole(match.getResultadoLuta().getDescricao() + "\n");

        if (aoFinalizar != null) {
            aoFinalizar.accept(match.getResultadoLuta());
        }

        console.esperarEnter("\n[Pressione ENTER para continuar]");

        if (sceneController != null) {
            View destino = (viewRetorno != null) ? viewRetorno : new MapaView(console, sceneController, gameController);
            sceneController.trocarCena(destino);
        }
    }

    private void imprimirHUD(Round round) {
        String titulo = round == null || round.isUltimo() ? "FIM DA LUTA" : "ROUND " + round.getNumero();

        String n1 = lutador1.getNome();
        if (n1.length() > 37) n1 = n1.substring(0, 37);
        String e1 = "[" + lutador1.getEstiloLuta().getNome() + "]";
        if (e1.length() > 37) e1 = e1.substring(0, 37);

        String n2 = lutador2.getNome();
        if (n2.length() > 37) n2 = n2.substring(0, 37);
        String e2 = "[" + lutador2.getEstiloLuta().getNome() + "]";
        if (e2.length() > 37) e2 = e2.substring(0, 37);

        String linhaNomes = String.format("%-37s %37s", n1, n2);
        String linhaEstilos = String.format("%-37s %37s", e1, e2);

        double hp1 = lutador1.getAtributosEfetivos().saude;
        double hp2 = lutador2.getAtributosEfetivos().saude;
        double st1 = lutador1.getAtributosEfetivos().energia;
        double st2 = lutador2.getAtributosEfetivos().energia;

        String barraHp1 = gerarBarra(hp1);
        String barraHp2 = gerarBarra(hp2);
        String barraSt1 = gerarBarra(st1);
        String barraSt2 = gerarBarra(st2);

        String linhaHpEsq = String.format("%3.0f HP %s", hp1, barraHp1);
        String linhaHpDir = String.format("%s %3.0f HP", barraHp2, hp2);
        String linhaHp = String.format("%-37s %37s", linhaHpEsq, linhaHpDir);

        String linhaStEsq = String.format("%3.0f ST %s", st1, barraSt1);
        String linhaStDir = String.format("%s %3.0f ST", barraSt2, st2);
        String linhaSt = String.format("%-37s %37s", linhaStEsq, linhaStDir);

        double f1 = lutador1.getAtributosEfetivos().forca;
        double a1 = lutador1.getAtributosEfetivos().agilidade;
        double r1 = lutador1.getAtributosEfetivos().resistencia;

        double f2 = lutador2.getAtributosEfetivos().forca;
        double a2 = lutador2.getAtributosEfetivos().agilidade;
        double r2 = lutador2.getAtributosEfetivos().resistencia;

        String attr1 = String.format("FOR: %2.0f | AGI: %2.0f | RES: %2.0f", f1, a1, r1);
        String attr2 = String.format("FOR: %2.0f | AGI: %2.0f | RES: %2.0f", f2, a2, r2);
        String linhaAttrs = String.format("%-37s %37s", attr1, attr2);

        String[] content = {
                titulo,
                "",
                linhaNomes,
                linhaEstilos,
                linhaHp,
                linhaSt,
                "",
                linhaAttrs
        };

        String box = new com.exa863.anselmo_adna.utils.AsciiBox()
                .size(79)
                .borders("─", "│")
                .corners("╭", "╮", "╰", "╯")
                .render(content);

        console.printlnConsole(box + "\n");
    }

    private String gerarBarra(double valor) {
        int total = 10;
        int preenchidas = (int) Math.round((valor / 100.0) * total);
        if (preenchidas > total)
            preenchidas = total;
        if (preenchidas < 0)
            preenchidas = 0;

        return "●".repeat(preenchidas) + "○".repeat(total - preenchidas);
    }

    private void anunciarRound(Round round) {
        imprimirApresentacaoHUD();

        int numeroRound = round.getNumero();
        if (numeroRound == 1) {
            apresentarLuta();
        } else {
            console.printDigitado("\n[Gongo soa] Preparem-se para o Round " + numeroRound + "!\n");
        }

        console.esperarEnter("\n[Pressione ENTER para continuar]");

        animarContagem();

        console.clearConsole();
        imprimirHUD(round);
    }

    private void imprimirApresentacaoHUD() {
        String titulo = "GRANDE COMBATE";

        String n1 = lutador1.getNome();
        if (n1.length() > 37) n1 = n1.substring(0, 37);
        String e1 = "[" + lutador1.getEstiloLuta().getNome() + "]";
        if (e1.length() > 37) e1 = e1.substring(0, 37);

        String n2 = lutador2.getNome();
        if (n2.length() > 37) n2 = n2.substring(0, 37);
        String e2 = "[" + lutador2.getEstiloLuta().getNome() + "]";
        if (e2.length() > 37) e2 = e2.substring(0, 37);

        String linhaNomes = String.format("%-37s %37s", n1, n2);
        String linhaEstilos = String.format("%-37s %37s", e1, e2);

        double f1 = lutador1.getAtributosEfetivos().forca;
        double a1 = lutador1.getAtributosEfetivos().agilidade;
        double r1 = lutador1.getAtributosEfetivos().resistencia;

        double f2 = lutador2.getAtributosEfetivos().forca;
        double a2 = lutador2.getAtributosEfetivos().agilidade;
        double r2 = lutador2.getAtributosEfetivos().resistencia;

        String attr1 = String.format("FOR: %2.0f | AGI: %2.0f | RES: %2.0f", f1, a1, r1);
        String attr2 = String.format("FOR: %2.0f | AGI: %2.0f | RES: %2.0f", f2, a2, r2);
        String linhaAttrs = String.format("%-37s %37s", attr1, attr2);

        String[] content = {
                titulo,
                "",
                linhaNomes,
                linhaEstilos,
                "",
                linhaAttrs
        };

        String box = new com.exa863.anselmo_adna.utils.AsciiBox()
                .size(79)
                .borders("─", "│")
                .corners("╭", "╮", "╰", "╯")
                .render(content);

        console.printlnConsole(box + "\n");
    }

    private void apresentarLuta() {
        java.time.LocalDate hoje = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataHj = hoje.format(formatter);

        console.printDigitado("\nSENHORAS E SENHORES! Boa noite!\n");
        try { Thread.sleep(800); } catch (Exception e) {}

        console.printDigitado("Na noite de hoje, " + dataHj + ", presenciaremos um combate histórico!\n");
        try { Thread.sleep(800); } catch (Exception e) {}

        console.printDigitado("Entrando pelo córner esquerdo, um mestre implacável do " + lutador1.getEstiloLuta().getNome() + "...");
        console.printDigitado("O temível " + lutador1.getNome().toUpperCase() + "!\n");
        try { Thread.sleep(800); } catch (Exception e) {}

        console.printDigitado("E pelo córner direito, com muita determinação e técnica no " + lutador2.getEstiloLuta().getNome() + "...");
        console.printDigitado("O formidável " + lutador2.getNome().toUpperCase() + "!\n");
        try { Thread.sleep(800); } catch (Exception e) {}

        console.printDigitado("A luta vai começar...\n");
        try { Thread.sleep(800); } catch (Exception e) {}
    }

    private void animarEspera() {
        if (Console.MODO_DEV) return;
        String[] frames = { ".", "..", "...", ".." };
        console.animarFramesTempo(frames, 2000, 100);
        console.printConsole("\r   \r");
    }

    private void animarFimDeRound() {
        if (Console.MODO_DEV) return;
        console.printlnConsole("");
        String base = "FIM DE ROUND";
        String[] frames = { base + "   ", base + ".  ", base + ".. ", base + "..." };
        console.animarFramesTempo(frames, 2000, 250);
        console.printConsole("\r" + " ".repeat(20) + "\r");
    }

    private void animarDing() {
        if (Console.MODO_DEV) return;
        String[] frames = {
                "        )))   D I N G   (((",
                "         ))   D I N G   (( ",
                "          )   D I N G   (  ",
                "            ) D I N G (    "
        };
        console.printConsole("\n");
        console.animarFramesTempo(frames, 1200, 150);
        console.printlnConsole("");
        console.esperarEnter("   [Pressione ENTER para continuar]");
    }

    private void animarContagem() {
        String[] contagem = {"3...", " 2...", " 1...", " FIGHT!\n"};
        console.animarTextoSequencial(contagem, 50, 600);
        console.printlnConsole("");
    }
}