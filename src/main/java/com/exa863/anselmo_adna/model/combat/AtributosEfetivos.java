package com.exa863.anselmo_adna.model.combat;

// guarda os atributos temporarios so durante a luta
// junta os atributos base com os bonus e penalidades do estilo de luta,
// sem mexer nos atributos reais e permanentes do lutador
public class AtributosEfetivos {
    public final double forca;
    public final double agilidade;
    public final double resistencia;
    public final double inteligencia;
    public final double energia;
    public final double saude;

    public AtributosEfetivos(double forca, double agilidade, double resistencia,
                             double inteligencia, double energia, double saude) {
        this.forca = forca;
        this.agilidade = agilidade;
        this.resistencia = resistencia;
        this.inteligencia = inteligencia;
        this.energia = energia;
        this.saude = saude;
    }
}
