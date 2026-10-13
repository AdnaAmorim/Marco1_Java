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

public class Capitulo08 extends Capitulo {

    public static final String ID = "CAPITULO_08";
    private final Personagem victorNoor;

    public Capitulo08() {
        super(ID, "O Palco Nacional", new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Campeonato Mundial"), List.of(Capitulo07.ID));
        this.victorNoor = new Personagem(5, "Victor Noor", "Empresário rico do submundo das apostas.", Cores.PRETO, Sexo.MASCULINO);
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        List<Dialogo> historia = new ArrayList<>(List.of(
                DialogoNarrativo.narrador("A pressão de estar no circuito Nacional é esmagadora. Câmeras, patrocinadores bilionários e expectativas pesam sobre seus ombros."),
                new Dialogo(player, "(Sentado no banco do vestiário, com a toalha no rosto) 'É hoje. O grande palco nacional. Tudo o que eu sempre sonhei...'"),
                DialogoNarrativo.narrador("De repente, o seu celular vibra estridentemente sobre o banco metálico. É uma mensagem de texto de um número desconhecido e mascarado."),
                DialogoNarrativo.sistema("Victor Noor (Mensagem): 'Chegou a hora de pagar o pedágio do sucesso, garoto. Caia no 3º round hoje. Não ouse resistir. Pense no seu futuro e na sua saúde.'"),
                new Dialogo(player, "(Lendo a mensagem, com as mãos tremendo de pura raiva) 'Maldito... Eles acham que controlam absolutamente tudo.'"),
                new Dialogo(player, "'Se eu ganhar agora, ele acaba com minha carreira fora do ringue e talvez com minha vida. Se eu perder propositalmente, perco o meu legado e minha alma.'"),
                DialogoNarrativo.narrador("Faltam apenas dez minutos agoniantes para a entrada no ringue principal. Você precisa tomar uma decisão imediata que definirá sua vida para sempre.")
        ));

        List<Dialogo.Opcao> opcoes = new ArrayList<>();

        opcoes.add(Dialogo.opcao("Aceitar a proposta de Noor", p -> {
            p.concluirCapitulo("FLAG_CONTRATO_ACEITO");
            p.alterarAfinidade(victorNoor, 50);
        }, List.of(
                new Dialogo(player, "'Eu não tenho escolha... Eu preciso desse dinheiro e dessa projeção para continuar minha vida. Vou engolir meu orgulho e fazer o que ele manda.'"),
                DialogoNarrativo.narrador("Você digita a palavra 'Fechado' e envia a resposta sombria. O peso amargo da culpa e da traição imediatamente esmaga o seu peito.")
        )));

        if (player.isCapituloConcluido("FLAG_JAMES")) {
            opcoes.add(Dialogo.opcao("Pedir análise de James", p -> {
                p.concluirCapitulo("FLAG_ESTRATEGIA_JAMES");
                p.getAtributos().setAgilidade(p.getAtributos().getAgilidade() + 1);
            }, List.of(
                    new Dialogo(player, "'James, Noor quer que eu entregue a luta hoje. Preciso de uma estratégia defensiva perfeita para vencer no ringue sem dar qualquer chance de me roubarem nos pontos.'"),
                    DialogoNarrativo.sistema("[James]: 'Entendido. Eu já mapeei os exatos pontos cegos do adversário dele. Se formos por um nocaute rápido no segundo round, os juízes comprados não terão o que fazer. Siga o plano friamente.'")
            )));
        }

        if (player.isCapituloConcluido("FLAG_ALEXANDRA")) {
            opcoes.add(Dialogo.opcao("Procurar provas com Alexandra", p -> p.concluirCapitulo("INVESTIGACAO_COMPLETA"), List.of(
                    new Dialogo(player, "(Ligando apressadamente) 'Alexandra! Noor acabou de mandar uma mensagem ameaçadora me extorquindo. Podemos usar esse rastreio contra ele?'"),
                    DialogoNarrativo.sistema("[Alexandra]: 'Isso é ouro puro! Faça o backup disso agora! Suba lá e lute para valer, eu cuido de acionar a polícia e prender o lixo lá fora.'")
            )));
        }

        historia.add(Dialogo.escolha("O que você decide fazer?", opcoes));
        historia.add(DialogoNarrativo.narrador("A sirene oficial de chamada soa gravemente pelos corredores de concreto. É hora de colocar as luvas e caminhar em direção ao destino sob os holofotes."));
        return historia;
    }
    @Override
    public boolean podeIniciar(Player player) {
        return player != null && player.getLutasNacionais() >= 1;
    }
}