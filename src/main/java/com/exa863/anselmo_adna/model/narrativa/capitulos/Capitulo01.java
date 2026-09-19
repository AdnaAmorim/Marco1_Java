package com.exa863.anselmo_adna.model.narrativa.capitulos;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.narrativa.Capitulo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.DialogoNarrativo;
import com.exa863.anselmo_adna.model.narrativa.GatilhoNarrativo;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;
import com.exa863.anselmo_adna.model.stats.Itens;

import java.util.List;

public class Capitulo01 extends Capitulo {

    public static final String ID = "CAPITULO_01";

    public Capitulo01() {
        super(
                ID,
                "Origens",
                new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "casa"),
                List.of()
        );
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        return List.of(
                // 1 a 7: Diálogos e ambientação
                DialogoNarrativo.narrador("O cheiro de poeira e suor antigo ainda paira no ar do velho quarto."),
                new Dialogo(player, "(Olhando para a velha caixa de papelão) 'Essas coisas estão aqui há anos...'"),
                DialogoNarrativo.narrador("A luz do fim de tarde entra pela janela, iluminando recortes de jornais desbotados na parede."),
                new Dialogo(player, "'Meu pai era o melhor... até que o ringue cobrou o preço final.'"),
                DialogoNarrativo.narrador("Você afasta algumas roupas velhas e encontra algo de couro no fundo da caixa."),
                DialogoNarrativo.sistema("Você encontrou as Luvas do Pai. Elas estão gastas, mas carregam um peso enorme."),
                new Dialogo(player, "(Passando o polegar sobre o couro rachado) 'Ele sempre dizia que o verdadeiro combate acontece na mente.'"),

                // 8: Escolha entre pegar ou não as luvas
                Dialogo.escolha(
                        "O que você faz diante das luvas?",
                        List.of(
                                Dialogo.opcao(
                                        "Pegar as Luvas",
                                        this::acaoPegarLuvas,
                                        List.of(
                                                new Dialogo(player, "'Eu não posso fugir disso. É o meu legado.'"),
                                                DialogoNarrativo.narrador("Você sente o peso da responsabilidade ao colocar as luvas na bolsa."),
                                                new Dialogo(player, "'Vou honrar o nome da nossa família. Onde ele parou, eu vou continuar.'")
                                        )
                                ),
                                Dialogo.opcao(
                                        "Não pegar as Luvas",
                                        this::acaoNaoPegarLuvas,
                                        List.of(
                                                new Dialogo(player, "'Ainda não estou pronto para carregar esse peso. Preciso do meu próprio caminho.'"),
                                                DialogoNarrativo.narrador("Você fecha a caixa, deixando as luvas no escuro mais uma vez."),
                                                new Dialogo(player, "'Se eu for lutar, será por mim. Com as minhas próprias mãos.'")
                                        )
                                )
                        )
                ),

                // 15 e 16: Conclusão
                new Dialogo(player, "(Respirando fundo e pegando a mochila) 'De qualquer forma, o torneio estadual não vai esperar por mim.'"),
                DialogoNarrativo.sistema("Novo local desbloqueado: Academia Física.")
        );
    }

    // Funções separadas para as ações/consequências
    private void acaoPegarLuvas(Player player) {
        player.getInventario().adicionarItem(Itens.LUVA_DO_PAI);
    }

    private void acaoNaoPegarLuvas(Player player) {
        // O jogador opta por não carregar o item no momento
    }
}
