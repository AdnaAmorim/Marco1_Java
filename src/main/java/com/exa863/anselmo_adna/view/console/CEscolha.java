package com.exa863.anselmo_adna.view.console;

/**
 * Representa uma opção de menu com um rótulo e um índice.
 * Usada em conjunto com CMultiplaEscolha para montar menus interativos.
 *
 * @author Anselmo e Adna
 */
public class CEscolha {

    public String titulo;
    public int index;

    public CEscolha(String titulo, int index) {
        this.titulo = titulo;
        this.index = index;
    }
}
