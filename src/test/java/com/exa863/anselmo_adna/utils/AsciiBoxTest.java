package com.exa863.anselmo_adna.utils;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsciiBoxTest {

    @Test
    @DisplayName("Deve renderizar uma caixa com cantos e bordas nas dimensões exatas")
    void testRenderComCantosEBordas() {
        String[] conteudo = new String[]{
                "Linha 1",
                "Linha 2 com mais texto",
                ""
        };

        String render = new AsciiBox()
                .size(50)
                .borders("━", "┃")
                .corners("┏", "┓", "┗", "┛")
                .render(conteudo);

        assertNotNull(render);
        String[] linhas = render.replaceFirst("^\n", "").split("\n");

        // 1 linha de topo + 3 de conteúdo + 1 de fundo = 5 linhas
        assertEquals(5, linhas.length);

        // Topo
        assertTrue(linhas[0].startsWith("┏"));
        assertTrue(linhas[0].endsWith("┓"));
        assertEquals(50, linhas[0].length());

        // Conteúdo
        for (int i = 1; i <= 3; i++) {
            assertTrue(linhas[i].startsWith("┃"));
            assertTrue(linhas[i].endsWith("┃"));
            assertEquals(50, linhas[i].length());
        }

        // Fundo
        assertTrue(linhas[4].startsWith("┗"));
        assertTrue(linhas[4].endsWith("┛"));
        assertEquals(50, linhas[4].length());
    }

    @Test
    @DisplayName("Deve calcular corretamente o tamanho mesmo com cores ANSI do JLine")
    void testRenderComCoresJLine() {
        String textoColorido = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                .append("TESTE COLORIDO")
                .toAnsi();

        String[] conteudo = new String[]{ textoColorido };

        String render = new AsciiBox()
                .size(40)
                .borders("━", "┃")
                .corners("┏", "┓", "┗", "┛")
                .render(conteudo);

        assertNotNull(render);
        String[] linhas = render.replaceFirst("^\n", "").split("\n");

        assertEquals(3, linhas.length);
        // O comprimento visível da linha com cor deve ser 40
        assertEquals(40, org.jline.utils.AttributedString.fromAnsi(linhas[1]).length());
    }
}
