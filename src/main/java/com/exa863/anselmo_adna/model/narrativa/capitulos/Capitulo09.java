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

public class Capitulo09 extends Capitulo {

    public static final String ID = "CAPITULO_09";
    private final Personagem mestreSmith;
    private final Personagem olivia;
    private final Personagem james;

    public Capitulo09() {
        super(ID, "O Passaporte", new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Cidade B"), List.of(Capitulo08.ID));
        this.mestreSmith = new Personagem(3, "Mestre Smith", "Um veterano rigoroso do boxe.", Cores.PRETO, Sexo.MASCULINO);
        this.olivia = new Personagem(2, "Olivia", "Estudante de fisioterapia", Cores.CASTANHO, Sexo.FEMININO);
        this.james = new Personagem(7, "James", "Analista de desempenho esportivo.", Cores.AZUL, Sexo.MASCULINO);
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        List<Dialogo> historia = new ArrayList<>(List.of(
                DialogoNarrativo.narrador("O motor do ônibus ronca suavemente enquanto corta as estradas escuras. A viagem noturna para a Cidade B é longa e reflexiva."),
                new Dialogo(player, "(Olhando melancólico pela janela embaçada) 'Uma última etapa separa a gente do grande Campeonato Mundial. Tanta coisa mudou...'"),
                new Dialogo(mestreSmith, "'Não deixe a mente vagar. Lembre-se de onde você veio. A disciplina é o que te mantém de pé quando as pernas e os pulmões falham.'"),
                new Dialogo(mestreSmith, "'Tente fechar os olhos e descansar agora. Amanhã será o dia mais longo, doloroso e decisivo da sua vida.'"),
                DialogoNarrativo.narrador("O velho Mestre coloca os fones de ouvido e fecha os olhos cansados. O corredor do ônibus mergulha em um silêncio reflexivo."),
                new Dialogo(player, "(Pensando consigo mesmo enquanto sente a tensão muscular) 'Quem eu devo procurar para acalmar minha mente agora, antes que a ansiedade me destrua?'")
        ));

        List<Dialogo.Opcao> opcoes = new ArrayList<>();


        if (player.getAfinidade(olivia) > 0 && player.podeRomancearCom(olivia.getNome())) {
            opcoes.add(Dialogo.opcao("Passar a noite conversando com Olivia", p -> {
                p.definirRomance(olivia.getNome());
                p.concluirCapitulo("ROMANCE_OLIVIA");
            }, List.of(
                    new Dialogo(player, "(Sentando vagarosamente ao lado dela) 'Olivia, eu tenho certeza de que não teria chegado até aqui vivo sem você cuidando de mim.'"),
                    new Dialogo(olivia, "(Sorrindo suavemente e segurando a mão do protagonista) 'Você lutou cada round sangrento sozinho. Eu só cuidei dos arranhões depois que o gongo tocava.'"),
                    new Dialogo(olivia, "'E eu vou continuar aqui, fiel, em todas as lutas, vencendo ou perdendo. Você nunca mais estará lutando sozinho.'")
            )));
        }

        if (player.getAfinidade(james) > 0 && player.podeRomancearCom(james.getNome())) {
            opcoes.add(Dialogo.opcao("Revisar táticas com James", p -> {
                p.definirRomance(james.getNome());
                p.concluirCapitulo("ROMANCE_JAMES");
            }, List.of(
                    new Dialogo(player, "(Sentando ao lado dele enquanto a tela do tablet ilumina seu rosto) 'James, você me fez ver o boxe de um jeito genial e completamente novo. Muito obrigado.'"),
                    new Dialogo(james, "(Sorrindo de forma tímida, fechando o laptop lentamente) 'A matemática por trás das lutas é bonita, não vou negar... mas o enorme coração que você coloca nela...'"),
                    new Dialogo(james, "'Isso é o que realmente impressiona. Sinceramente? Quero continuar analisando suas lutas... e você.'")
            )));
        }

        opcoes.add(Dialogo.opcao("Manter o foco apenas na carreira", p -> {}, List.of(
                new Dialogo(player, "(Isolando-se na poltrona do fundo) 'Amanhã é o dia do julgamento. Não posso me distrair com sentimentos soltos agora. O cinturão é a única coisa que importa.'"),
                DialogoNarrativo.narrador("Você fecha os olhos firmemente, visualizando obcecadamente a luta mortal contra Anthony repetidas vezes até adormecer exausto.")
        )));

        historia.add(Dialogo.escolha("Quem vai procurar no autocarro?", opcoes));
        return historia;
    }
}