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

public class Capitulo07 extends Capitulo {

    public static final String ID = "CAPITULO_07";
    private final Personagem alexandra;

    public Capitulo07() {
        super(ID, "A Sombra da Corrupção", new GatilhoNarrativo(TipoGatilho.ENTRAR_LOCAL, "Casa"), List.of(Capitulo06.ID));
        this.alexandra = new Personagem(8, "Alexandra Cruz", "Repórter investigativa implacável.", Cores.VERDE, Sexo.FEMININO);
    }

    @Override
    public List<Dialogo> getDialogos(Player player) {
        return List.of(
                DialogoNarrativo.narrador("Você termina uma exaustiva sessão de autógrafos após a Luta Estadual 2. A fama começa a cobrar seu preço em cansaço."),
                new Dialogo(alexandra, "'Com licença. Você tem um minuto para responder algumas perguntas para a imprensa?'"),
                new Dialogo(player, "'Claro, o que você quer saber? Como foi a preparação para o nocaute técnico?'"),
                new Dialogo(alexandra, "(Mostrando rapidamente um crachá oficial) 'Sou Alexandra Cruz, repórter investigativa. Não ligo para o nocaute.'"),
                new Dialogo(player, "(Franzindo a testa) 'Repórter investigativa na editoria de esportes? Isso é algo novo.'"),
                new Dialogo(alexandra, "'Estou investigando um fluxo altamente suspeito de apostas que assola as ligas deste estado. Lutas estão sendo compradas diariamente.'"),
                new Dialogo(alexandra, "'O nome do empresário Victor Noor apareceu nas planilhas do submundo. E minhas fontes dizem que ele conversou com você no mês passado.'"),
                new Dialogo(player, "(Engolindo seco e olhando para os lados) 'Eu converso com muita gente depois das lutas...'"),
                new Dialogo(alexandra, "(Aproximando-se, em um tom ríspido de urgência) 'Vou ser direta: você é só mais um fantoche dele ou está lutando limpo?'"),
                Dialogo.escolha(
                        "O que você faz?",
                        List.of(
                                Dialogo.opcao("Contar o que sabe a Alexandra", p -> p.concluirCapitulo("FLAG_ALEXANDRA"), List.of(
                                        new Dialogo(player, "'Olha, ele me procurou no vestiário com propostas bem estranhas de atalhos e patrocínios nebulosos. Mas eu os recusei.'"),
                                        new Dialogo(alexandra, "'Isso confirma toda a minha teoria inicial. Cuidado redobrado, garoto, ele destrói a carreira de quem não colabora.'"),
                                        new Dialogo(player, "'Se precisar do meu testemunho oficial, me avise. Quero meu esporte limpo acima de tudo.'")
                                )),
                                Dialogo.opcao("Não falar sobre Noor", p -> {}, List.of(
                                        new Dialogo(player, "'Eu só foco no meu árduo treinamento e nas minhas lutas. Não sei de nada sobre apostas clandestinas ou sobre esse tal de Noor.'"),
                                        new Dialogo(alexandra, "(Anotando no bloco, extremamente frustrada) 'Todo lutador covarde diz a mesma coisa... Se mudar de ideia, me ligue. Antes que seja tarde.'"),
                                        DialogoNarrativo.narrador("Ela vira as costas bufando e desaparece rapidamente na multidão, deixando uma terrível pulga atrás da sua orelha.")
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