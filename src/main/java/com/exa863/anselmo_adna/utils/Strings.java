package com.exa863.anselmo_adna.utils;

/**
 * Utilitário de manipulação de strings.
 *
 * @author Anselmo e Adna
 */
public class Strings {

    public static String repeat(String string, int count) {
        if (string == null || count <= 0) {
            return "";
        }
        return string.repeat(count);
    }
}