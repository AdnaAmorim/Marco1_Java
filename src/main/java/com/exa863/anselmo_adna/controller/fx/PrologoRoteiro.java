package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;

import java.util.List;
import java.util.Objects;

/** Transcrição dos seis quadros da HistoriaInicialView (modo terminal) para JavaFX. */
public final class PrologoRoteiro {
    /** Impede a criação de instâncias desta classe utilitária. */
    private PrologoRoteiro() { }

    /** Título e texto de uma etapa do prólogo. */
    public record Quadro(String titulo, String texto) { }

    /** Monta os quadros em ordem de exibição, usando o nome e o pronome do protagonista. */
    public static List<Quadro> criar(Player player) {
        Objects.requireNonNull(player, "O protagonista deve existir antes do prólogo.");
        String nome = player.getNome();
        String pronome = player.getSexo() == Sexo.FEMININO ? "Ela" : "Ele";
        return List.of(
                new Quadro("ANOS ATRÁS",
                        "Tudo começou quando " + nome + " ainda era apenas uma criança.\n" +
                        "Naquela época, seus pais eram as pessoas mais importantes de sua vida."),
                new Quadro("O SONHO DO PAI",
                        "Seu pai tinha um grande sonho.\n" +
                        "Ele queria se tornar um dos maiores lutadores de boxe de sua geração. " +
                        "Todos os dias ele treinava, sempre dando o seu melhor.\n" +
                        "Mas a vida tinha outros planos..."),
                new Quadro("O ACIDENTE",
                        "Certa noite, enquanto voltavam para casa, um acidente mudou tudo.\n" +
                        "Os pais de " + nome + " não sobreviveram.\n" +
                        "A vida daquela criança nunca mais seria a mesma."),
                new Quadro("UMA NOVA CASA",
                        nome + " passou a morar com os avós maternos.\n" +
                        "Os avós fizeram tudo o que podiam para dar uma vida boa à criança.\n" +
                        "O tempo passou."),
                new Quadro("18 ANOS DEPOIS",
                        "Agora, aos 18 anos, " + nome + " já não é mais uma criança.\n" +
                        "Mas nunca esqueceu o sonho do pai.\n" +
                        pronome + " decidiu seguir o mesmo caminho."),
                new Quadro("O INÍCIO DO SONHO",
                        "Seu objetivo é simples: tornar-se um grande lutador de boxe.\n" +
                        "Mas para chegar ao topo, você terá que treinar, fazer escolhas " +
                        "e enfrentar muitos desafios.\n" +
                        "O sonho do seu pai agora também é o seu.")
        );
    }
}
