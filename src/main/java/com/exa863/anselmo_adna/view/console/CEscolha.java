package com.exa863.anselmo_adna.view.console;

/**
 * Representa uma opção de menu com um rótulo e um índice.
 * Usada em conjunto com CMultiplaEscolha para montar menus interativos.
 *
 * @author Anselmo e Adna
 */
public class CEscolha {

    /** Índice reservado para uma linha em branco não selecionável entre as opções. */
    public static final int SEPARADOR = Integer.MIN_VALUE;

    public String titulo;
    public int index;

    public CEscolha(String titulo, int index) {
        this.titulo = titulo;
        this.index = index;
    }

    public static CEscolha separador() {
        return new CEscolha("", SEPARADOR);
    }

    public boolean isSeparador() {
        return index == SEPARADOR;
    }
}
