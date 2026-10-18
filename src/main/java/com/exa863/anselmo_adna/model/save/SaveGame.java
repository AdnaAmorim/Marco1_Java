package com.exa863.anselmo_adna.model.save;

import com.exa863.anselmo_adna.model.GameState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Representa os dados salvos do jogo.
 *
 * @author Anselmo e Adna
 */
@JacksonXmlRootElement(localName = "saveGame")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaveGame {

    public static final int VERSAO_ATUAL = 3;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @JacksonXmlProperty(isAttribute = true)
    private int versao;

    @JacksonXmlProperty(isAttribute = true)
    private String dataSalvamento;

    @JacksonXmlProperty(isAttribute = true)
    private boolean automatico;

    private GameState gameState;

    public SaveGame() {
    }

    public SaveGame(GameState gameState, boolean automatico) {
        this(VERSAO_ATUAL, automatico);
        this.gameState = Objects.requireNonNull(gameState, "O estado da partida é obrigatório.");
    }

    public SaveGame(GameState gameState) {
        this(gameState, false);
    }

    public SaveGame(int versao) {
        this(versao, LocalDateTime.now().format(FORMATTER), false);
    }

    public SaveGame(int versao, boolean automatico) {
        this(versao, LocalDateTime.now().format(FORMATTER), automatico);
    }

    public SaveGame(int versao, String dataSalvamento) {
        this(versao, dataSalvamento, false);
    }

    public SaveGame(int versao, String dataSalvamento, boolean automatico) {
        this.versao = versao;
        this.dataSalvamento = dataSalvamento;
        this.automatico = automatico;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public String getDataSalvamento() {
        return dataSalvamento;
    }

    public void setDataSalvamento(String dataSalvamento) {
        this.dataSalvamento = dataSalvamento;
    }

    public boolean isAutomatico() {
        return automatico;
    }

    public void setAutomatico(boolean automatico) {
        this.automatico = automatico;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaveGame that)) return false;
        return versao == that.versao &&
                automatico == that.automatico &&
                Objects.equals(dataSalvamento, that.dataSalvamento) &&
                Objects.equals(gameState, that.gameState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versao, dataSalvamento, automatico, gameState);
    }

    @Override
    public String toString() {
        return "SaveGame{" +
                "versao=" + versao +
                ", dataSalvamento='" + dataSalvamento + '\'' +
                ", automatico=" + automatico +
                '}';
    }
}
