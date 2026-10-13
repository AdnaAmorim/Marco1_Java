package com.exa863.anselmo_adna.model.combat;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;

import java.util.List;
import java.util.Random;

/**
 * Catálogo dos 15 oponentes pré-definidos da Academia de Boxe (circuito amador).
 */
public class OponentesBoxe {

    public record InfoOponente(
            int id,
            String nome,
            String apelido,
            String descricao,
            EstiloLuta estilo,
            int forca,
            int agilidade,
            int resistencia,
            int inteligencia,
            int premio
    ) {
        public Lutador criarLutador() {
            String nomeCompleto = nome + " \"" + apelido + "\"";
            Personagem p = new Personagem(id, nomeCompleto, descricao, Cores.CASTANHO, Sexo.MASCULINO);
            // Ordem do construtor: saude, forca, agilidade, resistencia, inteligencia, energia
            Atributo attr = new Atributo(100, forca, agilidade, resistencia, inteligencia, 100);
            return new Lutador(p, attr, estilo);
        }
    }

    public static final List<InfoOponente> OPONENTES = List.of(
            new InfoOponente(
                    1, "Zeca Silva", "Quebra-Queixo",
                    "Veterano dos ringues de várzea. Mão pesadíssima, mas pés lentos.",
                    new Nocauteador(), 3, 1, 3, 1, 120
            ),
            new InfoOponente(
                    2, "Marcos Lima", "Flecha da Baixada",
                    "Garoto elétrico com fintas rápidas e bom jogo de pernas.",
                    new Velocista(), 1, 3, 2, 2, 100
            ),
            new InfoOponente(
                    3, "Tião Rocha", "A Muralha",
                    "Guarda dupla cerrada como uma porta de ferro. Espera o erro alheio.",
                    new Contragolpeador(), 2, 2, 3, 2, 130
            ),
            new InfoOponente(
                    4, "Jailson Santos", "O Trator",
                    "Caminha sempre para frente sem medo de levar socos para conectar um.",
                    new Nocauteador(), 3, 2, 2, 1, 110
            ),
            new InfoOponente(
                    5, "Danilo Pereira", "Sombra",
                    "Usa jab e passada lateral constante para cansar e frustrar o rival.",
                    new Velocista(), 1, 3, 1, 3, 105
            ),
            new InfoOponente(
                    6, "Beto Carneiro", "Marreta",
                    "Especialista em castigar a linha de cintura com ganchos curtos.",
                    new Nocauteador(), 3, 1, 2, 2, 115
            ),
            new InfoOponente(
                    7, "Carlinhos Miranda", "Ganso",
                    "Lutador esguio que pontua à longa distância e recua no compasso certo.",
                    new Contragolpeador(), 2, 3, 1, 2, 110
            ),
            new InfoOponente(
                    8, "Edson Vieira", "Cascudo",
                    "Absorve golpes duros sorrindo e devolve com cruzados secos.",
                    new Contragolpeador(), 2, 1, 3, 2, 125
            ),
            new InfoOponente(
                    9, "Renato Lopes", "Vento Frio",
                    "Começa a luta num ritmo frenético para tentar vencer por pontos.",
                    new Velocista(), 2, 3, 2, 1, 115
            ),
            new InfoOponente(
                    10, "Valdir Duarte", "Pancada",
                    "Ex-carregador do cais com pegada natural em busca de nocaute imediato.",
                    new Nocauteador(), 3, 2, 1, 2, 120
            ),
            new InfoOponente(
                    11, "Giba Nascimento", "Cobra Coral",
                    "Frio e calculista. Fica no córner estudando antes de desferir o golpe letal.",
                    new Contragolpeador(), 1, 2, 3, 3, 135
            ),
            new InfoOponente(
                    12, "Luciano Gomes", "Faísca",
                    "Atleta de alta intensidade física que nunca para quieto no ringue.",
                    new Velocista(), 1, 3, 3, 1, 110
            ),
            new InfoOponente(
                    13, "Maurício Farias", "Dinamite",
                    "Explosão pura no primeiro minuto. Perigoso enquanto tiver gás.",
                    new Nocauteador(), 3, 3, 1, 1, 125
            ),
            new InfoOponente(
                    14, "Serjão Antunes", "Touro Velho",
                    "Amador experiente com clinch impecável e malícia nas cordas.",
                    new Contragolpeador(), 2, 1, 3, 3, 140
            ),
            new InfoOponente(
                    15, "Paulinho Costa", "Relâmpago",
                    "Finta cabeça para a esquerda e entra com direto limpo no queixo.",
                    new Velocista(), 2, 3, 1, 2, 115
            )
    );

    private static final Random RANDOM = new Random();

    public static InfoOponente sortear() {
        return OPONENTES.get(RANDOM.nextInt(OPONENTES.size()));
    }

    public static InfoOponente obterPorDia(int dia) {
        // Gera um oponente consistente para o mesmo dia, evitando rerolls imediatos
        int indice = Math.abs((dia * 31 + 7) % OPONENTES.size());
        return OPONENTES.get(indice);
    }
}
