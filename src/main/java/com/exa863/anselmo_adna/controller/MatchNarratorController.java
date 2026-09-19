package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.Lutador;
import com.exa863.anselmo_adna.model.combat.Match;
import com.exa863.anselmo_adna.model.combat.Round;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.TipoVitoria;
import com.exa863.anselmo_adna.model.narrativa.BancoFrases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MatchNarratorController {

    private final Random random;
    private final Set<String> frasesUsadas;

    // valores minimos pra classificar atributo
    // (atributos podem passar de 10 com os bonus)
    private static final double MINIMO_PARA_ATRIBUTO_ALTO = 10.0;
    private static final double MINIMO_PARA_ATRIBUTO_MEDIO = 6.0;

    // chances pra sortear intensidade da frase
    // pra atributo alto
    private static final int CHANCE_FRASE_EPICA_ATRIBUTO_ALTO = 70;
    private static final int CHANCE_FRASE_BOA_ATRIBUTO_ALTO = 95;

    // pra atributo medio
    private static final int CHANCE_FRASE_EPICA_ATRIBUTO_MEDIO = 20;
    private static final int CHANCE_FRASE_BOA_ATRIBUTO_MEDIO = 80;

    // pra atributo baixo
    private static final int CHANCE_FRASE_EPICA_ATRIBUTO_BAIXO = 5;
    private static final int CHANCE_FRASE_BOA_ATRIBUTO_BAIXO = 35;

    // chance de soltar comentario de narrador no round
    private static final double CHANCE_COMENTARIO_ALEATORIO = 0.25;

    // vencedor tem mais chance de atacar
    private static final double CHANCE_VENCEDOR_SER_ATOR = 0.75;

    public MatchNarratorController() {
        this.random = new Random();
        this.frasesUsadas = new HashSet<>();
    }

    public void adicionarNarracao(Match match) {
        this.frasesUsadas.clear(); // limpa frases antigas pra n bugar proximas lutas

        ResultadoLuta resultado = match.getResultadoLuta();
        Lutador vencedor = resultado.getVencedor();
        Lutador perdedor = resultado.getPerdedor();

        // passa pelos rounds ja criados no match
        for (Round round : match) {

            if (round.isUltimo()) {
                // se for ultimo round falso de finalizacao, poe frase de fim e pronto
                round.addEvento(gerarFraseFimDeLuta(resultado));
                continue;
            }

            // cada round tem de 2 a 4 interacoes
            int eventosNoRound = 2 + random.nextInt(3);

            for (int j = 0; j < eventosNoRound; j++) {
                boolean vencedorAje = random.nextDouble() < CHANCE_VENCEDOR_SER_ATOR;
                Lutador ator = vencedorAje ? vencedor : perdedor;
                Lutador alvo = vencedorAje ? perdedor : vencedor;

                AtributosEfetivos eff = ator.getAtributosEfetivos();

                double pesoBruto = eff.forca;
                double pesoEsquiva = eff.agilidade;
                double pesoRapido = eff.agilidade;

                double pesoTotal = pesoBruto + pesoEsquiva + pesoRapido;
                if (pesoTotal <= 0) pesoTotal = 1;

                double sorteio = random.nextDouble() * pesoTotal;

                String fraseTurno;
                if (sorteio < pesoBruto) {
                    fraseTurno = gerarFrase(BancoFrases.DANO_BRUTO, ator, alvo, eff);
                } else if (sorteio < pesoBruto + pesoEsquiva) {
                    fraseTurno = gerarFrase(BancoFrases.ESQUIVAS, ator, alvo, eff);
                } else {
                    fraseTurno = gerarFrase(BancoFrases.DANO_RAPIDO, ator, alvo, eff);
                }

                round.addEvento(fraseTurno);
            }

            // comentario de narrador estilo galvao no meio
            if (random.nextDouble() < CHANCE_COMENTARIO_ALEATORIO) {
                round.addEvento(sortearFraseUnica(BancoFrases.COMENTARIOS_ALEATORIOS));
            }
        }
    }

    // pega o banco 2d, ve intensidade do cara e sorteia a frase certa pra ele
    private String gerarFrase(String[][] bancoEventos, Lutador ator, Lutador alvo, AtributosEfetivos eff) {
        int intensidade = calcularIntensidadeAtor(eff, bancoEventos);

        // Pega o array de frases correspondente àquela intensidade
        String[] frases = bancoEventos[intensidade];

        return sortearEAplicar(frases, ator, alvo);
    }

    // ve atributos do cara pra definir intensidade da frase
    private int calcularIntensidadeAtor(AtributosEfetivos eff, String[][] bancoEventos) {
        double valorBase = 5.0; // default

        if (eff != null) {
            // dano bruto=forca, dano rapido e esquiva=agilidade
            if (bancoEventos == BancoFrases.DANO_BRUTO) {
                valorBase = eff.forca;
            } else {
                valorBase = eff.agilidade;
            }
        }

        // chances de intensidade
        int sorteio = random.nextInt(100);

        if (valorBase >= MINIMO_PARA_ATRIBUTO_ALTO) {
            // se for alto quase sempre gera frase foda ou boa
            if (sorteio < CHANCE_FRASE_EPICA_ATRIBUTO_ALTO) return BancoFrases.INTENSIDADE_ALTA;
            if (sorteio < CHANCE_FRASE_BOA_ATRIBUTO_ALTO) return BancoFrases.INTENSIDADE_MEDIA;
            return BancoFrases.INTENSIDADE_BAIXA;

        } else if (valorBase >= MINIMO_PARA_ATRIBUTO_MEDIO) {
            // se for medio maioria normal
            if (sorteio < CHANCE_FRASE_EPICA_ATRIBUTO_MEDIO) return BancoFrases.INTENSIDADE_ALTA;
            if (sorteio < CHANCE_FRASE_BOA_ATRIBUTO_MEDIO) return BancoFrases.INTENSIDADE_MEDIA;
            return BancoFrases.INTENSIDADE_BAIXA;

        } else {
            // se for baixo so frase fraca, quase nunca acerta algo foda
            if (sorteio < CHANCE_FRASE_EPICA_ATRIBUTO_BAIXO) return BancoFrases.INTENSIDADE_ALTA;
            if (sorteio < CHANCE_FRASE_BOA_ATRIBUTO_BAIXO) return BancoFrases.INTENSIDADE_MEDIA;
            return BancoFrases.INTENSIDADE_BAIXA;
        }
    }

    private String gerarFraseFimDeLuta(ResultadoLuta resultado) {
        Lutador lutador1 = resultado.getLutador1();
        Lutador lutador2 = resultado.getLutador2();
        TipoVitoria tipoVitoria = resultado.getTipoVitoria();

        if (tipoVitoria == TipoVitoria.EMPATE) {
            String frase = sortearFraseUnica(BancoFrases.EMPATES);
            return aplicarNomes(frase, lutador1.getNome(), lutador2.getNome());
        }

        Lutador vencedor = resultado.getVencedor();
        Lutador perdedor = resultado.getPerdedor();

        if (tipoVitoria == TipoVitoria.NOCAUTE) {
            String frase = sortearFraseUnica(BancoFrases.NOCAUTES);
            return aplicarNomes(frase, vencedor.getNome(), perdedor.getNome());

        } else if (tipoVitoria == TipoVitoria.DECISAO_UNANIME) {
            String frase = sortearFraseUnica(BancoFrases.DECISOES_UNANIMES);
            return aplicarNomes(frase, vencedor.getNome(), perdedor.getNome());

        } else { // decisao dividida
            String frase = sortearFraseUnica(BancoFrases.DECISOES_DIVIDIDAS);
            return aplicarNomes(frase, vencedor.getNome(), perdedor.getNome());
        }
    }

    // funçao pra tacar os nomes nas string
    private String aplicarNomes(String frase, String atacante, String adversario) {
        return frase.replace("{atacante}", atacante)
                .replace("{adversario}", adversario);
    }

    // sorteia a frase e ja põe os nomes
    private String sortearEAplicar(String[] banco, Lutador atacante, Lutador adversario) {
        String frase = sortearFraseUnica(banco);
        return aplicarNomes(frase, atacante.getNome(), adversario.getNome());
    }

    // garante q frase n repete na msm luta
    private String sortearFraseUnica(String[] banco) {
        List<String> disponiveis = new ArrayList<>();
        for (String frase : banco) {
            if (!frasesUsadas.contains(frase)) {
                disponiveis.add(frase);
            }
        }

        // se acabar as frases desse banco a gente limpa so ele
        if (disponiveis.isEmpty()) {
            for (String frase : banco) {
                frasesUsadas.remove(frase);
            }
            String sorteada = banco[random.nextInt(banco.length)];
            frasesUsadas.add(sorteada);
            return sorteada;
        }

        String sorteada = disponiveis.get(random.nextInt(disponiveis.size()));
        frasesUsadas.add(sorteada);
        return sorteada;
    }
}