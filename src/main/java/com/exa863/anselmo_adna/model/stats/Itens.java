package com.exa863.anselmo_adna.model.stats;

public class Itens {

    // Alimentos (vendidos na loja)
    public static final Item SALADA = new Item(
            "Salada", "Uma salada fresca e saudavel.", TipoItem.COMIDA, 15, 5);
    public static final Item BIFE = new Item(
            "Bife", "Um bife suculento, rico em proteína.", TipoItem.COMIDA, 30, 15);
    public static final Item REFRIGERANTE = new Item(
            "Refrigerante", "Uma bebida gelada e açucarada.", TipoItem.COMIDA, 10, 3);
    public static final Item BARRA_CEREAL = new Item(
            "Barra de Cereal", "Prática para repor energia rápido.", TipoItem.COMIDA, 12, 8);

    // Suplementos (vendidos na loja)
    public static final Item REMEDIO = new Item(
            "Remédio", "Ajuda a recuperar a saúde rapidamente.", TipoItem.SUPLEMENTO, 50, 25);
    public static final Item VITAMINAS = new Item(
            "Vitaminas", "Fortalece o corpo e melhora a recuperação.", TipoItem.SUPLEMENTO, 40, 20);

    // Itens especiais (não são vendidos — o personagem escolhe pegar ou não)
    public static final Item CONTRATO = new Item(
            "Contrato", "Um contrato para se vender.", TipoItem.ESPECIAL, 0, 0);
    public static final Item LUVA_DO_PAI = new Item(
            "Luva do Pai", "A luva de boxe que pertenceu ao seu pai.", TipoItem.ESPECIAL, 0, 0);

    public static final Item[] ITENS_LOJA = {
            SALADA, BIFE, REFRIGERANTE, BARRA_CEREAL, REMEDIO, VITAMINAS
    };

    private Itens() {
    }
}
