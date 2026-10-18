package com.exa863.anselmo_adna.controller;

import com.exa863.anselmo_adna.controller.cenas.CutsceneController;
import com.exa863.anselmo_adna.model.GameState;
import com.exa863.anselmo_adna.model.character.Cores;
import com.exa863.anselmo_adna.model.character.Personagem;
import com.exa863.anselmo_adna.model.character.Player;
import com.exa863.anselmo_adna.model.character.Sexo;
import com.exa863.anselmo_adna.model.narrativa.Dialogo;
import com.exa863.anselmo_adna.model.narrativa.TipoGatilho;
import com.exa863.anselmo_adna.model.narrativa.capitulos.Capitulo01;
import com.exa863.anselmo_adna.model.narrativa.capitulos.Capitulo02;
import com.exa863.anselmo_adna.model.save.SaveGame;
import com.exa863.anselmo_adna.model.stats.Item;
import com.exa863.anselmo_adna.model.stats.Itens;
import com.exa863.anselmo_adna.model.stats.TipoItem;
import com.exa863.anselmo_adna.model.world.EstadoGame;
import com.exa863.anselmo_adna.model.world.Local;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/** Exercita gravação real em XML e a continuidade das regras depois de carregar. */
class GameStatePersistenceTest {

    @TempDir
    Path pasta;

    private GameState novaPartida() {
        GameState state = new GameState();
        state.setPlayer(new Player(42, "João & Ana", "Boxeador <iniciante>", Cores.VERDE, Sexo.FEMININO));
        return state;
    }

    @Test
    void restauraPartidaCompletaSemReiniciarDadosOuDuplicarLocais() throws Exception {
        SaveGameController saves = new SaveGameController(pasta.toString());
        GameState original = novaPartida();
        Player player = original.getPlayer();
        player.setDinheiro(4321);
        player.getAtributos().setSaude(73);
        player.getAtributos().setEnergia(64);
        player.getAtributos().setForca(8);
        player.getAtributos().setAgilidade(7);
        player.getAtributos().setResistencia(6);
        player.getAtributos().setInteligencia(5);
        original.getDataDia().avancarMinutos(2 * 1440 + 137);
        int dia = original.getDataDia().getDias();
        player.registrarTreino(dia);
        player.registrarTreino(dia);
        player.registrarLutaBoxe(dia);
        player.registrarLutaLocal();
        player.registrarLutaEstadual();
        player.registrarLutaEstadual();
        player.registrarLutaNacional();
        player.definirRomance("Olivia");
        Personagem olivia = new Personagem(2, "Olivia", "Fisioterapeuta", Cores.CASTANHO, Sexo.FEMININO);
        player.alterarAfinidade(olivia, 12);
        player.alterarAfinidade(new Personagem(3, "Mestre Smith", "Treinador", Cores.PRETO, Sexo.MASCULINO), -2);
        player.concluirCapitulo(Capitulo01.ID);
        player.concluirCapitulo("FLAG_CONTRATO_ACEITO");
        player.getInventario().adicionarItem(Itens.CAFE, 3);
        player.getInventario().adicionarItem(Itens.LUVA_DO_PAI);
        Item itemCustomizado = new Item("Especial & único", "Descrição <especial>", TipoItem.SUPLEMENTO, 79, 11, 13);
        player.getInventario().adicionarItem(itemCustomizado, 2);
        original.registrarInteracao(Capitulo01.ID, "O que fazer?", 0, "Pegar as Luvas");
        original.setEstadoGame(EstadoGame.EXPLORANDO);

        Local cidadeNatal = original.getLocalAtual();
        Local cidadeA = cidadeNatal.getSubLocais().stream()
                .filter(l -> l.getNome().equals("Cidade A")).findFirst().orElseThrow();
        cidadeA.setAcessoLiberado(true);
        cidadeA.setCustoAcesso(450);
        original.setLocalAtual(cidadeA.getSubLocais().get(0)); // Loja, dentro de Cidade A.

        assertTrue(saves.salvar(1, original));
        String xmlSalvo = Files.readString(pasta.resolve("save_game_1.xml"));
        assertTrue(xmlSalvo.contains("<localAtualId>loja</localAtualId>"));
        assertTrue(xmlSalvo.contains("<locaisDesbloqueados>"));
        assertFalse(xmlSalvo.contains("<mapaGlobal>"));
        assertFalse(xmlSalvo.contains("<caminhoLocalAtual>"));
        // Alterar a memória depois de salvar não altera o estado gravado.
        player.setDinheiro(1);
        player.getInventario().removerItem(Itens.CAFE);
        original.getDataDia().avancarMinutos(30);

        GameState carregado = saves.carregarGameState(1);
        assertNotNull(carregado);
        assertNotSame(original, carregado);
        Player restaurado = carregado.getPlayer();
        assertEquals(42, restaurado.getId());
        assertEquals("João & Ana", restaurado.getNome());
        assertEquals("Boxeador <iniciante>", restaurado.getDescricao());
        assertEquals(Cores.VERDE, restaurado.getCorOlhos());
        assertEquals(Sexo.FEMININO, restaurado.getSexo());
        assertEquals(4321, restaurado.getDinheiro());
        assertEquals(73, restaurado.getAtributos().getSaude());
        assertEquals(64, restaurado.getAtributos().getEnergia());
        assertEquals(8, restaurado.getAtributos().getForca());
        assertEquals(7, restaurado.getAtributos().getAgilidade());
        assertEquals(6, restaurado.getAtributos().getResistencia());
        assertEquals(5, restaurado.getAtributos().getInteligencia());
        assertEquals(3, carregado.getDataDia().getDias());
        assertEquals("08:17", carregado.getDataDia().getHoraFormatada());
        assertEquals(3, restaurado.getTreinosRestantes(dia));
        assertEquals(5, restaurado.getTreinosRestantes(dia + 1));
        assertFalse(restaurado.podeLutarBoxe(dia));
        assertTrue(restaurado.podeLutarBoxe(dia + 1));
        assertEquals(dia, restaurado.getUltimoDiaLutaBoxe());
        assertEquals(1, restaurado.getLutasLocais());
        assertEquals(2, restaurado.getLutasEstaduais());
        assertEquals(1, restaurado.getLutasNacionais());
        assertEquals("Olivia", restaurado.getPersonagemRomance());
        assertFalse(restaurado.podeRomancearCom("James"));
        assertEquals(12, restaurado.getAfinidade(olivia));
        restaurado.alterarAfinidade(olivia, 1);
        assertEquals(13, restaurado.getAfinidade(olivia));
        assertEquals(10, restaurado.getRelacionamentos().length);
        assertNull(restaurado.getRelacionamentos()[9]);
        assertTrue(restaurado.isCapituloConcluido(Capitulo01.ID));
        assertTrue(restaurado.isCapituloConcluido("FLAG_CONTRATO_ACEITO"));
        assertEquals(3, restaurado.getInventario().getQuantidade(Itens.CAFE));
        assertTrue(restaurado.getInventario().temItem(Itens.LUVA_DO_PAI));
        Item itemRestaurado = restaurado.getInventario().getItens().keySet().stream()
                .filter(item -> item.equals(itemCustomizado)).findFirst().orElseThrow();
        assertEquals("Descrição <especial>", itemRestaurado.getDescricao());
        assertEquals(TipoItem.SUPLEMENTO, itemRestaurado.getTipo());
        assertEquals(79, itemRestaurado.getPreco());
        assertEquals(11, itemRestaurado.getCura());
        assertEquals(13, itemRestaurado.getEnergia());
        assertEquals(2, restaurado.getInventario().getQuantidade(itemRestaurado));
        assertTrue(restaurado.getInventario().usarItem(Itens.CAFE, restaurado));
        assertEquals(84, restaurado.getAtributos().getEnergia());
        assertEquals(original.getInteracoes(), carregado.getInteracoes());
        assertEquals(3, carregado.getInteracoes().get(0).dia());
        assertEquals(EstadoGame.EXPLORANDO, carregado.getEstadoGame());

        GameController jogo = new GameController();
        jogo.carregarGameState(carregado);
        assertSame(carregado, jogo.getGameState());
        assertSame(restaurado, jogo.getPlayer());
        assertSame(carregado.getDataDia(), jogo.getDataDia());
        assertEquals("Loja", jogo.getLocalAtual().getNome());
        Local cidadeRestaurada = jogo.getLocalAtual().getLocalPai();
        assertTrue(cidadeRestaurada.isAcessoLiberado());
        // O custo é configuração do mapa; somente o acesso liberado vem do save.
        assertEquals(400, cidadeRestaurada.getCustoAcesso());
        assertEquals(java.util.List.of("cidade_a"), carregado.getLocaisDesbloqueados());
        assertSame(jogo.getMapaGlobal().getSubLocais().get(0), cidadeRestaurada.getLocalPai());
        jogo.voltarLocal();
        assertSame(cidadeRestaurada, jogo.getLocalAtual());
        Local saida = cidadeRestaurada.getSubLocais().stream().filter(Local::isLocalDeSaida)
                .findFirst().orElseThrow();
        jogo.entrarLocal(saida);
        assertEquals("Cidade Natal", jogo.getLocalAtual().getNome());
        assertEquals(Capitulo02.ID, jogo.getNarrativaController()
                .obterCapituloDisponivel(TipoGatilho.ENTRAR_LOCAL, "Academia", restaurado)
                .orElseThrow().getId());
    }

    @Test
    void roundTripDePartidaNovaPreservaColecoesVaziasERaizDoMapa() throws Exception {
        SaveGameController saves = new SaveGameController(pasta.toString());
        GameState state = novaPartida();
        state.setLocalAtual(state.getMapaGlobal());
        assertTrue(saves.autoSave(2, state));
        SaveGame save = saves.carregar(2);
        assertNotNull(save);
        assertTrue(save.isAutomatico());
        GameState carregado = save.getGameState();
        assertTrue(carregado.getInteracoes().isEmpty());
        assertTrue(carregado.getLocaisDesbloqueados().isEmpty());
        assertTrue(carregado.getPlayer().getInventario().getItens().isEmpty());
        assertTrue(carregado.getPlayer().getCapitulosConcluidos().isEmpty());
        assertNull(carregado.getPlayer().getRelacionamentos()[0]);
        assertNull(carregado.getPlayer().getPersonagemRomance());
        assertSame(carregado.getMapaGlobal(), carregado.getLocalAtual());
        assertNull(carregado.getMapaGlobal().getLocalPai());
    }

    @Test
    void escolhaRealSalvaHistoricoEConsequencias() throws Exception {
        GameState state = novaPartida();
        CutsceneController cena = new CutsceneController(new Capitulo01(), state);
        Dialogo escolha = cena.getDialogos().stream().filter(Dialogo::isEscolha).findFirst().orElseThrow();
        cena.processarEscolha(escolha, 0);
        cena.finalizarCapitulo();
        SaveGameController saves = new SaveGameController(pasta.toString());
        assertTrue(saves.salvar(1, state));
        GameState carregado = saves.carregarGameState(1);
        assertNotNull(carregado);
        assertTrue(carregado.getPlayer().isCapituloConcluido(Capitulo01.ID));
        assertTrue(carregado.getPlayer().getInventario().temItem(Itens.LUVA_DO_PAI));
        assertEquals(escolha.getTexto(), carregado.getInteracoes().get(0).pergunta());
        assertEquals("Pegar as Luvas", carregado.getInteracoes().get(0).resposta());
        assertEquals(Capitulo01.ID, carregado.getInteracoes().get(0).capituloId());
    }

    @Test
    void rejeitaSlotsAntigosSemEstadoCorrompidosEVersaoDesconhecida() throws Exception {
        XmlFileController xml = new XmlFileController(pasta);
        SaveGameController saves = new SaveGameController(xml);
        xml.salvar("save_game_1", new SaveGame(1));
        xml.salvar("save_game_2", new SaveGame(SaveGame.VERSAO_ATUAL));
        Files.writeString(pasta.resolve("save_game_3.xml"), "<saveGame><quebrado>");
        SaveGame futuro = new SaveGame(novaPartida());
        futuro.setVersao(999);
        xml.salvar("save_game_4", futuro);
        for (int slot = 1; slot <= 4; slot++) {
            final int slotInvalido = slot;
            assertThrows(Exception.class, () -> saves.carregarGameState(slotInvalido));
        }
        assertThrows(IllegalArgumentException.class, () -> saves.salvar(1, null));
        assertThrows(IllegalArgumentException.class, () -> saves.salvar(1, new GameState()));
    }

    @Test
    void falhaDeGravacaoNaoDestroiSaveAnterior() throws IOException {
        XmlFileController xml = new XmlFileController(pasta);
        xml.salvar("slot", new SaveGame(novaPartida()));
        String anterior = Files.readString(pasta.resolve("slot.xml"));
        Object dadosComFalha = new Object() {
            public String getDado() throws IOException {
                throw new IOException("Falha simulada durante a gravação.");
            }
        };
        assertThrows(IOException.class, () -> xml.salvar("slot", dadosComFalha));
        assertEquals(anterior, Files.readString(pasta.resolve("slot.xml")));
        try (var arquivos = Files.list(pasta)) {
            assertEquals(1, arquivos.count());
        }
    }

    @Test
    void rejeitaRelogioOuPosicaoInvalidaSemAlterarPartidaAtiva() throws IOException {
        GameState state = novaPartida();
        XmlMapper mapper = new XmlMapper();
        String valido = mapper.writeValueAsString(state);
        assertTrue(valido.contains("<localAtualId>cidade_natal</localAtualId>"));
        assertThrows(JsonMappingException.class, () -> mapper.readValue(
                valido.replace("<horas>6</horas>", "<horas>24</horas>"), GameState.class));
        assertThrows(JsonMappingException.class, () -> mapper.readValue(
                valido.replace("<localAtualId>cidade_natal</localAtualId>",
                        "<localAtualId>local_inexistente</localAtualId>"), GameState.class));
        assertThrows(IllegalArgumentException.class,
                () -> state.setLocalAtual(new Local("loja", "Loja", "Fora desta partida")));
        assertEquals("Cidade Natal", state.getLocalAtual().getNome());
    }
}
