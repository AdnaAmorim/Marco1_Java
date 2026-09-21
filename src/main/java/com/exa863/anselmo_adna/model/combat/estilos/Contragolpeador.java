package com.exa863.anselmo_adna.model.combat.estilos;

import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.stats.Atributo;

/**
 * Estilo de luta que foca em resistência e inteligência.
 * Defende bem e ataca quando o adversário erra. Perde um pouco de agilidade.
 *
 * @author Anselmo e Adna
 */
public class Contragolpeador extends EstiloLuta {

    public static final double MULTIPLICADOR_PENALIDADE_AGILIDADE = 0.85; // -15% de agilidade, perde um pouco de velocidade pra defender

    public Contragolpeador() {
        super("Contragolpeador (Counter-Puncher)", "Defende bem e contra-ataca quando o adversário erra um golpe.");
    }

    @Override
    public AtributosEfetivos aplicarModificadores(Atributo base) {
        double f = base.getForca();
        double a = base.getAgilidade() * MULTIPLICADOR_PENALIDADE_AGILIDADE;
        double r = base.getResistencia();
        double i = base.getInteligencia();
        double e = base.getEnergia();
        double s = base.getSaude();

        double mediaBase = (r + i) / 2.0;

        // ganha bonus usando a media de resistencia e inteligencia
        double bonusPercentual = calcularPercentualBonus(mediaBase, Atributo.MAX_HABILIDADE);

        double resistenciaModificada = r * (1.0 + bonusPercentual);
        double inteligenciaModificada = i * (1.0 + bonusPercentual);

        return new AtributosEfetivos(f, a, resistenciaModificada, inteligenciaModificada, e, s);
    }
}
