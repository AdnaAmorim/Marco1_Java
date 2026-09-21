package com.exa863.anselmo_adna.model.character;

import com.exa863.anselmo_adna.model.stats.Atributo;
import com.exa863.anselmo_adna.model.stats.Inventario;
import com.exa863.anselmo_adna.model.stats.Relacionamentos;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa o jogador principal.
 * Guarda atributos, inventário, relacionamentos, limites de treino por dia e capítulos concluídos.
 *
 * @author Anselmo e Adna
 */
public class Player extends Personagem {

    private int dinheiro;
    private Atributo atributos;
    private Relacionamentos[] relacionamentos;
    private Inventario inventario;
    private final Set<String> capitulosConcluidos;

    public static final int MAX_TREINOS_POR_DIA = 5;
    private int diaUltimoTreino;
    private int treinosRealizadosHoje;

    private int ultimoDiaLutaBoxe;

    private int lutasLocais;
    private int lutasEstaduais;
    private int lutasNacionais;

    // Só dá para namorar um personagem por jogo
    private String personagemRomance;

    public Player(int id, String nome, String descricao, Cores corOlhos, Sexo sexo) {
        super(id, nome, descricao, corOlhos, sexo);
        this.dinheiro = 100;
        this.atributos = new Atributo();
        this.relacionamentos = new Relacionamentos[10];
        this.inventario = new Inventario();
        this.capitulosConcluidos = new HashSet<>();

        this.diaUltimoTreino = 0;
        this.treinosRealizadosHoje = 0;
        this.ultimoDiaLutaBoxe = -1;

        this.lutasLocais = 0;
        this.lutasEstaduais = 0;
        this.lutasNacionais = 0;

        this.personagemRomance = null;
    }

    // --- Lutas e Campeonatos ---
    public int getLutasLocais() {
        return lutasLocais;
    }

    public void registrarLutaLocal() {
        this.lutasLocais++;
    }

    public int getLutasEstaduais() {
        return lutasEstaduais;
    }

    public int getLutasNacionais() {
        return lutasNacionais;
    }

    public void registrarLutaEstadual() {
        this.lutasEstaduais++;
    }

    public void registrarLutaNacional() {
        this.lutasNacionais++;
    }

    // --- Romance e Afinidade ---

    /**
     * Confere se o jogador pode namorar com esse personagem (ou se já está namorando ele).
     *
     * @param nomePersonagem Nome do personagem.
     * @return true se ainda não estiver namorando ninguém ou se for a mesma pessoa.
     */
    public boolean podeRomancearCom(String nomePersonagem) {
        return personagemRomance == null || personagemRomance.equalsIgnoreCase(nomePersonagem);
    }

    /**
     * Define o par romântico do jogador.
     *
     * @param nomePersonagem Nome do personagem escolhido.
     */
    public void definirRomance(String nomePersonagem) {
        if (nomePersonagem != null && personagemRomance == null) {
            personagemRomance = nomePersonagem;
        }
    }

    public boolean possuiRomance() {
        return personagemRomance != null;
    }

    public String getPersonagemRomance() {
        return personagemRomance;
    }

    // --- Treinos Diários ---

    public int getTreinosRestantes(int diaAtual) {
        if (this.diaUltimoTreino != diaAtual) {
            return MAX_TREINOS_POR_DIA;
        }
        return MAX_TREINOS_POR_DIA - this.treinosRealizadosHoje;
    }

    /**
     * Confere se o jogador ainda pode treinar no dia de hoje.
     *
     * @param diaAtual Dia atual no jogo.
     * @return true se ainda não atingiu o limite de treinos do dia.
     */
    public boolean podeTreinarHoje(int diaAtual) {
        return getTreinosRestantes(diaAtual) > 0;
    }

    public void registrarTreino(int diaAtual) {
        if (this.diaUltimoTreino != diaAtual) {
            this.diaUltimoTreino = diaAtual;
            this.treinosRealizadosHoje = 1;
        } else {
            this.treinosRealizadosHoje++;
        }
    }

    public int getUltimoDiaLutaBoxe() {
        return ultimoDiaLutaBoxe;
    }

    public void setUltimoDiaLutaBoxe(int ultimoDiaLutaBoxe) {
        this.ultimoDiaLutaBoxe = ultimoDiaLutaBoxe;
    }

    /**
     * Confere se o jogador já pode lutar boxe de novo (precisa de pelo menos 1 dia de intervalo).
     *
     * @param diaAtual Dia atual no jogo.
     * @return true se puder lutar hoje.
     */
    public boolean podeLutarBoxe(int diaAtual) {
        if (this.ultimoDiaLutaBoxe == -1) {
            return true;
        }
        return (diaAtual - this.ultimoDiaLutaBoxe) >= 1;
    }

    public int getProximoDiaLutaBoxe() {
        if (this.ultimoDiaLutaBoxe == -1) {
            return 1;
        }
        return this.ultimoDiaLutaBoxe + 1;
    }

    public void registrarLutaBoxe(int diaAtual) {
        this.ultimoDiaLutaBoxe = diaAtual;
    }

    public int getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(int dinheiro) {
        this.dinheiro = dinheiro;
    }

    public Atributo getAtributos() {
        return atributos;
    }

    public void ativarModoDev() {
        this.dinheiro = 20000;
        this.atributos.setSaude(Atributo.MAX_SAUDE);
        this.atributos.setEnergia(Atributo.MAX_ENERGIA);
        this.atributos.setForca(Atributo.MAX_HABILIDADE);
        this.atributos.setAgilidade(Atributo.MAX_HABILIDADE);
        this.atributos.setResistencia(Atributo.MAX_HABILIDADE);
        this.atributos.setInteligencia(Atributo.MAX_HABILIDADE);
    }

    public Relacionamentos[] getRelacionamentos() {
        return relacionamentos;
    }

    public void alterarAfinidade(Personagem personagem, int delta) {
        if (personagem == null) {
            return;
        }

        Relacionamentos relacionamento = buscarRelacionamento(personagem);
        if (relacionamento != null) {
            relacionamento.setNivelAmizade(relacionamento.getNivelAmizade() + delta);
            return;
        }

        for (int i = 0; i < relacionamentos.length; i++) {
            if (relacionamentos[i] == null) {
                relacionamentos[i] = new Relacionamentos(personagem, delta);
                return;
            }
        }
    }

    public int getAfinidade(Personagem personagem) {
        Relacionamentos relacionamento = buscarRelacionamento(personagem);
        return relacionamento != null ? relacionamento.getNivelAmizade() : 0;
    }

    private Relacionamentos buscarRelacionamento(Personagem personagem) {
        if (personagem == null) {
            return null;
        }
        for (Relacionamentos relacionamento : relacionamentos) {
            if (relacionamento != null && relacionamento.getPersonagem().getId() == personagem.getId()) {
                return relacionamento;
            }
        }
        return null;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void concluirCapitulo(String idCapitulo) {
        if (idCapitulo != null) {
            capitulosConcluidos.add(idCapitulo);
        }
    }

    public boolean isCapituloConcluido(String idCapitulo) {
        return capitulosConcluidos.contains(idCapitulo);
    }

    public Set<String> getCapitulosConcluidos() {
        return capitulosConcluidos;
    }
}