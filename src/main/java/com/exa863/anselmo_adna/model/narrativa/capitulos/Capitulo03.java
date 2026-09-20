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

import java.util.List;

public class Capitulo03 extends Capitulo {

    public static final String ID = "CAPITULO_03";
    private final Personagem mestreSmith;
    private final Personagem chris;

    public Capitulo03() {
        super(
                ID,
                "O Teste do Mestre",
                new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Academia de Boxe"),
                List.of(Capitulo02.ID)
        );
        this.mestreSmith = new Personagem(3, "Mestre Smith", "Um veterano rigoroso do boxe.", Cores.PRETO, Sexo.MASCULINO);
        this.chris = new Personagem(4, "Chris", "Lutador surdo e muito veloz.", Cores.VERDE, Sexo.MASCULINO);
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        return List.of(
                DialogoNarrativo.narrador("A velha Academia de Boxe cheira a couro e resiliência. As paredes estão cheias de pôsteres rasgados."),
                new Dialogo(mestreSmith, "(Avaliando você de cima a baixo) 'Então você é o sangue novo. Olhando pra você, não vejo muito futuro.'"),
                new Dialogo(player, "'Meu pai treinou aqui. Eu vim aprender com o melhor.'"),
                new Dialogo(mestreSmith, "(Estreitando os olhos) 'Seu pai tinha talento, mas talento sem disciplina não é nada.'"),
                new Dialogo(mestreSmith, "'O ringue não perdoa arrogância. Dê 100 voltas. Depois conversamos.'"),
                DialogoNarrativo.narrador("O Mestre vira as costas e volta a limpar os sacos de pancada de forma rigorosa."),
                new Dialogo(chris, "(Acenando em Libras com um sorriso amigável) 'Ele é duro, mas é o melhor.'"),
                new Dialogo(player, "(Respondendo em Libras, meio enferrujado) 'Ele sempre é tão simpático assim?'"),
                new Dialogo(chris, "(Rindo silenciosamente e sinalizando) 'Pior. Quer correr no meu ritmo?'"),
                Dialogo.escolha(
                        "O que você faz?",
                        List.of(
                                Dialogo.opcao(
                                        "Seguir o treino de Chris",
                                        this::acaoSeguirChris,
                                        List.of(
                                                new Dialogo(player, "(Sinalizando) 'Vamos lá. Lado a lado.'"),
                                                new Dialogo(chris, "(Sorrindo, começa a ditar o ritmo da corrida, mostrando atalhos na respiração)."),
                                                DialogoNarrativo.narrador("A conexão entre vocês torna o castigo muito mais leve e cria um laço genuíno.")
                                        )
                                ),
                                Dialogo.opcao(
                                        "Seguir apenas as instruções de Mestre Smith",
                                        this::acaoSeguirMestre,
                                        List.of(
                                                new Dialogo(player, "'Agradeço, Chris, mas vou focar apenas no que o Mestre mandou. Preciso provar meu valor para ele.'"),
                                                new Dialogo(chris, "(Acena com a cabeça, compreendendo perfeitamente, e corre mais à frente sem atrapalhar)."),
                                                new Dialogo(mestreSmith, "(Observando de longe com um leve aceno) 'Foco e obediência. É isso que eu quero ver.'")
                                        )
                                )
                        )
                )
        );
    }

    private void acaoSeguirChris(Player player) {
        player.getAtributos().setEnergia(player.getAtributos().getEnergia() + 15);
        player.alterarAfinidade(chris, 1);
    }

    private void acaoSeguirMestre(Player player) {
        player.getAtributos().setResistencia(player.getAtributos().getResistencia() + 1);
        player.alterarAfinidade(mestreSmith, 1);
    }
}