package com.exa863.anselmo_adna.model.narrativa;

import java.util.Random;

public class Dialogos {

    private static final String[] FALAS_AVOS = {
            "Sua avó grita da cozinha: 'Deixei um pedaço de bolo na mesa, não vá treinar com fome!'",
            "Seu avô murmura enquanto lê o jornal: 'Seu pai tinha esse mesmo brilho nos olhos... tome cuidado.'",
            "Sua avó aparece na porta: 'Filho, você está cheio de hematomas! Passe uma pomada nisso.'",
            "Seu avô aponta para o saco de pancadas: 'O segredo não é só bater forte. É não ser atingido.'",
            "Sua avó suspira: 'Não gosto dessas suas lutas, mas estarei sempre torcendo por você.'",
            "Seu avô sorri de canto: 'Lembre-se: guarda alta e pernas leves. É assim que a gente sobrevive.'",
            "Você escuta sua avó rezando baixinho no quarto ao lado, pedindo pela sua proteção nos ringues.",
            "Seu avô cruza com você no corredor e dá dois tapinhas no seu ombro. Um apoio silencioso.",
            "Sua avó adverte: 'Vê se não gasta todo o seu dinheiro com essas coisas de academia, ouviu?'",
            "Seu avô ajeita os óculos: 'Bata nesse saco como se estivesse batendo nos problemas da vida, garoto.'"
    };

    private static final Random RANDOM = new Random();

    // Construtor privado para evitar instanciação, já que só usaremos métodos estáticos
    private Dialogos() {}

    public static String obterDialogoAleatorioAvos() {
        return FALAS_AVOS[RANDOM.nextInt(FALAS_AVOS.length)];
    }
}
