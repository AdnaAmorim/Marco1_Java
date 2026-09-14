package com.exa863.anselmo_adna.tools;

import com.exa863.anselmo_adna.controller.FightController;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.combat.AtributosEfetivos;
import com.exa863.anselmo_adna.model.combat.EstiloLuta;
import com.exa863.anselmo_adna.model.combat.ResultadoLuta;
import com.exa863.anselmo_adna.model.combat.estilos.Contragolpeador;
import com.exa863.anselmo_adna.model.combat.estilos.Nocauteador;
import com.exa863.anselmo_adna.model.combat.estilos.Velocista;
import com.exa863.anselmo_adna.model.stats.Atributo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// servidor local leve em Java puro para testar o balanceamento no navegador
public class SimuladorServer {

    private static final int PORTA = 8080;
    private static final FightController fightController = new FightController();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORTA), 0);

        // rota da pagina html
        server.createContext("/", new PaginaHandler());

        // rota da api que executa a luta oficial do Java
        server.createContext("/api/combate", new CombateApiHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("==================================================");
        System.out.println("🔥 Servidor de Balanceamento Iniciado com Sucesso!");
        System.out.println("👉 Abra no navegador: http://localhost:" + PORTA);
        System.out.println("Toda alteracao de atributo executa o FightController REAL do Java.");
        System.out.println("Pressione Ctrl+C para encerrar.");
        System.out.println("==================================================");
    }

    // entrega o arquivo simulador_combate.html
    static class PaginaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File htmlFile = new File("simulador_combate.html");
            if (!htmlFile.exists()) {
                String erro = "Arquivo simulador_combate.html nao encontrado na raiz do projeto.";
                exchange.sendResponseHeaders(404, erro.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(erro.getBytes(StandardCharsets.UTF_8));
                }
                return;
            }

            byte[] bytes = Files.readAllBytes(htmlFile.toPath());
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // recebe os dados da tela e chama o FightController oficial
    static class CombateApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // adiciona headers de cors caso abra direto
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            // le o corpo json da requisicao
            String corpoJson;
            try (InputStream is = exchange.getRequestBody()) {
                corpoJson = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            Map<String, String> dados = extrairJsonSimples(corpoJson);

            // monta lutador 1
            String nome1 = dados.getOrDefault("nome1", "Lutador 1");
            int s1 = parseInt(dados.get("s1"), 100);
            int e1 = parseInt(dados.get("e1"), 100);
            int f1 = parseInt(dados.get("f1"), 5);
            int a1 = parseInt(dados.get("a1"), 5);
            int r1 = parseInt(dados.get("r1"), 5);
            int i1 = parseInt(dados.get("i1"), 5);
            EstiloLuta estilo1 = criarEstilo(dados.getOrDefault("estilo1", "velocista"));

            Personagem l1 = new Personagem(1, nome1, "Corner Azul", Cores.AZUL, Sexo.MASCULINO);
            Atributo attr1 = new Atributo(s1, f1, a1, r1, i1, e1);

            // monta lutador 2
            String nome2 = dados.getOrDefault("nome2", "Lutador 2");
            int s2 = parseInt(dados.get("s2"), 100);
            int e2 = parseInt(dados.get("e2"), 100);
            int f2 = parseInt(dados.get("f2"), 5);
            int a2 = parseInt(dados.get("a2"), 5);
            int r2 = parseInt(dados.get("r2"), 5);
            int i2 = parseInt(dados.get("i2"), 5);
            EstiloLuta estilo2 = criarEstilo(dados.getOrDefault("estilo2", "nocauteador"));

            Personagem l2 = new Personagem(2, nome2, "Corner Vermelho", Cores.PRETO, Sexo.MASCULINO);
            Atributo attr2 = new Atributo(s2, f2, a2, r2, i2, e2);

            // executa o combate usando o codigo oficial do projeto
            ResultadoLuta resultado = fightController.resolverCombate(l1, attr1, estilo1, l2, attr2, estilo2);

            AtributosEfetivos eff1 = estilo1.aplicarModificadores(attr1);
            AtributosEfetivos eff2 = estilo2.aplicarModificadores(attr2);

            // calculo dos pilares individuais de cada um
            double acerto1 = (eff1.agilidade * FightController.PESO_AGILIDADE) + (eff1.inteligencia * FightController.PESO_INTELIGENCIA);
            double dano1 = (eff1.forca * FightController.PESO_FORCA) - (eff2.resistencia * FightController.PESO_RESISTENCIA);
            double folego1 = (eff1.saude * FightController.PESO_SAUDE) + (eff1.energia * FightController.PESO_ENERGIA);

            double acerto2 = (eff2.agilidade * FightController.PESO_AGILIDADE) + (eff2.inteligencia * FightController.PESO_INTELIGENCIA);
            double dano2 = (eff2.forca * FightController.PESO_FORCA) - (eff1.resistencia * FightController.PESO_RESISTENCIA);
            double folego2 = (eff2.saude * FightController.PESO_SAUDE) + (eff2.energia * FightController.PESO_ENERGIA);

            String vencedorNome = resultado.getVencedor() != null ? resultado.getVencedor().getNome() : "Empate";
            String tipoVit = resultado.getTipoVitoria() != null ? resultado.getTipoVitoria().name() : "EMPATE";
            String tipoVitDesc = resultado.getTipoVitoria() != null ? resultado.getTipoVitoria().getDescricao() : "Empate";

            // monta o json de resposta formatado
            String respostaJson = String.format(Locale.US, """
            {
              "vencedor": "%s",
              "tipoVitoria": "%s",
              "tipoVitoriaDescricao": "%s",
              "descricao": "%s",
              "score1": %.2f,
              "score2": %.2f,
              "saldo": %.2f,
              "isEmpate": %b,
              "constantes": {
                "pesoAgilidade": %.2f,
                "pesoInteligencia": %.2f,
                "pesoForca": %.2f,
                "pesoResistencia": %.2f,
                "pesoSaude": %.2f,
                "pesoEnergia": %.2f,
                "pesoAcerto": %.2f,
                "pesoDano": %.2f,
                "pesoFolego": %.2f,
                "pontosNocaute": %.1f,
                "pontosDecisao": %.1f,
                "pontosEmpate": %.1f
              },
              "lutador1": {
                "effForca": %.1f, "effAgilidade": %.1f, "effResistencia": %.1f, "effInteligencia": %.1f,
                "effSaude": %.0f, "effEnergia": %.0f,
                "acerto": %.1f, "dano": %.1f, "folego": %.1f, "score": %.2f
              },
              "lutador2": {
                "effForca": %.1f, "effAgilidade": %.1f, "effResistencia": %.1f, "effInteligencia": %.1f,
                "effSaude": %.0f, "effEnergia": %.0f,
                "acerto": %.1f, "dano": %.1f, "folego": %.1f, "score": %.2f
              }
            }
            """,
            vencedorNome, tipoVit, tipoVitDesc, resultado.getDescricao(),
            resultado.getScoreLutador1(), resultado.getScoreLutador2(), resultado.getSaldoFinal(),
            resultado.isEmpate(),
            FightController.PESO_AGILIDADE, FightController.PESO_INTELIGENCIA,
            FightController.PESO_FORCA, FightController.PESO_RESISTENCIA,
            FightController.PESO_SAUDE, FightController.PESO_ENERGIA,
            FightController.PESO_ACERTO, FightController.PESO_DANO, FightController.PESO_FOLEGO,
            FightController.PONTOS_NOCAUTE, FightController.PONTOS_DECISAO, FightController.PONTOS_EMPATE,
            eff1.forca, eff1.agilidade, eff1.resistencia, eff1.inteligencia, eff1.saude, eff1.energia,
            acerto1 * FightController.PESO_ACERTO, dano1 * FightController.PESO_DANO, folego1 * FightController.PESO_FOLEGO, resultado.getScoreLutador1(),
            eff2.forca, eff2.agilidade, eff2.resistencia, eff2.inteligencia, eff2.saude, eff2.energia,
            acerto2 * FightController.PESO_ACERTO, dano2 * FightController.PESO_DANO, folego2 * FightController.PESO_FOLEGO, resultado.getScoreLutador2()
            );

            byte[] respBytes = respostaJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, respBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(respBytes);
            }
        }
    }

    private static EstiloLuta criarEstilo(String nome) {
        if (nome == null) return new Velocista();
        return switch (nome.toLowerCase().trim()) {
            case "nocauteador" -> new Nocauteador();
            case "contragolpeador" -> new Contragolpeador();
            default -> new Velocista();
        };
    }

    private static int parseInt(String val, int padrao) {
        if (val == null) return padrao;
        try {
            return (int) Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return padrao;
        }
    }

    // extrator leve de chaves e valores json sem precisar de biblioteca externa
    private static Map<String, String> extrairJsonSimples(String json) {
        Map<String, String> mapa = new HashMap<>();
        if (json == null) return mapa;

        Pattern pattern = Pattern.compile("\"(\\w+)\"\\s*:\\s*(?:\"([^\"]*)\"|([\\d.-]+))");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String chave = matcher.group(1);
            String valorTexto = matcher.group(2);
            String valorNumero = matcher.group(3);
            mapa.put(chave, valorTexto != null ? valorTexto : valorNumero);
        }
        return mapa;
    }
}
