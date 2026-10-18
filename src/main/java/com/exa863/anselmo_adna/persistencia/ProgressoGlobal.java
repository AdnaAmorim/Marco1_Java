package com.exa863.anselmo_adna.persistencia;

import java.util.ArrayList;

public class ProgressoGlobal {

    public ArrayList<String> finaisAlcancados = new ArrayList<>();

    public ArrayList<String> conquistasDesbloqueadas = new ArrayList<>();

    public void registrarFinal(String finalAlcancado) {
        if (!finaisAlcancados.contains(finalAlcancado)) {
            finaisAlcancados.add(finalAlcancado);
        }
    }

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
