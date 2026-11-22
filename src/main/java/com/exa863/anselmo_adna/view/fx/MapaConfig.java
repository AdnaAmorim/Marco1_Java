package com.exa863.anselmo_adna.view.fx;

import java.util.List;

/**
 * Define os dados visuais e os pontos clicáveis de cada mapa/cidade.
 * Para adicionar novas cidades, basta registrar uma nova constante aqui.
 *
 * @author Anselmo e Adna
 */
public enum MapaConfig {

    CIDADE_NATAL(
            "cidade_natal",
            "/images/cidades/Cidade_Natal_Amanhecer.png",
            "/images/cidades/Cidade_Natal_Entardecer.png",
            "/images/cidades/Cidade_Natal_Noite.png",
            List.of(
                    new Marcador("academia", "ACADEMIA", Icone.STAR, 106, 112, false),
                    new Marcador("academia_boxe", "ACADEMIA DE BOXE", Icone.GAME_CONTROLLER, 368, 165, false),
                    new Marcador("clube_luta", "CLUBE DE LUTA", Icone.EXCLAMATION_MARK, 720, 142, false),
                    new Marcador("casa", "CASA", Icone.HOME, 88, 325, false),
                    new Marcador("rodoviaria", "RODOVIÁRIA", Icone.LOCATION, 664, 345, true)
            ),
            List.of("cidade_a", "cidade_b")
    ),

    CIDADE_A(
            "cidade_a",
            "/images/cidades/Cidade_A_Amanhecer.png",
            "/images/cidades/Cidade_A_Entardecer.png",
            "/images/cidades/Cidade_A_Noite.png",
            List.of(
                    new Marcador("academia_profissional", "ACADEMIA PROFISSIONAL", Icone.STAR, 605, 104, false),
                    new Marcador("loja", "LOJA DE TUDO", Icone.CART, 212, 236, false),
                    new Marcador("rodoviaria", "RODOVIÁRIA", Icone.LOCATION, 595, 332, true)
            ),
            List.of("cidade_natal", "cidade_b")
    ),

    CIDADE_B(
            "cidade_b",
            "/images/cidades/Cidade_B_Amanhecer.png",
            "/images/cidades/Cidade_B_Entardecer.png",
            "/images/cidades/Cidade_B_Noite.png",
            List.of(
                    new Marcador("campeonato_mundial", "CAMPEONATO MUNDIAL", Icone.STAR, 615, 110, false),
                    new Marcador("abrir_academia", "ACADEMIA À VENDA", Icone.HOME, 165, 242, false),
                    new Marcador("rodoviaria", "RODOVIÁRIA", Icone.LOCATION, 578, 342, true)
            ),
            List.of("cidade_natal", "cidade_a")
    );

    public record Marcador(String localId, String rotulo, Icone icone, double x, double y, boolean viagem) {
    }

    private final String localId;
    private final String imagemManha;
    private final String imagemTarde;
    private final String imagemNoite;
    private final List<Marcador> marcadores;
    private final List<String> destinosViagem;

    MapaConfig(String localId, String imagemManha, String imagemTarde, String imagemNoite,
               List<Marcador> marcadores, List<String> destinosViagem) {
        this.localId = localId;
        this.imagemManha = imagemManha;
        this.imagemTarde = imagemTarde;
        this.imagemNoite = imagemNoite;
        this.marcadores = marcadores;
        this.destinosViagem = destinosViagem;
    }

    public String getLocalId() {
        return localId;
    }

    public String getImagemPorHora(int hora) {
        if (hora >= 5 && hora < 12) {
            return imagemManha;
        }
        if (hora >= 12 && hora < 18) {
            return imagemTarde;
        }
        return imagemNoite;
    }

    public List<Marcador> getMarcadores() {
        return marcadores;
    }

    public List<String> getDestinosViagem() {
        return destinosViagem;
    }

    public static MapaConfig paraLocal(String id) {
        if (id != null) {
            for (MapaConfig config : values()) {
                if (config.localId.equalsIgnoreCase(id)) {
                    return config;
                }
            }
        }
        return CIDADE_NATAL;
    }
}
