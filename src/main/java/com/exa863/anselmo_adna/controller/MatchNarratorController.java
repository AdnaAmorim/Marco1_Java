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

/**
 * Gera as frases de narração da luta rodada por rodada,
 * escolhendo as falas de acordo com os atributos e estilo de cada lutador.
 *
 * @author Anselmo e Adna
 */
public class MatchNarratorController {

    private final Random random;
    private final Set<String> frasesUsadas;

    // Valores mínimos para classificar o atributo como alto ou médio (com bônus pode passar de 10)
    private static final double MINIMO_PARA_ATRIBUTO_ALTO = 10.0;
    private static final double MINIMO_PARA_ATRIBUTO_MEDIO = 6.0;

    // Chances de sortear frases fortes ou médias para quem tem atributo alto
    private static final int CHANCE_FRASE_EPICA_ATRIBUTO_ALTO = 70;
    private static final int CHANCE_FRASE_BOA_ATRIBUTO_ALTO = 95;

    // Chances para atributo médio
    private static final int CHANCE_FRASE_EPICA_ATRIBUTO_MEDIO = 20;
    private static final int CHANCE_FRASE_BOA_ATRIBUTO_MEDIO = 80;

    // Chances para atributo baixo
    private static final int CHANCE_FRASE_EPICA_ATRIBUTO_BAIXO = 5;
    private static final int CHANCE_FRASE_BOA_ATRIBUTO_BAIXO = 35;

    private static final double CHANCE_COMENTARIO_ALEATORIO = 0.25;
    // O lutador que está ganhando tem mais chance de atacar no round
    private static final double CHANCE_VENCEDOR_SER_ATOR = 0.75;

    public MatchNarratorController() {
        this.random = new Random();
        this.frasesUsadas = new HashSet<>();
    }

    /**
     * Preenche os rounds da luta com frases de narração dos golpes e comentários do narrador.
     *
     * @param match Luta que vai receber as frases em cada round.
     */
    public void adicionarNarracao(Match match) {
        this.frasesUsadas.clear(); // Limpa as frases da luta anterior para não repetir

        ResultadoLuta resultado = match.getResultadoLuta();
        Lutador vencedor = resultado.getVencedor();
        Lutador perdedor = resultado.getPerdedor();

        for (Round round : match) {

            if (round.isUltimo()) {
                // Round de encerramento: exibe o desfecho da luta
                round.addEvento(gerarFraseFimDeLuta(resultado));
                continue;
            }

            // Cada round possui de 2 a 4 interações de combate
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

            if (random.nextDouble() < CHANCE_COMENTARIO_ALEATORIO) {
                round.addEvento(sortearFraseUnica(BancoFrases.COMENTARIOS_ALEATORIOS));
            }
        }
    }

    private String gerarFrase(String[][] bancoEventos, Lutador ator, Lutador alvo, AtributosEfetivos eff) {
        int intensidade = calcularIntensidadeAtor(eff, bancoEventos);
        String[] frases = bancoEventos[intensidade];
        return sortearEAplicar(frases, ator, alvo);
    }

    private int calcularIntensidadeAtor(AtributosEfetivos eff, String[][] bancoEventos) {
        double valorBase = 5.0;

        if (eff != null) {
            if (bancoEventos == BancoFrases.DANO_BRUTO) {
                valorBase = eff.forca;
            } else {
                valorBase = eff.agilidade;
            }
        }

        int sorteio = random.nextInt(100);

        if (valorBase >= MINIMO_PARA_ATRIBUTO_ALTO) {
            // Atributo alto: quase sempre gera frases fortes
            if (sorteio < CHANCE_FRASE_EPICA_ATRIBUTO_ALTO) return BancoFrases.INTENSIDADE_ALTA;
            if (sorteio < CHANCE_FRASE_BOA_ATRIBUTO_ALTO) return BancoFrases.INTENSIDADE_MEDIA;
            return BancoFrases.INTENSIDADE_BAIXA;

        } else if (valorBase >= MINIMO_PARA_ATRIBUTO_MEDIO) {
            // Atributo médio: maioria frases normais
            if (sorteio < CHANCE_FRASE_EPICA_ATRIBUTO_MEDIO) return BancoFrases.INTENSIDADE_ALTA;
            if (sorteio < CHANCE_FRASE_BOA_ATRIBUTO_MEDIO) return BancoFrases.INTENSIDADE_MEDIA;
            return BancoFrases.INTENSIDADE_BAIXA;

        } else {
            // Atributo baixo: quase sempre frases fracas
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

        } else {
            String frase = sortearFraseUnica(BancoFrases.DECISOES_DIVIDIDAS);
            return aplicarNomes(frase, vencedor.getNome(), perdedor.getNome());
        }
    }

    private String aplicarNomes(String frase, String atacante, String adversario) {
        return frase.replace("{atacante}", atacante)
                .replace("{adversario}", adversario);
    }

    private String sortearEAplicar(String[] banco, Lutador atacante, Lutador adversario) {
        String frase = sortearFraseUnica(banco);
        return aplicarNomes(frase, atacante.getNome(), adversario.getNome());
    }

    // Evita repetição de frases na mesma luta
    private String sortearFraseUnica(String[] banco) {
        List<String> disponiveis = new ArrayList<>();
        for (String frase : banco) {
            if (!frasesUsadas.contains(frase)) {
                disponiveis.add(frase);
            }
        }

        // Se todas as opções deste banco foram exibidas, reinicia o conjunto
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