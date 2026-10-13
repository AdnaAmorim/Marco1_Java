package com.exa863.anselmo_adna.model.stats;

public class Atributo {
    public static final int MIN_VALOR = 0;
    public static final int MAX_HABILIDADE = 10; // para forca, agilidade, resistencia, inteligencia
    public static final int MAX_SAUDE = 100;
    public static final int MAX_ENERGIA = 100;

    private int saude;        // 0-100
    private int forca;        // 0-10
    private int agilidade;    // 0-10
    private int resistencia;  // 0-10
    private int inteligencia; // 0-10
    private int energia;      // 0-100

    public Atributo() {
        setSaude(MAX_SAUDE);
        setEnergia(MAX_ENERGIA);
        setForca(1);
        setAgilidade(1);
        setResistencia(1);
        setInteligencia(1);
    }

    public Atributo(int saude, int forca, int agilidade, int resistencia, int inteligencia, int energia) {
        setSaude(saude);
        setForca(forca);
        setAgilidade(agilidade);
        setResistencia(resistencia);
        setInteligencia(inteligencia);
        setEnergia(energia);
    }

    // garante que o valor nao passe do maximo nem fique negativo
    private int limitar(int valor, int max) {
        return Math.max(MIN_VALOR, Math.min(valor, max));
    }

    public int getSaude() {
        return saude;
    }

    public void setSaude(int saude) {
        this.saude = limitar(saude, MAX_SAUDE);
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = limitar(forca, MAX_HABILIDADE);
    }

    public int getAgilidade() {
        return agilidade;
    }

    public void setAgilidade(int agilidade) {
        this.agilidade = limitar(agilidade, MAX_HABILIDADE);
    }

    public int getResistencia() {
        return resistencia;
    }

    public void setResistencia(int resistencia) {
        this.resistencia = limitar(resistencia, MAX_HABILIDADE);
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = limitar(inteligencia, MAX_HABILIDADE);
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia = limitar(energia, MAX_ENERGIA);
    }
}
