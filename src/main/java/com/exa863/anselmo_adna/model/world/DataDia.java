package com.exa863.anselmo_adna.model.world;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Controla o tempo do jogo em dias, horas e minutos.
 * O dia começa às 6h e avança automaticamente ao realizar ações.
 *
 * @author Anselmo e Adna
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class DataDia {
    private int dias = 1;
    private int horas = 6;  // O dia começa às 6h
    private int minutos = 0;

    public DataDia() {
    }

    @JsonCreator
    public DataDia(@JsonProperty("dias") int dias, @JsonProperty("horas") int horas,
                   @JsonProperty("minutos") int minutos) {
        if (dias < 1 || horas < 0 || horas > 23 || minutos < 0 || minutos > 59) {
            throw new IllegalArgumentException("Data ou horário da partida inválido.");
        }
        this.dias = dias;
        this.horas = horas;
        this.minutos = minutos;
    }

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

    public int getDias() {
        return dias;
    }
    public int getHoras() {
        return horas;
    }
    public int getMinutos() {
        return minutos;
    }
}
