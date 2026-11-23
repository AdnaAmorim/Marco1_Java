package com.exa863.anselmo_adna.view.fx;

import com.exa863.anselmo_adna.model.stats.Item;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolve e faz cache dos ícones pixel art dos itens do inventário e da loja.
 *
 * @author Anselmo e Adna
 */
public final class ItemImagem {

    /** Pasta. */
    private static final String PASTA = "/images/itens/";
    /** Cache. */
    private static final Map<String, Image> CACHE = new HashMap<>();

    /** Impede a criação de instâncias desta classe utilitária. */
    private ItemImagem() {
    }

    /** Busca a ilustração pelo nome do item. Retorna null quando não há imagem disponível. */
    public static Image obter(Item item) {
        if (item == null || item.getNome() == null) {
            return null;
        }
        String arquivo = switch (item.getNome().toLowerCase()) {
            case "salada" -> "Salada.png";
            case "bife" -> "Bife.png";
            case "refrigerante" -> "Refrigerante.png";
            case "barra de cereal" -> "Barra_De_Cereal.png";
            case "café expresso", "cafe expresso" -> "Cafe_Expresso.png";
            case "energético", "energetico" -> "Energetico.png";
            case "remédio", "remedio" -> "Remedio.png";
            case "vitaminas" -> "Vitaminas.png";
            case "contrato" -> "Contrato.png";
            case "luva do pai" -> "Luva_De_Boxe.png";
            default -> null;
        };
        if (arquivo == null) {
            return null;
        }
        String caminho = PASTA + arquivo;
        return CACHE.computeIfAbsent(caminho, key -> {
            URL recurso = ItemImagem.class.getResource(key);
            return recurso != null ? new Image(recurso.toExternalForm()) : null;
        });
    }

    /** Formata efeitos. */
    public static String formatarEfeitos(Item item) {
        if (item == null || !item.isConsumivel()) {
            return "ITEM ESPECIAL";
        }
        StringBuilder sb = new StringBuilder();
        if (item.getCura() > 0) {
            sb.append("+").append(item.getCura()).append(" SAÚDE");
        }
        if (item.getEnergia() > 0) {
            if (!sb.isEmpty()) {
                sb.append("  •  ");
            }
            sb.append("+").append(item.getEnergia()).append(" ENERGIA");
        }
        return sb.toString();
    }
}
