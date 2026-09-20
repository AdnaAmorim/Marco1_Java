package com.exa863.anselmo_adna.model.narrativa.capitulos;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.DialogoNarrativo;
import com.exa863.anselmo_adna.model.narrativa.GatilhoNarrativo;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;

import java.util.ArrayList;
import java.util.List;

public class Capitulo10 extends Capitulo {

    public static final String ID = "CAPITULO_10";
    private final Personagem anthony;

    public Capitulo10() {
        super(ID, "O Julgamento Final", new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Campeonato Mundial"), List.of(Capitulo09.ID));
        this.anthony = new Personagem(6, "Anthony", "O Trovão.", Cores.CASTANHO, Sexo.MASCULINO);
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        List<Dialogo> historia = new ArrayList<>();

        historia.add(DialogoNarrativo.narrador("Chegamos ao ápice. O Campeonato Mundial de Boxe, sediado na gigantesca e apoteótica arena iluminada da Cidade B."));
        historia.add(DialogoNarrativo.narrador("A multidão enfurecida ruge como um oceano furioso de vozes. As luzes cegam sua visão temporariamente e o locutor principal grita seu nome para o mundo ouvir."));
        historia.add(DialogoNarrativo.sistema("[Locutor Oficial]: 'E no córner azul... o desafiante que chocou todo o país ao chegar até aqui! O homem sem medo!'"));

        if (player.isCapituloConcluido("RIVALIDADE_ANTHONY")) {
            historia.add(new Dialogo(anthony, "(Sorrindo com pura malícia, lembrando da sua afronta na pesagem) 'Você abriu a boca grande naquele dia, novato. Hoje eu vou arrancar os seus dentes!'"));
        } else {
            historia.add(new Dialogo(anthony, "(Batendo as luvas agressivamente, com os olhos injetados e cheios de fúria) 'Chegamos ao fim da linha, caipira silencioso. Vamos ver se você luta melhor do que fala.'"));
        }

        historia.add(new Dialogo(player, "(Respirando fundo e erguendo a guarda clássica) 'Estou feito do mesmo legado impecável que meu pai. E hoje, o passado vai te nocautear com meus punhos.'"));

        if (player.isCapituloConcluido("FLAG_ESTRATEGIA_JAMES")) {
            historia.add(new Dialogo(player, "(Focando os olhos nos ombros do rival) 'Ele deixa a guarda direita aberta por exatos 0.8 segundos, como James previu. É a minha janela de oportunidade...'"));
        }

        historia.add(DialogoNarrativo.narrador("O gongo soa como um trovão. O último e derradeiro combate da sua vida, a provação de fogo, finalmente começa."));
        historia.add(DialogoNarrativo.sistema("Prepare-se para subir no ringue e lutar de verdade!"));

        return historia;
    }
    @Override
    public boolean podeIniciar(Player player) {
        return player != null && player.getLutasNacionais() >= 2;
    }
}