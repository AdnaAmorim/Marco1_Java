package com.exa863.anselmo_adna.model.stats;

public class Atributo {
    private int saude;        // 0-100
    private int forca;        // 0-10
    private int agilidade;    // 0-10
    private int resistencia;  // 0-10
    private int inteligencia; // 0-10
    private int energia;      // 0-100

    public Atributo() {
        this.saude = 100;
        this.forca = 5;
        this.agilidade = 5;
        this.resistencia = 5;
        this.inteligencia = 5;
        this.energia = 100;
    }

    public Atributo(int saude, int forca, int agilidade, int resistencia, int inteligencia, int energia) {
        this.saude = saude;
        this.forca = forca;
        this.agilidade = agilidade;
        this.resistencia = resistencia;
        this.inteligencia = inteligencia;
        this.energia = energia;
    }

    public int getSaude() { return saude; }
    public void setSaude(int saude) { this.saude = saude; }

    public int getForca() { return forca; }
    public void setForca(int forca) { this.forca = forca; }

    public int getAgilidade() { return agilidade; }
    public void setAgilidade(int agilidade) { this.agilidade = agilidade; }

    public int getResistencia() { return resistencia; }
    public void setResistencia(int resistencia) { this.resistencia = resistencia; }

    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = energia; }
}
