package com.exa863.anselmo_adna.controller.fx;

import com.exa863.anselmo_adna.exception.FalhaCarregamentoException;
import com.exa863.anselmo_adna.persistencia.Conquistas;
import com.exa863.anselmo_adna.persistencia.ProgressoGlobal;
import com.exa863.anselmo_adna.persistencia.ProgressoXML;
import com.exa863.anselmo_adna.view.fx.Icone;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Modal de conquistas e progresso global acessível pelo menu inicial.
 *
 * @author Anselmo e Adna
 */
public class ConquistasController {

    private record ItemConquista(String id, String descricao) {
    }

    private static final List<ItemConquista> CATALOGO = List.of(
            new ItemConquista(Conquistas.PRIMEIROS_PASSOS, "VENÇA SUA PRIMEIRA LUTA NO RINGUE OU NO CLUBE."),
            new ItemConquista(Conquistas.DESTAQUE_ESTADUAL, "COMPLETE AS LUTAS DO CINTURÃO ESTADUAL."),
            new ItemConquista(Conquistas.NOME_NACIONAL, "COMPLETE AS LUTAS DO CINTURÃO NACIONAL."),
            new ItemConquista(Conquistas.CORACAO_DE_LUTADOR, "MOSTROU DETERMINAÇÃO E HONRA NA JORNADA."),
            new ItemConquista(Conquistas.INVESTIGADOR, "DESCOBRIU A VERDADE SOBRE O PASSADO DE SEU PAI."),
            new ItemConquista(Conquistas.CHEGUEI_AO_MUNDIAL, "ALCANÇOU OS REQUISITOS E CHEGOU AO CAMPEONATO MUNDIAL."),
            new ItemConquista(Conquistas.CAMPEAO_DO_MUNDO, "DERROTOU ANTHONY 'O TROVÃO' NO CAMPEONATO MUNDIAL."),
            new ItemConquista(Conquistas.VERDADEIRO_LEGADO, "CONQUISTOU O FINAL VERDADEIRO DO LEGADO DO RINGUE."),
            new ItemConquista(Conquistas.MULTIPLOS_DESTINOS, "ALCANÇOU MAIS DE UM FINAL DIFERENTE NA HISTÓRIA."),
            new ItemConquista(Conquistas.TODOS_OS_CAMINHOS, "DESBLOQUEOU TODOS OS FINAIS POSSÍVEIS DO JOGO.")
    );

    @FXML private StackPane rootPane;
    @FXML private Label lblResumo;
    @FXML private VBox listaConquistas;
    @FXML private Button btnFechar;

    @FXML
    public void initialize() {
        IconManager.aplicarBotao(btnFechar, Icone.LEFT_ARROW, 15);
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                fechar();
                event.consume();
            }
        });
        carregarConquistas();
    }

    private void carregarConquistas() {
        ProgressoGlobal progresso = new ProgressoGlobal();
        try {
            new ProgressoXML().carregar(progresso);
        } catch (FalhaCarregamentoException ignored) {
        }

        int desbloqueadas = 0;
        listaConquistas.getChildren().clear();

        for (ItemConquista item : CATALOGO) {
            boolean ok = progresso.conquistaJaDesbloqueada(item.id());
            if (ok) {
                desbloqueadas++;
            }
            listaConquistas.getChildren().add(criarLinhaConquista(item, ok));
        }

        lblResumo.setText(desbloqueadas + " / " + CATALOGO.size()
                + "  •  FINAIS: " + progresso.finaisAlcancados.size());
    }

    private HBox criarLinhaConquista(ItemConquista item, boolean desbloqueada) {
        Label lblIcone = new Label();
        IconManager.aplicar(lblIcone,
                desbloqueada ? Icone.STAR : Icone.LOCK_CLOSED,
                20,
                desbloqueada ? IconManager.COR_DESTAQUE : IconManager.COR_TEXTO);

        Label lblNome = new Label(Conquistas.getNome(item.id()).toUpperCase());
        lblNome.getStyleClass().add("conq-nome");
        if (desbloqueada) {
            lblNome.getStyleClass().add("conq-nome-desbloqueada");
        }

        Label lblDesc = new Label(item.descricao());
        lblDesc.getStyleClass().add("conq-desc");
        lblDesc.setWrapText(true);

        VBox textos = new VBox(2, lblNome, lblDesc);
        HBox.setHgrow(textos, Priority.ALWAYS);

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.SOMETIMES);

        Label lblStatus = new Label(desbloqueada ? "DESBLOQUEADA" : "BLOQUEADA");
        lblStatus.getStyleClass().add("conq-status");
        if (desbloqueada) {
            lblStatus.getStyleClass().add("conq-status-ok");
        }

        HBox linha = new HBox(lblIcone, textos, lblStatus);
        linha.getStyleClass().add("conq-item");
        if (desbloqueada) {
            linha.getStyleClass().add("conq-item-desbloqueada");
        }
        return linha;
    }

    @FXML
    public void fechar() {
        SceneManager.fecharModal();
    }
}
