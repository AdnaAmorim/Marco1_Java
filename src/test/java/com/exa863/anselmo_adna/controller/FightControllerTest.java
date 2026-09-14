package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.TipoVitoria;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FightControllerTest {

    private FightController fightController;
    private EstiloLuta velocista;
    private EstiloLuta nocauteador;
    private EstiloLuta contragolpeador;

    // 3 lutadores especializados
    private Personagem lutadorAgil;
    private Atributo attrAgil;

    private Personagem lutadorPesado;
    private Atributo attrPesado;

    private Personagem lutadorTatico;
    private Atributo attrTatico;

    @BeforeEach
    void setUp() {
        fightController = new FightController();
        velocista = new Velocista();
        nocauteador = new Nocauteador();
        contragolpeador = new Contragolpeador();

        // 1. Especialista em agilidade e volume de golpes
        lutadorAgil = new Personagem(1, "Ali", "Velocista Ágil", Cores.AZUL, Sexo.MASCULINO);
        attrAgil = new Atributo(100, 4, 9, 4, 7, 100);

        // 2. Especialista em forca bruta e nocaute
        lutadorPesado = new Personagem(2, "Tyson", "Nocauteador Pesado", Cores.PRETO, Sexo.MASCULINO);
        attrPesado = new Atributo(100, 9, 3, 7, 4, 100);

        // 3. Especialista em resistencia e contra-ataque
        lutadorTatico = new Personagem(3, "Mayweather", "Contragolpeador Defensivo", Cores.CASTANHO, Sexo.MASCULINO);
        attrTatico = new Atributo(100, 4, 5, 9, 9, 100);
    }

    // =========================================================================
    // 1. ESPECIALISTAS LUTANDO CONTRA SI MESMOS NOS 3 ESTILOS (3x3)
    // =========================================================================

    @Test
    @DisplayName("lutador agil contra ele mesmo nos 3 estilos")
    void testLutadorAgilContraEleMesmo() {
        // mesmo estilo contra ele mesmo deve empatar cravado
        ResultadoLuta empate = fightController.resolverCombate(
                lutadorAgil, attrAgil, velocista,
                lutadorAgil, attrAgil, velocista
        );
        assertTrue(empate.isEmpate());
        assertEquals(TipoVitoria.EMPATE, empate.getTipoVitoria());
        assertEquals(0.0, empate.getSaldoFinal(), 0.001);

        // estilo certo (velocista) contra estilo errado (nocauteador)
        ResultadoLuta vsNocauteador = fightController.resolverCombate(
                lutadorAgil, attrAgil, velocista,
                lutadorAgil, attrAgil, nocauteador
        );
        assertTrue(vsNocauteador.isVencedor(lutadorAgil));
        assertEquals(TipoVitoria.DECISAO_UNANIME, vsNocauteador.getTipoVitoria());
        assertEquals(21.65, vsNocauteador.getSaldoFinal(), 0.01);

        // estilo certo (velocista) contra estilo neutro (contragolpeador)
        ResultadoLuta vsContra = fightController.resolverCombate(
                lutadorAgil, attrAgil, velocista,
                lutadorAgil, attrAgil, contragolpeador
        );
        assertTrue(vsContra.isVencedor(lutadorAgil));
        assertEquals(TipoVitoria.DECISAO_UNANIME, vsContra.getTipoVitoria());
        assertEquals(13.90, vsContra.getSaldoFinal(), 0.01);
    }

    @Test
    @DisplayName("lutador pesado contra ele mesmo nos 3 estilos")
    void testLutadorPesadoContraEleMesmo() {
        // mesmo estilo contra ele mesmo deve empatar cravado
        ResultadoLuta empate = fightController.resolverCombate(
                lutadorPesado, attrPesado, nocauteador,
                lutadorPesado, attrPesado, nocauteador
        );
        assertTrue(empate.isEmpate());
        assertEquals(TipoVitoria.EMPATE, empate.getTipoVitoria());
        assertEquals(0.0, empate.getSaldoFinal(), 0.001);

        // estilo certo (nocauteador) contra pior estilo pra ele (velocista)
        ResultadoLuta vsVelocista = fightController.resolverCombate(
                lutadorPesado, attrPesado, nocauteador,
                lutadorPesado, attrPesado, velocista
        );
        assertTrue(vsVelocista.isVencedor(lutadorPesado));
        assertEquals(TipoVitoria.DECISAO_UNANIME, vsVelocista.getTipoVitoria());
        assertEquals(19.15, vsVelocista.getSaldoFinal(), 0.01);

        // estilo certo (nocauteador) contra estilo neutro (contragolpeador)
        ResultadoLuta vsContra = fightController.resolverCombate(
                lutadorPesado, attrPesado, nocauteador,
                lutadorPesado, attrPesado, contragolpeador
        );
        assertTrue(vsContra.isVencedor(lutadorPesado));
        assertEquals(TipoVitoria.DECISAO_UNANIME, vsContra.getTipoVitoria());
        assertEquals(10.47, vsContra.getSaldoFinal(), 0.01);
    }

    @Test
    @DisplayName("lutador tatico contra ele mesmo nos 3 estilos")
    void testLutadorTaticoContraEleMesmo() {
        // mesmo estilo contra ele mesmo deve empatar cravado
        ResultadoLuta empate = fightController.resolverCombate(
                lutadorTatico, attrTatico, contragolpeador,
                lutadorTatico, attrTatico, contragolpeador
        );
        assertTrue(empate.isEmpate());
        assertEquals(TipoVitoria.EMPATE, empate.getTipoVitoria());
        assertEquals(0.0, empate.getSaldoFinal(), 0.001);

        // estilo certo (contragolpeador) contra estilo errado (velocista)
        ResultadoLuta vsVelocista = fightController.resolverCombate(
                lutadorTatico, attrTatico, contragolpeador,
                lutadorTatico, attrTatico, velocista
        );
        assertTrue(vsVelocista.isVencedor(lutadorTatico));
        assertEquals(TipoVitoria.DECISAO_UNANIME, vsVelocista.getTipoVitoria());
        assertEquals(18.28, vsVelocista.getSaldoFinal(), 0.01);

        // estilo certo (contragolpeador) contra estilo errado (nocauteador)
        ResultadoLuta vsNocauteador = fightController.resolverCombate(
                lutadorTatico, attrTatico, contragolpeador,
                lutadorTatico, attrTatico, nocauteador
        );
        assertTrue(vsNocauteador.isVencedor(lutadorTatico));
        assertEquals(TipoVitoria.DECISAO_UNANIME, vsNocauteador.getTipoVitoria());
        assertEquals(21.78, vsNocauteador.getSaldoFinal(), 0.01);
    }

    // =========================================================================
    // 2. CONFRONTOS CRUZADOS ENTRE OS 3 ESPECIALISTAS
    // =========================================================================

    @Test
    @DisplayName("confrontos cruzados entre os tres lutadores especializados")
    void testConfrontosCruzadosEntreOsTres() {
        // 1. Agil(Velocista) vs Pesado(Nocauteador) -> luta parelha, agil vence por pontos
        ResultadoLuta agilVsPesado = fightController.resolverCombate(
                lutadorAgil, attrAgil, velocista,
                lutadorPesado, attrPesado, nocauteador
        );
        assertTrue(agilVsPesado.isVencedor(lutadorAgil));
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, agilVsPesado.getTipoVitoria());
        assertEquals(5.90, agilVsPesado.getSaldoFinal(), 0.01);

        // 2. Agil(Velocista) vs Tatico(Contragolpeador) -> defesa do tatico vence por pouco
        ResultadoLuta agilVsTatico = fightController.resolverCombate(
                lutadorAgil, attrAgil, velocista,
                lutadorTatico, attrTatico, contragolpeador
        );
        assertTrue(agilVsTatico.isVencedor(lutadorTatico));
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, agilVsTatico.getTipoVitoria());
        assertEquals(-2.03, agilVsTatico.getSaldoFinal(), 0.01);

        // 3. Pesado(Nocauteador) vs Tatico(Contragolpeador) -> contragolpeador neutraliza e vence
        ResultadoLuta pesadoVsTatico = fightController.resolverCombate(
                lutadorPesado, attrPesado, nocauteador,
                lutadorTatico, attrTatico, contragolpeador
        );
        assertTrue(pesadoVsTatico.isVencedor(lutadorTatico));
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, pesadoVsTatico.getTipoVitoria());
        assertEquals(-7.93, pesadoVsTatico.getSaldoFinal(), 0.01);
    }

    // =========================================================================
    // 3. ATRIBUTOS FORTES SUPERAM ESTILO FAVORAVEL (ATRIBUTO > ESTILO)
    // =========================================================================

    @Test
    @DisplayName("lutador com atributos fortes vence lutador fraco mesmo se o fraco usar estilo favoravel")
    void testAtributosFortesSuperamEstiloFavoravelDoFraco() {
        // lutador fraco com estilo favoravel (velocista com agilidade 4 e forca 3)
        Personagem fraco = new Personagem(4, "Fraco", "Iniciante", Cores.VERDE, Sexo.MASCULINO);
        Atributo attrFraco = new Atributo(100, 3, 4, 3, 4, 100);

        // lutador forte com atributos altos (forca 8 e resistencia 8), mesmo errando o estilo (usando velocista)
        Personagem forte = new Personagem(5, "Forte", "Veterano", Cores.CASTANHO, Sexo.MASCULINO);
        Atributo attrForte = new Atributo(100, 8, 3, 8, 3, 100);

        // caso 1: o forte colocou estilo errado (velocista), mas seus atributos brutos superam o fraco
        ResultadoLuta res1 = fightController.resolverCombate(
                fraco, attrFraco, velocista,
                forte, attrForte, velocista
        );
        assertTrue(res1.isVencedor(forte), "Forte deve vencer pelo peso dos atributos brutos");
        assertEquals(TipoVitoria.DECISAO_UNANIME, res1.getTipoVitoria());
        assertEquals(-13.60, res1.getSaldoFinal(), 0.01);

        // caso 2: forte usando nocauteador contra velocista fraco
        ResultadoLuta res2 = fightController.resolverCombate(
                fraco, attrFraco, velocista,
                forte, attrForte, nocauteador
        );
        assertTrue(res2.isVencedor(forte));
        assertEquals(TipoVitoria.DECISAO_UNANIME, res2.getTipoVitoria());
        assertEquals(-22.70, res2.getSaldoFinal(), 0.01);
    }

    // =========================================================================
    // 4. ESCOLHA INTELIGENTE DE ESTILO VIRA A LUTA EM DISPUTA MODERADA
    // =========================================================================

    @Test
    @DisplayName("estilo inteligente vira o combate para o lutador com atributos menores")
    void testEscolhaInteligenteDeEstiloViraLutaDoUnderdog() {
        // underdog tem atributos menores no geral (total 21: forca 4, agilidade 8, resistencia 4, inteligencia 5)
        Personagem underdog = new Personagem(6, "Underdog", "Azarao", Cores.AZUL, Sexo.MASCULINO);
        Atributo attrUnderdog = new Atributo(100, 4, 8, 4, 5, 100);

        // favorito tem atributos maiores no geral (total 25: forca 8, agilidade 5, resistencia 7, inteligencia 5)
        Personagem favorito = new Personagem(7, "Favorito", "Campeao", Cores.PRETO, Sexo.MASCULINO);
        Atributo attrFavorito = new Atributo(100, 8, 5, 7, 5, 100);

        // cenario A: se o underdog escolher o estilo errado (nocauteador), o favorito esmaga
        ResultadoLuta cenarioA = fightController.resolverCombate(
                underdog, attrUnderdog, nocauteador,
                favorito, attrFavorito, nocauteador
        );
        assertTrue(cenarioA.isVencedor(favorito));
        assertEquals(-16.50, cenarioA.getSaldoFinal(), 0.01);

        // cenario B: o underdog usa o estilo perfeito (velocista) e o favorito usa estilo errado (velocista)
        // a escolha de estilo inteligente vira a luta a favor do underdog!
        ResultadoLuta cenarioB = fightController.resolverCombate(
                underdog, attrUnderdog, velocista,
                favorito, attrFavorito, velocista
        );
        assertTrue(cenarioB.isVencedor(underdog), "Underdog com estilo correto deve virar a luta");
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, cenarioB.getTipoVitoria());
        assertEquals(1.20, cenarioB.getSaldoFinal(), 0.01);
    }

    // =========================================================================
    // 5. LUTADOR NAO ESPECIALISTA / EQUILIBRADO (5 EM TUDO)
    // =========================================================================

    @Test
    @DisplayName("lutador nao especialista com atributos equilibrados testado nos 3 estilos")
    void testLutadorNaoEspecialistaEquilibrado() {
        Personagem equilibrado1 = new Personagem(8, "Neutro 1", "Equilibrado", Cores.CASTANHO, Sexo.MASCULINO);
        Personagem equilibrado2 = new Personagem(9, "Neutro 2", "Equilibrado", Cores.VERDE, Sexo.MASCULINO);
        Atributo attrEquilibrado = new Atributo(100, 5, 5, 5, 5, 100);

        // confronto velocista vs nocauteador em lutador neutro:
        // agilidade conecta mais que forca bruta com atributos medianos
        ResultadoLuta velVsNoc = fightController.resolverCombate(
                equilibrado1, attrEquilibrado, velocista,
                equilibrado2, attrEquilibrado, nocauteador
        );
        assertTrue(velVsNoc.isVencedor(equilibrado1));
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, velVsNoc.getTipoVitoria());
        assertEquals(2.00, velVsNoc.getSaldoFinal(), 0.01);

        // confronto contragolpeador vs nocauteador em lutador neutro:
        ResultadoLuta contraVsNoc = fightController.resolverCombate(
                equilibrado1, attrEquilibrado, contragolpeador,
                equilibrado2, attrEquilibrado, nocauteador
        );
        assertTrue(contraVsNoc.isVencedor(equilibrado1));
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, contraVsNoc.getTipoVitoria());
        assertEquals(2.50, contraVsNoc.getSaldoFinal(), 0.01);

        // confronto contragolpeador vs velocista em lutador neutro:
        // diferenca minima de 0.50 cai na zona morta de empate (-1.0 a 1.0)
        ResultadoLuta contraVsVel = fightController.resolverCombate(
                equilibrado1, attrEquilibrado, contragolpeador,
                equilibrado2, attrEquilibrado, velocista
        );
        assertTrue(contraVsVel.isEmpate(), "Diferenca de 0.50 esta dentro da zona morta e deve empatar");
        assertEquals(TipoVitoria.EMPATE, contraVsVel.getTipoVitoria());
        assertEquals(0.50, contraVsVel.getSaldoFinal(), 0.01);
    }

    // =========================================================================
    // 6. FATOR FISICO: DESGASTE / EXAUSTAO DE SAUDE E ENERGIA
    // =========================================================================

    @Test
    @DisplayName("lutador com atributos fortes porem exausto perde para lutador mediano descansado")
    void testLutadorForteExaustoPerdeParaMedianoDescansado() {
        // lutador com habilidades superiores (forca 6, agilidade 6), mas exausto (saude 20, energia 10)
        Personagem exausto = new Personagem(10, "Exausto", "Veterano sem gas", Cores.PRETO, Sexo.MASCULINO);
        Atributo attrExausto = new Atributo(20, 6, 6, 5, 5, 10);

        // lutador comum com atributos medianos (5 em tudo), mas 100% de saude e energia
        Personagem descansado = new Personagem(11, "Descansado", "Pronto pra lutar", Cores.AZUL, Sexo.MASCULINO);
        Atributo attrDescansado = new Atributo(100, 5, 5, 5, 5, 100);

        ResultadoLuta resultado = fightController.resolverCombate(
                descansado, attrDescansado, velocista,
                exausto, attrExausto, velocista
        );

        assertTrue(resultado.isVencedor(descansado), "Descansado deve vencer o lutador exausto");
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, resultado.getTipoVitoria());
        assertEquals(4.40, resultado.getSaldoFinal(), 0.01);
    }

    // =========================================================================
    // 7. VITORIA POR NOCAUTE (DISPARIDADE BRUTAL)
    // =========================================================================

    @Test
    @DisplayName("vitoria por nocaute contra lutador fragil")
    void testVitoriaPorNocaute() {
        Personagem frango = new Personagem(12, "Frango", "Iniciante Fraco", Cores.AZUL, Sexo.MASCULINO);
        Atributo attrFrango = new Atributo(50, 2, 2, 2, 2, 40);

        ResultadoLuta resultado = fightController.resolverCombate(
                lutadorPesado, attrPesado, nocauteador,
                frango, attrFrango, velocista
        );

        assertTrue(resultado.isVencedor(lutadorPesado));
        assertEquals(TipoVitoria.NOCAUTE, resultado.getTipoVitoria());
        assertTrue(resultado.getSaldoFinal() >= FightController.PONTOS_NOCAUTE);
    }

    // =========================================================================
    // 8. SIMETRIA E INVERSAO DE LADOS
    // =========================================================================

    @Test
    @DisplayName("simetria ao inverter os cantos dos lutadores")
    void testSimetriaInversaoDeLados() {
        ResultadoLuta ida = fightController.resolverCombate(
                lutadorAgil, attrAgil, velocista,
                lutadorPesado, attrPesado, nocauteador
        );

        ResultadoLuta volta = fightController.resolverCombate(
                lutadorPesado, attrPesado, nocauteador,
                lutadorAgil, attrAgil, velocista
        );

        // o vencedor deve ser o mesmo e o saldo deve ser o inverso
        assertEquals(ida.getVencedor(), volta.getVencedor());
        assertEquals(ida.getSaldoFinal(), -volta.getSaldoFinal(), 0.001);
    }

    // =========================================================================
    // 9. ZONA MORTA DE EMPATE (ENTRE -PONTOS_EMPATE E +PONTOS_EMPATE)
    // =========================================================================

    @Test
    @DisplayName("zona morta considera empate diferencas entre -PONTOS_EMPATE e +PONTOS_EMPATE")
    void testZonaMortaEmpate() {
        // lutador base com stats iguais
        Personagem p1 = new Personagem(20, "Lutador A", "Azul", Cores.AZUL, Sexo.MASCULINO);
        Personagem p2 = new Personagem(21, "Lutador B", "Vermelho", Cores.PRETO, Sexo.MASCULINO);

        // caso 1: diferenca de 0.60 pts (energia 100 vs 90) fica dentro da zona morta (<= 1.0)
        Atributo attrBase = new Atributo(100, 5, 5, 5, 5, 100);
        Atributo attrPequenaDiferenca = new Atributo(100, 5, 5, 5, 5, 90);

        ResultadoLuta resZonaMorta = fightController.resolverCombate(
                p1, attrBase, velocista,
                p2, attrPequenaDiferenca, velocista
        );
        assertEquals(0.60, resZonaMorta.getSaldoFinal(), 0.01);
        assertTrue(resZonaMorta.isEmpate(), "0.60 esta dentro da margem de 1.0 e deve empatar");
        assertEquals(TipoVitoria.EMPATE, resZonaMorta.getTipoVitoria());
        assertNull(resZonaMorta.getVencedor());
        assertNull(resZonaMorta.getPerdedor());

        // caso 2: inverter lutadores (-0.60 pts) tambem fica dentro da zona morta (>= -1.0)
        ResultadoLuta resZonaMortaInvertida = fightController.resolverCombate(
                p2, attrPequenaDiferenca, velocista,
                p1, attrBase, velocista
        );
        assertEquals(-0.60, resZonaMortaInvertida.getSaldoFinal(), 0.01);
        assertTrue(resZonaMortaInvertida.isEmpate(), "-0.60 esta dentro da margem de -1.0 e deve empatar");
        assertEquals(TipoVitoria.EMPATE, resZonaMortaInvertida.getTipoVitoria());

        // caso 3: diferenca de 1.80 pts (energia 100 vs 70) ultrapassa a margem (> 1.0)
        Atributo attrSuperouMargem = new Atributo(100, 5, 5, 5, 5, 70);
        ResultadoLuta resVitoria = fightController.resolverCombate(
                p1, attrBase, velocista,
                p2, attrSuperouMargem, velocista
        );
        assertEquals(1.80, resVitoria.getSaldoFinal(), 0.01);
        assertFalse(resVitoria.isEmpate(), "1.80 supera a margem de 1.0 e deve declarar vencedor");
        assertTrue(resVitoria.isVencedor(p1));
        assertEquals(TipoVitoria.DECISAO_DIVIDIDA, resVitoria.getTipoVitoria());
    }

    // =========================================================================
    // 10. TESTES UNITARIOS DE MODIFICADORES E REGRAS
    // =========================================================================

    @Test
    @DisplayName("calculo do percentual de bonus por faixa")
    void testEscalonamentoBonusFaixasClasseMae() {
        // escala de 0 a 10
        assertEquals(0.15, velocista.calcularPercentualBonus(1, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.15, nocauteador.calcularPercentualBonus(2, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.30, contragolpeador.calcularPercentualBonus(3, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.30, velocista.calcularPercentualBonus(5, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.45, nocauteador.calcularPercentualBonus(7, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.45, contragolpeador.calcularPercentualBonus(8, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.60, velocista.calcularPercentualBonus(8.5, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.75, velocista.calcularPercentualBonus(9, Atributo.MAX_HABILIDADE), 0.001);
        assertEquals(0.75, nocauteador.calcularPercentualBonus(10, Atributo.MAX_HABILIDADE), 0.001);

        // escala de 0 a 100 (vida e energia)
        assertEquals(0.15, velocista.calcularPercentualBonus(20, Atributo.MAX_SAUDE), 0.001);
        assertEquals(0.30, velocista.calcularPercentualBonus(50, Atributo.MAX_SAUDE), 0.001);
        assertEquals(0.45, velocista.calcularPercentualBonus(75, Atributo.MAX_SAUDE), 0.001);
        assertEquals(0.60, velocista.calcularPercentualBonus(85, Atributo.MAX_SAUDE), 0.001);
        assertEquals(0.75, velocista.calcularPercentualBonus(95, Atributo.MAX_SAUDE), 0.001);
    }

    @Test
    @DisplayName("bonus e penalidades do velocista")
    void testModificadoresSubclasseVelocista() {
        Atributo base = new Atributo(100, 10, 8, 5, 5, 100);
        var efetivos = velocista.aplicarModificadores(base);

        assertEquals(11.6, efetivos.agilidade, 0.01);
        assertEquals(8.0, efetivos.forca, 0.01);
        assertEquals(120.0, efetivos.energia, 0.01);
    }

    @Test
    @DisplayName("bonus e penalidades do nocauteador")
    void testModificadoresSubclasseNocauteador() {
        Atributo base = new Atributo(100, 8, 10, 5, 5, 100);
        var efetivos = nocauteador.aplicarModificadores(base);

        assertEquals(11.6, efetivos.forca, 0.01);
        assertEquals(8.0, efetivos.agilidade, 0.01);
        assertEquals(80.0, efetivos.energia, 0.01);
    }

    @Test
    @DisplayName("bonus e penalidades do contragolpeador")
    void testModificadoresSubclasseContragolpeador() {
        Atributo base = new Atributo(100, 6, 10, 8, 6, 100);
        var efetivos = contragolpeador.aplicarModificadores(base);

        assertEquals(11.6, efetivos.resistencia, 0.01);
        assertEquals(8.5, efetivos.agilidade, 0.01);
    }

    @Test
    @DisplayName("trava de valor minimo e maximo nos atributos")
    void testTravasMinimoEMaximoAtributo() {
        Atributo attr = new Atributo();

        attr.setSaude(-50);
        attr.setForca(-5);
        attr.setEnergia(-10);
        assertEquals(Atributo.MIN_VALOR, attr.getSaude());
        assertEquals(Atributo.MIN_VALOR, attr.getForca());
        assertEquals(Atributo.MIN_VALOR, attr.getEnergia());

        attr.setSaude(999);
        attr.setForca(999);
        attr.setEnergia(999);
        assertEquals(Atributo.MAX_SAUDE, attr.getSaude());
        assertEquals(Atributo.MAX_HABILIDADE, attr.getForca());
        assertEquals(Atributo.MAX_ENERGIA, attr.getEnergia());
    }
}
