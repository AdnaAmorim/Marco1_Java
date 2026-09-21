package com.exa863.anselmo_adna.model.stats;

import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da mochila de itens do jogador.
 * Verifica adição, remoção, contagem e uso de consumíveis para recuperar atributos.
 *
 * @author Anselmo e Adna
 */
public class InventarioTest {

    private Inventario inventario;
    private Player player;

    @BeforeEach
    public void setUp() {
        inventario = new Inventario();
        player = new Player(1, "Anselmo", "Protagonista", Cores.CASTANHO, Sexo.MASCULINO);
    }

    @Test
    public void testAdicionarItemSimples() {
        Item salada = new Item("Salada", "Uma salada fresca.", TipoItem.COMIDA, 15, 5, 0);

        inventario.adicionarItem(salada);

        assertTrue(inventario.temItem(salada));
        assertEquals(1, inventario.getQuantidade(salada));
    }

    @Test
    public void testAdicionarItemComQuantidade() {
        Item energetico = new Item("Energético", "Dá energia.", TipoItem.COMIDA, 20, 0, 40);

        inventario.adicionarItem(energetico, 3);
        assertEquals(3, inventario.getQuantidade(energetico));

        inventario.adicionarItem(energetico, 2);
        assertEquals(5, inventario.getQuantidade(energetico));
    }

    @Test
    public void testRemoverItemDiminuiQuantidadeERemove() {
        Item bife = new Item("Bife", "Bife suculento.", TipoItem.COMIDA, 30, 15, 0);

        inventario.adicionarItem(bife, 2);

        // Remove 1 unidade
        boolean removeuPrimeiro = inventario.removerItem(bife);
        assertTrue(removeuPrimeiro);
        assertEquals(1, inventario.getQuantidade(bife));
        assertTrue(inventario.temItem(bife));

        // Remove a última unidade
        boolean removeuSegundo = inventario.removerItem(bife);
        assertTrue(removeuSegundo);
        assertEquals(0, inventario.getQuantidade(bife));
        assertFalse(inventario.temItem(bife));

        // Tentar remover item que já acabou deve retornar false
        boolean removeuSemTer = inventario.removerItem(bife);
        assertFalse(removeuSemTer);
    }

    @Test
    public void testUsarItemRecuperaVidaEEnergia() {
        // Deixa a vida e energia abaixo do máximo para poder curar
        player.getAtributos().setSaude(50);
        player.getAtributos().setEnergia(40);

        Item lanche = new Item("Barra de Cereal", "Cura vida e energia.", TipoItem.COMIDA, 12, 10, 20);
        inventario.adicionarItem(lanche);

        boolean usou = inventario.usarItem(lanche, player);
        assertTrue(usou);

        // Saúde: 50 + 10 = 60 | Energia: 40 + 20 = 60
        assertEquals(60, player.getAtributos().getSaude());
        assertEquals(60, player.getAtributos().getEnergia());

        // Item foi consumido da mochila
        assertFalse(inventario.temItem(lanche));
    }

    @Test
    public void testNaoConsegueUsarItemQueNaoTem() {
        Item remedio = new Item("Remédio", "Cura rápida.", TipoItem.SUPLEMENTO, 50, 25, 0);

        boolean usou = inventario.usarItem(remedio, player);
        assertFalse(usou);
    }
}
