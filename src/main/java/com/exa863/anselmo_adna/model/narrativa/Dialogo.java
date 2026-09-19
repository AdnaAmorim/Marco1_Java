package com.exa863.anselmo_adna.model.narrativa;

import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;

import java.util.List;
import java.util.function.Consumer;

public class Dialogo {

    private final Personagem personagem;
    private final TipoEmissor tipoEmissor;
    private final String texto;
    private final List<Opcao> opcoes;

    // Construtor principal para fala de um personagem
    public Dialogo(Personagem personagem, String texto) {
        this.personagem = personagem;
        this.tipoEmissor = TipoEmissor.PERSONAGEM;
        this.texto = texto;
        this.opcoes = List.of();
    }

    // Construtor para emissores com tipo específico (ex: Narrador ou Sistema)
    public Dialogo(TipoEmissor tipoEmissor, String texto) {
        this.personagem = null;
        this.tipoEmissor = tipoEmissor != null ? tipoEmissor : TipoEmissor.NARRADOR;
        this.texto = texto;
        this.opcoes = List.of();
    }

    // Construtor para pontos de escolha interativa
    public Dialogo(String pergunta, List<Opcao> opcoes) {
        this.personagem = null;
        this.tipoEmissor = TipoEmissor.SISTEMA;
        this.texto = pergunta;
        this.opcoes = opcoes != null ? opcoes : List.of();
    }

    public static Dialogo escolha(String pergunta, List<Opcao> opcoes) {
        return new Dialogo(pergunta, opcoes);
    }

    public static Opcao opcao(String texto, Consumer<Player> acao, List<? extends Dialogo> desfecho) {
        return new Opcao(texto, acao, desfecho);
    }

    public boolean isEscolha() {
        return opcoes != null && !opcoes.isEmpty();
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public TipoEmissor getTipoEmissor() {
        return tipoEmissor;
    }

    public String getTexto() {
        return texto;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public String getNomeEmissor() {
        if (tipoEmissor == TipoEmissor.PERSONAGEM && personagem != null) {
            return personagem.getNome();
        }
        if (tipoEmissor == TipoEmissor.SISTEMA) {
            return "Sistema";
        }
        return "Narrador";
    }

    // Opção de escolha interna ao Diálogo, reutilizável para qualquer personagem
    public static class Opcao {
        private final String texto;
        private final Consumer<Player> acao;
        private final List<? extends Dialogo> desfecho;

        private Opcao(String texto, Consumer<Player> acao, List<? extends Dialogo> desfecho) {
            this.texto = texto;
            this.acao = acao;
            this.desfecho = desfecho != null ? desfecho : List.of();
        }

        public String getTexto() {
            return texto;
        }

        public Consumer<Player> getAcao() {
            return acao;
        }

        public List<? extends Dialogo> getDesfecho() {
            return desfecho;
        }

        public void executarAcao(Player player) {
            if (acao != null && player != null) {
                acao.accept(player);
            }
        }
    }
}
