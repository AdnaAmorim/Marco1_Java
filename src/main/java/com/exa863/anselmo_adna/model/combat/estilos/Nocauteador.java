package com.exa863.anselmo_adna.model.combat.estilos;

import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.stats.Atributo;

public class Nocauteador extends EstiloLuta {

    public static final double MULTIPLICADOR_PENALIDADE_AGILIDADE = 0.80; // -20% de agilidade, se mexe mais devagar
    public static final double MULTIPLICADOR_PENALIDADE_ENERGIA = 0.80;   // -20% de energia, gasta mais energia dando soco forte

    public Nocauteador() {
        super("Nocauteador (Slugger)", "Luta de perto batendo forte para vencer por nocaute.");
    }

    @Override
    public AtributosEfetivos aplicarModificadores(Atributo base) {
        double f = base.getForca();
        double a = base.getAgilidade() * MULTIPLICADOR_PENALIDADE_AGILIDADE;
        double r = base.getResistencia();
        double i = base.getInteligencia();
        double e = base.getEnergia() * MULTIPLICADOR_PENALIDADE_ENERGIA;
        double s = base.getSaude();

        // ganha bonus na forca bruta
        double bonusPercentual = calcularPercentualBonus(f, Atributo.MAX_HABILIDADE);
        double forcaModificada = f * (1.0 + bonusPercentual);

        return new AtributosEfetivos(forcaModificada, a, r, i, e, s);
    }
}
