package com.exa863.anselmo_adna.model.world;

public class DataDia {
    private int dias = 1;
    private int horas = 6;  // O dia começa às 6h
    private int minutos = 0;

    public void avancarMinutos(int quantidade) {
        minutos += quantidade;
        while (minutos >= 60) {
            minutos -= 60;
            horas++;

            if (horas >= 24) {
                horas = 0;
                dias++;
            }
        }
    }

    public String getHoraFormatada() {
        return String.format("%02d:%02d", horas, minutos);
    }

    public String getDiaFormatado() {
        return "Dia " + dias;
    }

    public int getDias() { return dias; }
    public int getHoras() { return horas; }
    public int getMinutos() { return minutos; }
}
