package com.exa863.anselmo_adna.model.stats;

public class Itens {

    // Alimentos (vendidos na loja)
    public static final Item SALADA = new Item(
            "Salada", "Uma salada fresca e saudavel.", TipoItem.COMIDA, 15, 5, 0);
    public static final Item BIFE = new Item(
            "Bife", "Um bife suculento, rico em proteína.", TipoItem.COMIDA, 30, 15, 0);
    public static final Item REFRIGERANTE = new Item(
            "Refrigerante", "Uma bebida gelada e açucarada.", TipoItem.COMIDA, 10, 3, 5);
    public static final Item BARRA_CEREAL = new Item(
            "Barra de Cereal", "Prática para repor energia rápido.", TipoItem.COMIDA, 12, 8, 10);

    // Bebidas Energéticas
    public static final Item CAFE = new Item(
            "Café Expresso", "Um café puro e forte para despertar.", TipoItem.COMIDA, 8, 0, 20);
    public static final Item ENERGETICO = new Item(
            "Energético", "Bebida cheia de taurina e cafeína.", TipoItem.COMIDA, 20, 0, 40);

    // Suplementos (vendidos na loja)
    public static final Item REMEDIO = new Item(
            "Remédio", "Ajuda a recuperar a saúde rapidamente.", TipoItem.SUPLEMENTO, 50, 25, 0);
    public static final Item VITAMINAS = new Item(
            "Vitaminas", "Fortalece o corpo e melhora a recuperação.", TipoItem.SUPLEMENTO, 40, 20, 10);

    // Itens especiais
    public static final Item CONTRATO = new Item(
            "Contrato", "Um contrato para se vender.", TipoItem.ESPECIAL, 0, 0, 0);
    public static final Item LUVA_DO_PAI = new Item(
            "Luva do Pai", "A luva de boxe que pertenceu ao seu pai.", TipoItem.ESPECIAL, 0, 0, 0);

    public static final Item[] ITENS_LOJA = {
            SALADA, BIFE, REFRIGERANTE, BARRA_CEREAL, CAFE, ENERGETICO, REMEDIO, VITAMINAS
    };

    private Itens() {
    }
}