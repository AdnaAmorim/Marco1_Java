package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.controller.GameController;
import com.exa863.anselmo_adna.controller.MatchController;
import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.exception.FalhaSalvamentoException;
import com.exa863.anselmo_adna.model.character.*;
import com.exa863.anselmo_adna.model.combat.*;
import com.exa863.anselmo_adna.model.combat.estilos.*;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.persistencia.*;
import com.exa863.anselmo_adna.view.fx.LutaContexto;
import com.exa863.anselmo_adna.view.fx.Tela;
import java.util.ArrayList;
import java.util.List;

/** Prepara a luta contra Anthony e registra o final da carreira no progresso global. */
final class MundialFx {
    static final String EVENTO = "Campeonato Mundial";

    enum Final {
        DERROTA("01", "FINAL_1_ALMA_VENDIDA_DERROTA", "A alma vendida", "Você cumpriu o acordo e caiu na lona. As organizações por trás das apostas não perdoaram sua covardia."),
        TRAGEDIA("02", "FINAL_2_ALMA_VENDIDA_VITORIA", "Vitória trágica", "Você derrotou Anthony, mas quebrou o contrato de Noor. A glória do ringue durou só até aquela noite."),
        CAMPEAO("03", "FINAL_3_CAMPEAO_DO_MUNDO", "O cinturão mundial", "O juiz ergue seu braço. Pai, onde quer que você esteja, nós conseguimos!"),
        LEGADO("04", "FINAL_4_VERDADEIRO_LEGADO", "O verdadeiro legado", "Anthony venceu no ringue. Anos depois, o legado do seu pai vive na academia onde você ensina.");

        final String numero, id, titulo, texto;
        Final(String numero, String id, String titulo, String texto) {
            this.numero = numero;
            this.id = id;
            this.titulo = titulo;
            this.texto = texto;
        }
    }

    /** Combina o resultado da luta com o acordo de Noor, indicado pelo contrato aceito ou pela afinidade de pelo menos 50. */
    static Final desfecho(Player player, boolean venceu) {
        Personagem noor = new Personagem(5, "Victor Noor", "", Cores.PRETO, Sexo.MASCULINO);
        if (player.isCapituloConcluido("FLAG_CONTRATO_ACEITO") || player.getAfinidade(noor) >= 50) {
            return venceu ? Final.TRAGEDIA : Final.DERROTA;
        }
        return venceu ? Final.CAMPEAO : Final.LEGADO;
    }

    static void iniciarCombate(Player player, EstiloLuta estilo) {
        var desafiante = new Lutador(player, player.getAtributos(), estilo);
        var anthony = new Personagem(6, "Anthony", "O Trovão", Cores.CASTANHO, Sexo.MASCULINO);
        var campeao = new Lutador(anthony, new Atributo(100, 10, 9, 8, 8, 100), new Nocauteador());
        var match = new MatchController().executarPartida(desafiante, campeao);
        player.setDinheiro(player.getDinheiro() - GameController.TAXA_MUNDIAL);
        SceneManager.iniciarLuta(new LutaContexto(EVENTO, "/images/locais/Campeonato_Mundial.png",
                "/images/visual_novel/Anthony.png", LutaContexto.NARRADOR_PROFISSIONAL,
                desafiante, campeao, match, Tela.MENU_INICIAL,
                resultado -> registrar(player, desfecho(player, resultado.isVencedor(desafiante)))));
    }

    /** Registra o desfecho do Mundial e reúne os avisos para o jogador. */
    private static List<String> registrar(Player player, Final fim) {
        List<String> linhas = new ArrayList<>();
        if (fim == Final.CAMPEAO) {
            player.setDinheiro(player.getDinheiro() + GameController.PREMIO_MUNDIAL);
            linhas.add("+Prêmio do Mundial: R$ " + GameController.PREMIO_MUNDIAL);
        }
        if ((fim == Final.CAMPEAO || fim == Final.LEGADO) && player.isCapituloConcluido("INVESTIGACAO_COMPLETA"))
            linhas.add("A reportagem de Alexandra Cruz levou à prisão de Victor Noor.");
        try {
            // Os finais das carreiras anteriores também contam para as conquistas de múltiplos destinos.
            var progresso = new ProgressoGlobal();
            var xml = new ProgressoXML();
            xml.carregar(progresso);
            progresso.registrarFinal(fim.id);
            progresso.registrarConquista(Conquistas.CHEGUEI_AO_MUNDIAL);
            if (fim == Final.CAMPEAO) progresso.registrarConquista(Conquistas.CAMPEAO_DO_MUNDO);
            if (fim == Final.LEGADO) progresso.registrarConquista(Conquistas.VERDADEIRO_LEGADO);
            if (progresso.finaisAlcancados.size() >= 2) progresso.registrarConquista(Conquistas.MULTIPLOS_DESTINOS);
            if (progresso.finaisAlcancados.size() >= 4) progresso.registrarConquista(Conquistas.TODOS_OS_CAMINHOS);
            xml.salvar(progresso);
        } catch (FalhaCarregamentoException | FalhaSalvamentoException ex) {
            linhas.add("!Não foi possível registrar o final no progresso global.");
        }
        return linhas;
    }
}
