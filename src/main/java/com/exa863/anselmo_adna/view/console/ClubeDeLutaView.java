package com.exa863.anselmo_adna.view.console;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.MatchController;
import com.exa863.anselmo_adna.controller.SceneController;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.view.View;

import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Tela do Clube de Luta clandestino.
 * Permite apostar dinheiro em lutas de rua, com mais risco de perder vida e energia.
 *
 * @author Anselmo e Adna
 */
public class ClubeDeLutaView implements View {

    private final Console console;
    private final SceneController sceneController;
    private final GameController gameController;
    private final Random random = new Random();

    public ClubeDeLutaView(Console console, SceneController sceneController, GameController gameController) {
        this.console = console;
        this.sceneController = sceneController;
        this.gameController = gameController;
    }

    @Override
    public void render() {
        console.clearConsole();
        Player player = gameController.getPlayer();
        Atributo attr = player.getAtributos();

        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                  CLUBE DE LUTA - BRIGAS CLANDESTINAS                 ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  O ambiente é sombrio, com cheiro de fumaça, apostas clandestinas e suor.");
        console.printlnConsole("  Aqui não existem nomes, juízes ou regras: apenas a lei da rua.\n");

        if (attr.getSaude() <= 25 || attr.getEnergia() < 20) {
            console.printlnConsole("  [!] Um apostador empurra você para longe do círculo:\n");
            console.printlnConsole("      \"Ei, parceiro, você mal consegue parar em pé! Não dura 10 segundos aqui.\"");
            console.printlnConsole("      (Saúde ou Energia muito baixas para encarar a briga de rua)");
            console.printlnConsole("  ► Vá até sua Casa no mapa descansar e dormir para se recuperar!\n");

            console.printlnConsole("       ┌────────────────────────────────────────────────────────┐");
            console.printlnConsole("       │              [ ENTER ]  Voltar ao Mapa                 │");
            console.printlnConsole("       └────────────────────────────────────────────────────────┘");
            console.esperarEnter("");
            gameController.voltarLocal();
            sceneController.trocarCena(new MapaView(console, sceneController, gameController));
            return;
        }

        int forcaOponente = 1 + random.nextInt(2);
        int agilidadeOponente = 1 + random.nextInt(2);
        int resistenciaOponente = 1 + random.nextInt(2);
        int inteligenciaOponente = 1;

        int dinheiroAposta = 50 + random.nextInt(51);

        EstiloLuta estiloOponente = switch (random.nextInt(3)) {
            case 0 -> new Velocista();
            case 1 -> new Nocauteador();
            default -> new Contragolpeador();
        };

        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ INFORMAÇÕES DO ADVERSÁRIO                                          │");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ Estilo de Luta     : " + String.format("%-46s", estiloOponente.getNome()) + "│");
        console.printlnConsole("  │ Força              : " + String.format("%-46s", forcaOponente + " / 10") + "│");
        console.printlnConsole("  │ Agilidade          : " + String.format("%-46s", agilidadeOponente + " / 10") + "│");
        console.printlnConsole("  │ Resistência        : " + String.format("%-46s", resistenciaOponente + " / 10") + "│");
        console.printlnConsole("  │ Inteligência       : " + String.format("%-46s", inteligenciaOponente + " / 10") + "│");
        console.printlnConsole("  │ Dinheiro de Aposta : " + String.format("%-46s", dinheiroAposta + " Reais (para o vencedor)") + "│");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ SEU STATUS         : Saúde: " + String.format("%-3d", attr.getSaude()) + "/100  |  Energia: "
                + String.format("%-3d", attr.getEnergia()) + "/100  |  Dinheiro: " + player.getDinheiro() + " Reais" + " ".repeat(7) + "│");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");

        console.printlnConsole("  [!] Cuidado: Brigas clandestinas de rua causam ferimentos pesados!\n");

        CEscolha[] opcoes = new CEscolha[] {
                new CEscolha("Encarar a Briga de Rua", 0),
                new CEscolha("Dar as costas e Voltar ao Mapa", 1)
        };

        try {
            CMultiplaEscolha menu = new CMultiplaEscolha(console);
            CEscolha escolha = menu.escolha(opcoes);

            if (escolha.index == 1) {
                gameController.voltarLocal();
                sceneController.trocarCena(new MapaView(console, sceneController, gameController));
                return;
            }

            EstiloLuta postura = escolherPostura();
            iniciarBrigaDeRua(player, estiloOponente, forcaOponente, agilidadeOponente,
                    resistenciaOponente, inteligenciaOponente, dinheiroAposta, postura);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EstiloLuta escolherPostura() throws IOException {
        console.clearConsole();
        console.printlnConsole("╔══════════════════════════════════════════════════════════════════════╗");
        console.printlnConsole("║                     POSTURA NA BRIGA DE RUA                          ║");
        console.printlnConsole("╚══════════════════════════════════════════════════════════════════════╝\n");
        console.printlnConsole("  Cada postura altera suas capacidades com bônus e penalidades percentuais:\n");

        console.printlnConsole("  ┌────────────────────────────────────────────────────────────────────┐");
        console.printlnConsole("  │ 1. VELOCISTA (Out-Boxer)                                           │");
        console.printlnConsole("  │    ▲ Agilidade  : Bônus de +15% a +75% (conforme sua agilidade)    │");
        console.printlnConsole("  │    ▲ Energia    : Bônus de +20% (mais fôlego durante os rounds)    │");
        console.printlnConsole("  │    ▼ Força      : Penalidade de -20% (golpes mais leves)           │");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ 2. NOCAUTEADOR (Slugger)                                           │");
        console.printlnConsole("  │    ▲ Força      : Bônus de +15% a +75% (conforme sua força atual)  │");
        console.printlnConsole("  │    ▼ Agilidade  : Penalidade de -20% (movimentação mais pesada)    │");
        console.printlnConsole("  │    ▼ Energia    : Penalidade de -20% (gasto maior ao golpear)      │");
        console.printlnConsole("  ├────────────────────────────────────────────────────────────────────┤");
        console.printlnConsole("  │ 3. CONTRAGOLPEADOR (Counter-Puncher)                               │");
        console.printlnConsole("  │    ▲ Resistência: Bônus de +15% a +75% (guarda mais impenetrável)  │");
        console.printlnConsole("  │    ▲ Inteligên. : Bônus de +15% a +75% (leitura tática de brechas) │");
        console.printlnConsole("  │    ▼ Agilidade  : Penalidade de -15% (postura mais plantada)       │");
        console.printlnConsole("  └────────────────────────────────────────────────────────────────────┘\n");

        CEscolha[] posturas = new CEscolha[] {
                new CEscolha("Adotar Postura Velocista       (+Agilidade, +Energia, -Força)", 0),
                new CEscolha("Adotar Postura Nocauteador     (+Força, -Agilidade, -Energia)", 1),
                new CEscolha("Adotar Postura Contragolpeador (+Resistência, +Inteligência, -Agilidade)", 2)
        };

        CMultiplaEscolha menuPostura = new CMultiplaEscolha(console);
        CEscolha escolha = menuPostura.escolha(posturas);

        return switch (escolha.index) {
            case 0 -> new Velocista();
            case 1 -> new Nocauteador();
            default -> new Contragolpeador();
        };
    }

    private void iniciarBrigaDeRua(Player player, EstiloLuta estiloOponente,
                                   int f, int a, int r, int i, int dinheiroAposta, EstiloLuta postura) {
        Lutador lutadorPlayer = new Lutador(player, player.getAtributos(), postura);

        Personagem pOponente = new Personagem(999, "Lutador Desconhecido", "Desconhecido", Cores.CASTANHO, Sexo.MASCULINO);
        Atributo attrOponente = new Atributo(100, f, a, r, i, 100);
        Lutador lutadorOponente = new Lutador(pOponente, attrOponente, estiloOponente);

        MatchController matchController = new MatchController();
        Match match = matchController.executarPartida(lutadorPlayer, lutadorOponente);

        Consumer<ResultadoLuta> callbackPosBriga = resultado -> {
            gameController.getDataDia().avancarMinutos(60);
            player.getAtributos().setEnergia(Math.max(0, player.getAtributos().getEnergia() - 25));

            console.printlnConsole("");
            if (resultado.isVencedor(lutadorPlayer)) {
                player.setDinheiro(player.getDinheiro() + dinheiroAposta);
                // Penalidade severa de saúde característica das brigas clandestinas (redução de 50 com piso em 15)
                int novaSaude = Math.max(15, player.getAtributos().getSaude() - 50);
                player.getAtributos().setSaude(novaSaude);

                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                    VITÓRIA NA BRIGA DE RUA!                          ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► Você nocauteou o oponente no chão sujo do galpão!");
                console.printlnConsole("  ► Dinheiro de aposta : +" + dinheiroAposta + " Reais em dinheiro vivo!");
                console.printlnConsole("  ► Seu Dinheiro atual : " + player.getDinheiro() + " Reais");
                console.printlnConsole("  ► Danos corporais    : Você sofreu cortes e hematomas severos!");
                console.printlnConsole("  ► Sua Saúde restante : " + novaSaude + " / 100 (Pouca saúde!)");
                console.printlnConsole("  ► Sugestão           : Vá para sua Casa no mapa descansar e dormir!\n");
            } else if (resultado.isEmpate()) {
                int meioDinheiro = dinheiroAposta / 2;
                player.setDinheiro(player.getDinheiro() + meioDinheiro);
                int novaSaude = Math.max(15, player.getAtributos().getSaude() - 40);
                player.getAtributos().setSaude(novaSaude);

                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                     FIM DA BRIGA - CONFUSÃO                          ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► A briga foi interrompida pelos apostadores!");
                console.printlnConsole("  ► Dinheiro recolhido : +" + meioDinheiro + " Reais");
                console.printlnConsole("  ► Sua Saúde restante : " + novaSaude + " / 100 (Pouca saúde!)\n");
            } else {
                int novaSaude = Math.max(15, player.getAtributos().getSaude() - 60);
                player.getAtributos().setSaude(novaSaude);

                console.printlnConsole("  ╔══════════════════════════════════════════════════════════════════════╗");
                console.printlnConsole("  ║                     DERROTA NA BRIGA DE RUA                          ║");
                console.printlnConsole("  ╚══════════════════════════════════════════════════════════════════════╝\n");
                console.printlnConsole("  ► Você levou a pior e foi jogado na sarjeta sem um tostão!");
                console.printlnConsole("  ► Sua Saúde restante : " + novaSaude + " / 100 (Estado crítico!)");
                console.printlnConsole("  ► Vá URGENTEMENTE até sua Casa no mapa dormir para se recuperar!\n");
            }
        };

        View destinoRetorno = new ClubeDeLutaView(console, sceneController, gameController);
        sceneController.trocarCena(new MatchView(console, sceneController, gameController, match,
                lutadorPlayer, lutadorOponente, destinoRetorno, callbackPosBriga));
    }
}
