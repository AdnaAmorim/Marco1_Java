package com.exa863.anselmo_adna.model.combat;

/**
 * Guarda os atributos do lutador só durante a luta, já com os bônus e penalidades do estilo aplicados.
 * Não altera os atributos reais e permanentes do personagem.
 *
 * @author Anselmo e Adna
 */
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
