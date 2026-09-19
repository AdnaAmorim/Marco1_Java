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
        List<Dialogo> historia = new ArrayList<>(List.of(
                DialogoNarrativo.narrador("Chegamos ao ápice. O Campeonato Mundial de Boxe, sediado na gigantesca e apoteótica arena iluminada da Cidade B."),
                DialogoNarrativo.narrador("A multidão enfurecida ruge como um oceano furioso de vozes. As luzes cegam sua visão temporariamente e o locutor principal grita seu nome para o mundo ouvir."),
                DialogoNarrativo.sistema("[Locutor Oficial]: 'E no córner azul... o desafiante que chocou todo o país ao chegar até aqui! O homem sem medo!'"),
                new Dialogo(anthony, "(Batendo as luvas agressivamente, com os olhos injetados e cheios de fúria) 'Chegamos ao fim da linha, caipira insolente. Vamos ver de que lama você é realmente feito.'"),
                new Dialogo(player, "(Respirando fundo e erguendo a guarda clássica) 'Estou feito do mesmo legado impecável que meu pai. E hoje, o passado vai te nocautear com meus punhos.'"),
                DialogoNarrativo.narrador("O gongo soa como um trovão. O último e derradeiro combate da sua vida, a provação de fogo, finalmente começa.")
        ));

        // SISTEMA DE VERIFICAÇÃO DE FINAIS
        boolean aceitouContrato = player.isCapituloConcluido("FLAG_CONTRATO_ACEITO");
        boolean venceuLuta = (player.getAtributos().getForca() + player.getAtributos().getAgilidade()) >= 3;

        if (aceitouContrato) {
            if (!venceuLuta) {
                historia.add(DialogoNarrativo.narrador("Você cumpriu a ordem vergonhosa e fingiu o doloroso nocaute no 3º round. Sua parte do acordo sujo foi feita."));
                historia.add(DialogoNarrativo.narrador("Mas as perigosas organizações que apostaram milhões em você não perdoaram a traição e a covardia. O dinheiro maldito nunca chegou a ser gasto na sua curta vida."));
                historia.add(DialogoNarrativo.sistema("FIM 1: A ALMA VENDIDA (Derrota Opcional)"));
            } else {
                historia.add(DialogoNarrativo.narrador("O orgulho falou mais alto e você se recusou a cair na lona. Você aniquilou Anthony com um cruzado brilhante e espetacular de direita no último segundo."));
                historia.add(DialogoNarrativo.narrador("Porém, você quebrou o letal contrato de Noor. A sua glória perante a multidão foi eterna, mas sua vida terminou tragicamente naquela mesma noite chuvosa, em um beco atrás da arena."));
                historia.add(DialogoNarrativo.sistema("FIM 2: A ALMA VENDIDA (Vitória Trágica)"));
            }
        } else {
            if (venceuLuta) {
                historia.add(DialogoNarrativo.narrador("O ringue sangrento silencia antes da explosão de gritos. O juiz levanta o seu braço direito com força e convicção. O choro de alívio e dor é completamente inevitável."));
                historia.add(new Dialogo(player, "(Erguendo as sagradas Luvas do Pai com a mão trêmula) 'Pai, onde quer que você esteja... nós finalmente conseguimos! O nosso legado de sangue está salvo e eternamente limpo!'"));
                historia.add(DialogoNarrativo.sistema("FIM 3: O CAMPEÃO DO MUNDO"));
            } else {
                historia.add(DialogoNarrativo.narrador("Anthony simplesmente foi superior técnico. Seus músculos falharam. A lona dura e gelada beija o seu rosto em câmera lenta. É o doloroso fim de jogo."));
                historia.add(new Dialogo(player, "(Anos depois, treinando pacientemente o jovem Chris em uma modesta academia) 'Eu não venci o mundial naquele dia escuro, garoto. Mas com a generosa bolsa da luta, abrimos esta academia juntos.'"));
                historia.add(new Dialogo(player, "'Eu finalmente entendi que o meu verdadeiro legado não era carregar um pedaço de ouro na cintura... era você. Mantenha essas mãos altas.'"));
                historia.add(DialogoNarrativo.sistema("FIM 4: O VERDADEIRO LEGADO"));
            }
        }

        return historia;
    }
}