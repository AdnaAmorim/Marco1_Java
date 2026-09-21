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

/**
 * Capítulo 2: Forjando o Corpo. Acontece na academia física.
 *
 * @author Anselmo e Adna
 */
public class Capitulo02 extends Capitulo {

    public static final String ID = "CAPITULO_02";
    private final Personagem olivia;

    public Capitulo02() {
        super(
                ID,
                "Forjando o Corpo",
                new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Academia"),
                List.of(Capitulo01.ID)
        );
        this.olivia = new Personagem(2, "Olivia", "Estudante de fisioterapia", Cores.CASTANHO, Sexo.FEMININO);
    }

    @Override
    public List<NomeLocal> getLocaisBloqueados() {
        return List.of(
                NomeLocal.ACADEMIA_BOXE,
                NomeLocal.CLUBE_LUTA,
                NomeLocal.CIDADE_A,
                NomeLocal.CIDADE_B
        );
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        return List.of(
                DialogoNarrativo.narrador("O som de pesos batendo ecoa pela Academia Física. O cheiro de magnésio e suor é intenso."),
                new Dialogo(player, "(Ofegante) 'Só mais uma... série... Eu consigo...'"),
                DialogoNarrativo.narrador("Seus músculos queimam. A barra do supino parece pesar o dobro do que pesava no início."),
                new Dialogo(olivia, "'Ei, vai devagar! Você está sobrecarregando o ombro esquerdo.'"),
                new Dialogo(player, "(Assustado, quase deixando a barra cair) 'Quem é você? E de onde surgiu?'"),
                new Dialogo(olivia, "(Ajudando a encaixar a barra no suporte) 'Sou Olivia, estudante de fisioterapia.'"),
                new Dialogo(olivia, "'Se continuar com essa postura torta, vai se lesionar antes mesmo de pisar no ringue.'"),
                new Dialogo(player, "'Eu preciso estar forte para o torneio. Não tenho tempo a perder.'"),
                new Dialogo(olivia, "'Força sem equilíbrio é só um atalho para o hospital. Quer um conselho de verdade?'"),
                Dialogo.escolha(
                        "O que você faz?",
                        List.of(
                                Dialogo.opcao(
                                        "Pedir ajuda a Olivia",
                                        this::acaoPedirAjuda,
                                        List.of(
                                                new Dialogo(player, "'Você tem razão. Pode me mostrar a postura correta?'"),
                                                new Dialogo(olivia, "(Sorrindo e pegando um colchonete) 'Claro! Vamos ajustar essa base primeiro.'"),
                                                new Dialogo(player, "'Ai! Nunca pensei que alongamento doesse tanto...'")
                                        )
                                ),
                                Dialogo.opcao(
                                        "Treinar sozinho",
                                        this::acaoTreinarSozinho,
                                        List.of(
                                                new Dialogo(player, "'Eu sei o que estou fazendo. Obrigado, mas prefiro treinar sozinho.'"),
                                                new Dialogo(olivia, "(Suspirando e cruzando os braços) 'Tudo bem, o ombro é seu. Só não diga que não avisei.'"),
                                                new Dialogo(player, "(Sentindo uma fisgada de dor logo depois) 'Talvez ela tivesse um pouco de razão... mas não posso parar agora.'")
                                        )
                                )
                        )
                )
        );
    }

    private void acaoPedirAjuda(Player player) {
        player.getAtributos().setSaude(player.getAtributos().getSaude() + 10);
        player.alterarAfinidade(olivia, 1);
    }

    private void acaoTreinarSozinho(Player player) {
        player.getAtributos().setSaude(player.getAtributos().getSaude() - 10);
    }
}