package com.exa863.anselmo_adna.model.combat.estilos;

import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.stats.Atributo;

public class Velocista extends EstiloLuta {

    public static final double MULTIPLICADOR_PENALIDADE_FORCA = 0.80; // -20% de força, bate mais fraco
    public static final double MULTIPLICADOR_BONUS_ENERGIA = 1.20;    // +20% de energia, cansa menos

    public Velocista() {
        super("Velocista (Out-Boxer)", "Mantém a distância, se mexe rápido e acerta muitos golpes.");
    }

    @Override
    public AtributosEfetivos aplicarModificadores(Atributo base) {
        double f = base.getForca() * MULTIPLICADOR_PENALIDADE_FORCA;
        double a = base.getAgilidade();
        double r = base.getResistencia();
        double i = base.getInteligencia();
        double e = base.getEnergia() * MULTIPLICADOR_BONUS_ENERGIA;
        double s = base.getSaude();

        // ganha bonus na agilidade
        double bonusPercentual = calcularPercentualBonus(a, Atributo.MAX_HABILIDADE);
        double agilidadeModificada = a * (1.0 + bonusPercentual);

        return new AtributosEfetivos(f, agilidadeModificada, r, i, e, s);
    }
}
