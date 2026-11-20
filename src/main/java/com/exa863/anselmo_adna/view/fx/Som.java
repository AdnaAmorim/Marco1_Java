package com.exa863.anselmo_adna.view.fx;

/**
 * Lista os efeitos sonoros da interface e do jogo em resources/audios.
 *
 * @author Anselmo e Adna
 */
public enum Som {

    HOVER("Buttons/hover_soft_01"),
    CLIQUE("Buttons/click_button_01"),
    SLIDER_STEP("Buttons/boop_up_01"),
    SELECIONAR_SLOT("Buttons/tap_wood_01"),
    ABRIR_MODAL("Buttons/click_button_01"),
    FECHAR_MODAL("Buttons/pop_bubble_01"),
    TROCAR_ABA("Menus/page_turn_01"),
    TOGGLE_ON("Toggles/toggle_on"),
    TOGGLE_OFF("Toggles/toggle_off"),
    CONFIRMAR("Feedback/confirm_01"),
    CANCELAR("Feedback/cancel_01"),
    ERRO("Feedback/error_01"),
    NOTIFICACAO("Notifications/notification_01"),
    MOEDA("Pickups/coin_01"),
    NIVEL("Rewards/level_up"),
    FALA("Text/talk_mid_01");

    private static final String PASTA_AUDIOS = "/audios/";
    private static final String EXTENSAO = ".wav";

    private final String caminhoRelativo;

    Som(String caminhoRelativo) {
        this.caminhoRelativo = caminhoRelativo;
    }

    public String getCaminho() {
        return PASTA_AUDIOS + caminhoRelativo + EXTENSAO;
    }
}
