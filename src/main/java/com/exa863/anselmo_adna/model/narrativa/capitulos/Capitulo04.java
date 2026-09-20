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
import com.exa863.anselmo_adna.model.stats.Itens;
import com.exa863.anselmo_adna.model.world.NomeLocal;

import java.util.List;

public class Capitulo04 extends Capitulo {

    public static final String ID = "CAPITULO_04";
    private final Personagem victorNoor;

    public Capitulo04() {
        super(
                ID,
                "O Submundo",
                new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Academia de Boxe"),
                List.of(Capitulo03.ID)
        );
        this.victorNoor = new Personagem(5, "Victor Noor", "Um empresário rico do submundo das apostas.", Cores.PRETO, Sexo.MASCULINO);
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
                DialogoNarrativo.narrador("O vestiário está silencioso e cheira a arnica após a sua segunda vitória nas lutas locais."),
                new Dialogo(player, "(Tirando as bandagens das mãos) 'Duas vitórias seguidas. O campeonato estadual está cada vez mais perto.'"),
                DialogoNarrativo.narrador("A porta se abre lentamente. Um homem bem vestido entra, seguido pelo cheiro de um charuto caro."),
                new Dialogo(victorNoor, "(Encostado na porta, sorrindo de lado) 'Você tem o cruzado do seu pai. Impressionante.'"),
                new Dialogo(player, "(Ficando de pé rapidamente) 'Quem é você e o que faz no meu vestiário?'"),
                new Dialogo(victorNoor, "'Calma, garoto. Eu sou alguém que pode transformar essas vitórias de fundo de quintal em dinheiro de verdade.'"),
                new Dialogo(victorNoor, "'Me chame de Noor. Victor Noor.'"),
                new Dialogo(player, "'Eu não preciso de ajuda de engravatados. Eu luto por mim mesmo.'"),
                new Dialogo(victorNoor, "'Isso é muito poético, mas a poesia não paga os médicos quando você quebrar a mandíbula.'"),
                Dialogo.escolha(
                        "O que você faz diante da proposta?",
                        List.of(
                                Dialogo.opcao(
                                        "Ouvir a proposta de Noor",
                                        this::acaoOuvirNoor,
                                        List.of(
                                                new Dialogo(player, "'Estou ouvindo. Mas seja rápido.'"),
                                                new Dialogo(victorNoor, "(Dando uma tragada no charuto) 'A glória tem um preço, garoto. E eu ofereço o caminho mais curto.'"),
                                                new Dialogo(victorNoor, "'Quando precisar de recursos de verdade, você sabe onde me encontrar.'")
                                        )
                                ),
                                Dialogo.opcao(
                                        "Recusar a conversa",
                                        this::acaoRecusar,
                                        List.of(
                                                new Dialogo(player, "'Não estou à venda. E nem tenho interesse nos seus atalhos. Saia do meu vestiário agora.'"),
                                                new Dialogo(victorNoor, "(Apagando o charuto na parede com desprezo) 'Ideais elevados costumam afundar rápido neste circuito. Boa sorte.'"),
                                                DialogoNarrativo.narrador("Ele vai embora, mas a sensação perturbadora de que você está sendo observado permanece.")
                                        )
                                )
                        )
                )
        );
    }

    private void acaoOuvirNoor(Player player) {
        player.setDinheiro(player.getDinheiro() + 1500);
        player.getInventario().adicionarItem(Itens.CONTRATO);
        player.alterarAfinidade(victorNoor, 10);
    }

    private void acaoRecusar(Player player) {
    }
}