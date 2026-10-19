package com.exa863.anselmo_adna.persistencia;

import java.util.ArrayList;
/**
 * Armazena o progresso global do jogador.
 * Mantém os finais alcançados e as conquistas desbloqueadas durante o jogo.
 *
 * @author Anselmo e Adna
 */
public class ProgressoGlobal {

    public ArrayList<String> finaisAlcancados = new ArrayList<>();

    public ArrayList<String> conquistasDesbloqueadas = new ArrayList<>();

    public void registrarFinal(String finalAlcancado) {
        if (!finaisAlcancados.contains(finalAlcancado)) {
            finaisAlcancados.add(finalAlcancado);
        }
    }
    /**
     * Registra um novo final alcançado pelo jogador.
     * O mesmo final não é adicionado mais de uma vez.
     *
     * @param finalAlcancado Identificador do final alcançado.
     */
    public boolean finalJaAlcancado(String finalAlcancado) {
        return finaisAlcancados.contains(finalAlcancado);
    }

    public void registrarConquista(String conquista) {
        if (!conquistasDesbloqueadas.contains(conquista)) {
            conquistasDesbloqueadas.add(conquista);
        }
    }

    public boolean conquistaJaDesbloqueada(String conquista) {
        return conquistasDesbloqueadas.contains(conquista);
    }
}
