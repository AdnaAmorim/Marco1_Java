package com.exa863.anselmo_adna.persistencia;

/**
 * Armazena os identificadores das conquistas disponíveis no jogo.
 * Também fornece os nomes das conquistas que são exibidos para o jogador.
 *
 * @author Anselmo e Adna
 */
public class Conquistas {

    public static final String PRIMEIROS_PASSOS = "PRIMEIROS_PASSOS";
    public static final String DESTAQUE_ESTADUAL = "DESTAQUE_ESTADUAL";
    public static final String NOME_NACIONAL = "NOME_NACIONAL";
    public static final String CORACAO_DE_LUTADOR = "CORACAO_DE_LUTADOR";
    public static final String INVESTIGADOR = "INVESTIGADOR";
    public static final String CHEGUEI_AO_MUNDIAL = "CHEGUEI_AO_MUNDIAL";
    public static final String CAMPEAO_DO_MUNDO = "CAMPEAO_DO_MUNDO";
    public static final String VERDADEIRO_LEGADO = "VERDADEIRO_LEGADO";
    public static final String MULTIPLOS_DESTINOS = "MULTIPLOS_DESTINOS";
    public static final String TODOS_OS_CAMINHOS = "TODOS_OS_CAMINHOS";

    /**
     * Retorna o nome da conquista que será mostrado para o jogador.
     *
     * @param conquista Identificador da conquista.
     * @return Nome da conquista para exibição.
     */
    public static String getNome(String conquista) {

        return switch (conquista) {

            case PRIMEIROS_PASSOS ->
                    "Primeiros Passos";

            case DESTAQUE_ESTADUAL ->
                    "Destaque Estadual";

            case NOME_NACIONAL ->
                    "Nome Nacional";

            case CORACAO_DE_LUTADOR ->
                    "Coração de Lutador";

            case INVESTIGADOR ->
                    "Investigador";

            case CHEGUEI_AO_MUNDIAL ->
                    "Cheguei ao Mundial";

            case CAMPEAO_DO_MUNDO ->
                    "Campeão do Mundo";

            case VERDADEIRO_LEGADO ->
                    "Verdadeiro Legado";

            case MULTIPLOS_DESTINOS ->
                    "Múltiplos Destinos";

            case TODOS_OS_CAMINHOS ->
                    "Todos os Caminhos";

            default ->
                    conquista;
        };
    }
}