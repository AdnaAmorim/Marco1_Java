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

public class Capitulo06 extends Capitulo {

    public static final String ID = "CAPITULO_06";
    private final Personagem james;

    public Capitulo06() {
        super(ID, "Novos Horizontes", new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Academia Profissional"), List.of(Capitulo05.ID));
        this.james = new Personagem(7, "James", "Analista de desempenho esportivo.", Cores.AZUL, Sexo.MASCULINO);
    }

    @Override
    public List<NomeLocal> getLocaisBloqueados() {
        return List.of(NomeLocal.CIDADE_B);
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        return List.of(
                DialogoNarrativo.narrador("A Academia Profissional da Cidade A é intimidadora. Equipamentos de ponta, ringues oficiais e atletas de elite em todo canto."),
                new Dialogo(player, "'É um mundo completamente diferente da academia do Mestre Smith...'"),
                DialogoNarrativo.narrador("Enquanto você bate exaustivamente no saco de areia, percebe um rapaz com um tablet anotando cada movimento seu."),
                new Dialogo(james, "'Sua postura defensiva é estruturalmente falha contra canhotos. O seu cruzado de direita te deixa exposto por 0.8 segundos.'"),
                new Dialogo(player, "(Parando o treino, surpreso) 'Você estava analisando minha luta? Quem é você?'"),
                new Dialogo(james, "'Sou analista de desempenho esportivo. Meu nome é James. E os dados que coletei não mentem.'"),
                new Dialogo(james, "'Se você quer sobreviver no circuito brutal da Cidade A, vai precisar de mais do que só força bruta e coragem cega.'"),
                new Dialogo(player, "'Eu cheguei até aqui confiando no meu suor e nos ensinamentos do meu mestre, não em um computador.'"),
                new Dialogo(james, "'E é exatamente por isso que você vai estagnar em breve. Posso prever e anular os movimentos de qualquer adversário seu.'"),
                Dialogo.escolha(
                        "O que você faz?",
                        List.of(
                                Dialogo.opcao("Treinar com foco em análise com James", p -> {
                                    p.concluirCapitulo("FLAG_JAMES");
                                    p.alterarAfinidade(james, 1);
                                }, List.of(
                                        new Dialogo(player, "'Certo. Me mostre o que você encontrou nesses dados. Preciso de toda ajuda possível para não cair.'"),
                                        new Dialogo(james, "(Ajustando os óculos com um sorriso empolgado) 'Excelente decisão. Vamos transformar você em uma máquina eficiente.'"),
                                        DialogoNarrativo.narrador("As próximas horas de treino são intensamente focadas em corrigir micro-movimentos que você nem sabia que fazia.")
                                )),
                                Dialogo.opcao("Continuar treinando sozinho", p -> p.getAtributos().setForca(p.getAtributos().getForca() + 1), List.of(
                                        new Dialogo(player, "'Obrigado pela oferta, James, mas eu confio no meu instinto. O ringue não é uma planilha do Excel.'"),
                                        new Dialogo(james, "'Instintos falham quando o cansaço bate e o oxigênio falta no cérebro. A matemática, não. Boa sorte então.'"),
                                        DialogoNarrativo.narrador("James guarda o tablet na mochila e se afasta, parecendo visivelmente decepcionado com a sua teimosia.")
                                ))
                        )
                )
        );
    }
    @Override
    public boolean podeIniciar(Player player) {
        return player != null && player.getLutasEstaduais() >= 2;
    }
}