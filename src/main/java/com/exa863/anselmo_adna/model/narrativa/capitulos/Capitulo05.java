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
import com.exa863.anselmo_adna.model.world.NomeLocal;

import java.util.List;

public class Capitulo05 extends Capitulo {

    public static final String ID = "CAPITULO_05";
    private final Personagem anthony;
    private final Personagem mestreSmith;

    public Capitulo05() {
        super(ID, "O Encontro com o Trovão", new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Academia de Boxe"), List.of(Capitulo04.ID));
        this.anthony = new Personagem(6, "Anthony", "O Trovão, estrela arrogante do torneio.", Cores.CASTANHO, Sexo.MASCULINO);
        this.mestreSmith = new Personagem(3, "Mestre Smith", "Um veterano rigoroso do boxe.", Cores.PRETO, Sexo.MASCULINO);
    }

    @Override
    public List<NomeLocal> getLocaisBloqueados() {
        return List.of(
                NomeLocal.CIDADE_A,
                NomeLocal.CIDADE_B
        );
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        return List.of(
                DialogoNarrativo.narrador("Os flashes das câmeras iluminam a sala de pesagem da Luta Estadual. A tensão é palpável."),
                new Dialogo(player, "(Subindo na balança) 'Peso batido. Tudo certo.'"),
                DialogoNarrativo.narrador("Do outro lado da sala, uma grande comoção anuncia a chegada do favorito e estrela do torneio."),
                new Dialogo(anthony, "(Rindo alto para os repórteres) 'Olha só quem chegou. O garoto do interior achando que pode brincar com os grandes.'"),
                new Dialogo(player, "(Mantendo o olhar fixo e frio) 'O ringue tem o mesmo tamanho para todo mundo, Anthony.'"),
                new Dialogo(anthony, "(Empurrando o ombro do protagonista rudemente) 'Vou acabar com você no primeiro round. É melhor já ir chamando a ambulância.'"),
                DialogoNarrativo.narrador("Os repórteres disparam dezenas de fotos, esperando que uma briga exploda ali mesmo."),
                new Dialogo(mestreSmith, "(Sussurrando no seu ouvido) 'Deixe ele falar. Ele quer que você perca a cabeça antes mesmo de lutar.'"),
                new Dialogo(mestreSmith, "'Observe os pés dele, estão inquietos. Ele tenta esconder o nervosismo com arrogância.'"),
                Dialogo.escolha(
                        "O que você faz?",
                        List.of(
                                Dialogo.opcao("Responder à provocação", p -> p.concluirCapitulo("RIVALIDADE_ANTHONY"), List.of(
                                        new Dialogo(player, "'O único que vai precisar de ambulância é você, Trovão. Seu tempo de bater em novatos assustados acabou.'"),
                                        new Dialogo(anthony, "(Ficando vermelho de raiva) 'Gostei! Pelo menos vai apanhar com estilo, seu lixo!'"),
                                        DialogoNarrativo.narrador("Os seguranças da organização precisam separar vocês dois antes que a luta comece mais cedo.")
                                )),
                                Dialogo.opcao("Ignorar Anthony", p -> p.getAtributos().setInteligencia(p.getAtributos().getInteligencia() + 2), List.of(
                                        new Dialogo(player, "(Encara Anthony em silêncio absoluto, estudando sua postura e ignorando o empurrão completamente)."),
                                        new Dialogo(anthony, "(Visivelmente irritado com a falta de reação) 'Mudo? Ótimo, vai ser ainda mais fácil te nocautear se você não reagir.'"),
                                        new Dialogo(mestreSmith, "'Muito bem, garoto. A verdadeira resposta se dá apenas com os punhos dentro das cordas.'")
                                ))
                        )
                )
        );
    }
}