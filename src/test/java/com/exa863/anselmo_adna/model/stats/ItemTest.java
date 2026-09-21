package com.exa863.anselmo_adna.model.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para itens da mochila e da loja.
 * Verifica propriedades, igualdade e se o item é consumível.
 *
 * @author Anselmo e Adna
 */
public class ItemTest {

    private Item comida;
    private Item energetico;
    private Item especial;

    @BeforeEach
    public void setUp() {
        comida = new Item("Salada", "Cura um pouco.", TipoItem.COMIDA, 15, 5, 0);
        energetico = new Item("Café", "Dá energia.", TipoItem.COMIDA, 8, 0, 20);
        especial = new Item("Contrato", "Item de missão.", TipoItem.ESPECIAL, 0, 0, 0);
    }

    @Test
    public void testItemConsumivelQuandoTemCuraOuEnergia() {
        assertTrue(comida.isConsumivel());
        assertTrue(energetico.isConsumivel());
        assertFalse(especial.isConsumivel());
    }

    @Test
    public void testIgualdadePorNome() {
        Item outroItemMesmoNome = new Item("Salada", "Outra descrição", TipoItem.COMIDA, 50, 20, 5);

        assertEquals(comida, outroItemMesmoNome);
        assertEquals(comida.hashCode(), outroItemMesmoNome.hashCode());
        assertNotEquals(comida, especial);
    }
}
