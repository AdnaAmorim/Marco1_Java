package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.view.fx.Icone;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Carrega e recolora os ícones SVG pixel art para usar nos botões e labels do JavaFX.
 *
 * @author Anselmo e Adna
 */
public final class IconManager {

    /** Cor texto. */
    public static final Color COR_TEXTO = Color.web("#FAF0DC");
    /** Cor destaque. */
    public static final Color COR_DESTAQUE = Color.web("#FFD166");
    /** Cor perigo. */
    public static final Color COR_PERIGO = Color.web("#E63946");

    /** Svg size. */
    private static final Pattern SVG_SIZE = Pattern.compile("width=\"(\\d+)\"\\s+height=\"(\\d+)\"");
    /** Rect pattern. */
    private static final Pattern RECT_PATTERN = Pattern.compile(
            "<rect\\s+x=\"(\\d+)\"\\s+y=\"(\\d+)\"\\s+width=\"(\\d+)\"\\s+height=\"(\\d+)\"\\s+fill=\"(#[0-9A-Fa-f]{6})\"");

    /** Retângulo de pixels usado para desenhar o ícone. */
    private record PixelRect(int x, int y, int w, int h, boolean sombreado) {
    }

    /** Dimensões e retângulos lidos do ícone SVG. */
    private record SvgData(int largura, int altura, List<PixelRect> rects) {
    }

    /** Cache svg. */
    private static final Map<Icone, SvgData> CACHE_SVG = new EnumMap<>(Icone.class);
    /** Cache imagem. */
    private static final Map<String, Image> CACHE_IMG = new HashMap<>();

    /** Impede a criação de instâncias desta classe utilitária. */
    private IconManager() {
    }

    /** Cria a representação visual do ícone. */
    public static ImageView criar(Icone icone, double tamanho, Color cor) {
        ImageView view = new ImageView(renderizar(icone, cor));
        view.setFitWidth(tamanho);
        view.setFitHeight(tamanho);
        view.setPreserveRatio(true);
        view.setSmooth(false);
        return view;
    }

    /** Associa o ícone ao componente. */
    public static void aplicar(Labeled controle, Icone icone, double tamanho, Color cor) {
        if (controle == null || icone == null) {
            return;
        }
        controle.setGraphic(criar(icone, tamanho, cor));
        controle.setGraphicTextGap(8);
    }

    /** Aplica ícone do botão. */
    public static void aplicarBotao(ButtonBase botao, Icone icone, double tamanho) {
        aplicarBotao(botao, icone, tamanho, COR_TEXTO, COR_DESTAQUE);
    }

    /** Aplica ícone do botão de ação destrutiva. */
    public static void aplicarBotaoPerigo(ButtonBase botao, Icone icone, double tamanho) {
        aplicarBotao(botao, icone, tamanho, COR_TEXTO, COR_PERIGO);
    }

    /** Aplica ícone do botão. */
    public static void aplicarBotao(ButtonBase botao, Icone icone, double tamanho, Color corNormal, Color corHover) {
        if (botao == null || icone == null) {
            return;
        }
        Image imgNormal = renderizar(icone, corNormal);
        Image imgHover = renderizar(icone, corHover);
        ImageView view = new ImageView(imgNormal);
        view.setFitWidth(tamanho);
        view.setFitHeight(tamanho);
        view.setPreserveRatio(true);
        view.setSmooth(false);

        botao.hoverProperty().addListener((obs, oldVal, hover) ->
                view.setImage(hover && !botao.isDisabled() ? imgHover : imgNormal));

        botao.setGraphic(view);
        botao.setGraphicTextGap(10);
    }

    /** Pinta os retângulos do SVG e guarda a imagem para reutilizá-la em ícones da mesma cor. */
    private static Image renderizar(Icone icone, Color cor) {
        String chave = icone.name() + ":" + cor.toString();
        return CACHE_IMG.computeIfAbsent(chave, k -> {
            SvgData svg = carregarSvg(icone);
            if (svg == null || svg.largura <= 0 || svg.altura <= 0) {
                return new WritableImage(1, 1);
            }
            WritableImage img = new WritableImage(svg.largura, svg.altura);
            PixelWriter writer = img.getPixelWriter();
            Color corSombra = cor.deriveColor(0, 1.0, 0.78, 1.0);

            for (PixelRect r : svg.rects) {
                Color pixel = r.sombreado ? corSombra : cor;
                for (int px = r.x; px < r.x + r.w && px < svg.largura; px++) {
                    for (int py = r.y; py < r.y + r.h && py < svg.altura; py++) {
                        writer.setColor(px, py, pixel);
                    }
                }
            }
            return img;
        });
    }

    /** Carrega descrição dos retângulos do ícone SVG. */
    private static SvgData carregarSvg(Icone icone) {
        return CACHE_SVG.computeIfAbsent(icone, i -> {
            try (InputStream in = IconManager.class.getResourceAsStream(i.getCaminho())) {
                if (in == null) {
                    return null;
                }
                String conteudo = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                Matcher sizeMatcher = SVG_SIZE.matcher(conteudo);
                if (!sizeMatcher.find()) {
                    return null;
                }
                int largura = Integer.parseInt(sizeMatcher.group(1));
                int altura = Integer.parseInt(sizeMatcher.group(2));

                List<PixelRect> rects = new ArrayList<>();
                Matcher rectMatcher = RECT_PATTERN.matcher(conteudo);
                while (rectMatcher.find()) {
                    int x = Integer.parseInt(rectMatcher.group(1));
                    int y = Integer.parseInt(rectMatcher.group(2));
                    int w = Integer.parseInt(rectMatcher.group(3));
                    int h = Integer.parseInt(rectMatcher.group(4));
                    boolean sombreado = !"#FFFFFF".equalsIgnoreCase(rectMatcher.group(5));
                    rects.add(new PixelRect(x, y, w, h, sombreado));
                }
                return new SvgData(largura, altura, rects);
            } catch (IOException e) {
                return null;
            }
        });
    }
}
