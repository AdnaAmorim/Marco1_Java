package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.view.fx.Icone;
import com.exa863.anselmo_adna.view.fx.Som;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controla o modal do guia do boxeador (tutorial).
 *
 * @author Anselmo e Adna
 */
public class TutorialController {

    @FXML private Button btnAbaRotina;
    @FXML private Button btnAbaAtributos;
    @FXML private Button btnAbaHistoria;
    @FXML private Button btnAbaEconomia;
    @FXML private Button btnVoltar;

    @FXML private Label lblSecaoTitulo;
    @FXML private Label lblItem1;
    @FXML private Label lblItem2;
    @FXML private Label lblItem3;
    @FXML private Label lblItem4;
    @FXML private Label lblItem5;

    private Button[] abas;
    private Label[] itens;
    private int abaAtual = -1;

    @FXML
    public void initialize() {
        abas = new Button[] { btnAbaRotina, btnAbaAtributos, btnAbaHistoria, btnAbaEconomia };
        itens = new Label[] { lblItem1, lblItem2, lblItem3, lblItem4, lblItem5 };
        IconManager.aplicarBotao(btnAbaRotina, Icone.CLOCK, 14);
        IconManager.aplicarBotao(btnAbaAtributos, Icone.HEART, 14);
        IconManager.aplicarBotao(btnAbaHistoria, Icone.BOOKMARK, 14);
        IconManager.aplicarBotao(btnAbaEconomia, Icone.BAG, 14);
        IconManager.aplicarBotao(btnVoltar, Icone.LEFT_ARROW, 16);
        exibirSecao(0, false, "CICLO DE VIDA E ROTINA",
                "• O TEMPO E OS DIAS AVANÇAM A CADA ATIVIDADE REALIZADA.",
                "• TREINAR CONSOME ENERGIA E APRIMORA SEUS ATRIBUTOS FÍSICOS.",
                "• DORMIR EM CASA RESTAURA SUAS FORÇAS PARA A PRÓXIMA JORNADA.");
    }

    @FXML
    public void abrirRotina() {
        exibirSecao(0, true, "CICLO DE VIDA E ROTINA",
                "• O TEMPO E OS DIAS AVANÇAM A CADA ATIVIDADE REALIZADA.",
                "• TREINAR CONSOME ENERGIA E APRIMORA SEUS ATRIBUTOS FÍSICOS.",
                "• DORMIR EM CASA RESTAURA SUAS FORÇAS PARA A PRÓXIMA JORNADA.");
    }

    @FXML
    public void abrirAtributos() {
        exibirSecao(1, true, "SEUS ATRIBUTOS DE COMBATE",
                "• SAÚDE & ENERGIA: BASES VITAIS NO DIA A DIA E NOS RINGUES.",
                "• FORÇA: POTÊNCIA E IMPACTO DEVASTADOR DOS GOLPES.",
                "• AGILIDADE: VELOCIDADE DE ATAQUE, ESQUIVAS E REFLEXOS.",
                "• RESISTÊNCIA: ABSORÇÃO DE DANO E FÔLEGO NAS LUTAS.",
                "• INTELIGÊNCIA: LEITURA TÁTICA DOS OPONENTES E ESTRATÉGIAS.");
    }

    @FXML
    public void abrirHistoria() {
        exibirSecao(2, true, "EXPLORAÇÃO & HISTÓRIA (CAPÍTULOS)",
                "• VIAJE PELO MAPA ENTRE CIDADES, ACADEMIAS E ARENAS.",
                "• ENTRAR EM LOCAIS ESPECÍFICOS ACIONA OS CAPÍTULOS DA HISTÓRIA.",
                "• SUAS ESCOLHAS DEFINEM ALIANÇAS, CONTRATOS E O DESFECHO FINAL!");
    }

    @FXML
    public void abrirEconomia() {
        exibirSecao(3, true, "ECONOMIA & ITENS",
                "• VISITE A LOJA PARA COMPRAR ALIMENTOS E REMÉDIOS REVIGORANTES.",
                "• GUARDE DINHEIRO PARA PAGAR PASSAGENS DE VIAGEM ENTRE CIDADES.",
                "• USE A MOCHILA PARA CONSUMIR ITENS E RECUPERAR ENERGIA OU SAÚDE.");
    }

    @FXML
    public void voltar() {
        SceneManager.fecharModal();
    }

    private void exibirSecao(int indiceAba, boolean tocarSom, String titulo, String... linhas) {
        if (tocarSom && indiceAba != abaAtual) {
            AudioManager.tocar(Som.TROCAR_ABA);
        }
        abaAtual = indiceAba;

        for (int i = 0; i < abas.length; i++) {
            abas[i].getStyleClass().remove("aba-ativa");
            if (i == indiceAba) {
                abas[i].getStyleClass().add("aba-ativa");
            }
        }

        lblSecaoTitulo.setText(titulo);
        for (int i = 0; i < itens.length; i++) {
            boolean visivel = i < linhas.length;
            itens[i].setText(visivel ? linhas[i] : "");
            itens[i].setVisible(visivel);
            itens[i].setManaged(visivel);
        }
    }
}
