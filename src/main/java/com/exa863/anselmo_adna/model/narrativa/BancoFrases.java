package com.exa863.anselmo_adna.model.narrativa;

public class BancoFrases {

    // indices da intensidade das frases
    public static final int INTENSIDADE_BAIXA = 0;
    public static final int INTENSIDADE_MEDIA = 1;
    public static final int INTENSIDADE_ALTA = 2;

    // frases de nocaute e fim de luta
    public static final String[] NOCAUTES = {
            "{adversario} VAI À LONA! {adversario} VAI À LONA! {adversario} VAI À LONA! Que marretada absurda de {atacante}!",
            "ELE CAIU! ACABOU! ACABOU! O juiz não deixa continuar! {atacante} apagou {adversario}!",
            "Acho que ouvi um 'No más'! {adversario} simplesmente desiste depois de uma pedrada de {atacante}!",
            "{atacante} ataca novamente! O homem mais temido do planeta enterra {adversario} na lona!",
            "Ele chocou o mundo! Eu choquei o mundo! {atacante} consegue um nocaute histórico em cima de {adversario}!",
            "A arena explode! {atacante} acerta a ponta do queixo e {adversario} vai de encontro ao chão. É O MAIOR DE TODOS!",
            "E {atacante} apaga a luz de {adversario}! Um cruzado brutal que encerra a luta instantaneamente!",
            "Doutor, pode trazer a maca! {atacante} acaba de mandar {adversario} para outra dimensão!",
            "Caiu igual uma árvore! {adversario} desaba no ringue após o direto demolidor de {atacante}!",
            "Nocaute brutal! O árbitro nem precisa abrir contagem, {atacante} despacha {adversario} para o mundo dos sonhos!",
            "{adversario} deita no tablado e não levanta mais! {atacante} consegue um nocaute espetacular!",
            "Um gancho que vai passar nos melhores momentos por anos! {atacante} oblitera {adversario}!",
            "É fim de jogo para {adversario}! {atacante} acha o botão de desligar e fatura por nocaute!",
            "Fica no chão! Fica no chão! O árbitro encerra enquanto {atacante} comemora um nocaute avassalador sobre {adversario}!",
            "{atacante} arranca a alma de {adversario} com esse upper! Nocaute fulminante!",
            "E a luta acaba de forma dramática! {atacante} decreta o fim com um golpe devastador em {adversario}!"
    };

    public static final String[] DECISOES_UNANIMES = {
            "TEMOS UM NOVO CAMPEÃO! Ou E AINDA É O CAMPEÃO! O que importa é que a vitória é clara e UNÂNIME para {atacante}!",
            "BING BING BING! Que verdadeira clínica de boxe aplicada por {atacante}, vitória merecida nas papeletas!",
            "Fim de papo na Batalha do Século particular deles! Domínio absoluto de {atacante}.",
            "Fim de luta! A contagem dos juízes é apenas uma formalidade, vitória claríssima de {atacante}!",
            "Domínio absoluto, do primeiro ao último sino. Vitória unânime e incontestável de {atacante}!",
            "Não sobrou pedra sobre pedra da estratégia de {adversario}. {atacante} vence na decisão unânime!",
            "Os três juízes viram a mesma coisa: um passeio completo de {atacante} sobre {adversario}!",
            "Vitória unânime para {atacante}! Uma performance de gala que não deixou margem para dúvidas!",
            "Uma aula magna! {atacante} ditou o ritmo todos os rounds e leva por decisão unânime!",
            "E nas papeletas, nenhuma surpresa! {atacante} consagra-se vencedor de forma unânime e soberana!",
            "Vitória limpa e cristalina de {atacante}! Todos os árbitros concordam com a superioridade demonstrada hoje.",
            "{adversario} tentou, mas hoje o ringue era de {atacante}. Decisão unânime confirmada!",
            "Consenso na mesa dos juízes! Vitória unânime para {atacante}, que lutou com perfeição do início ao fim!"
    };

    public static final String[] DECISOES_DIVIDIDAS = {
            "Foi no detalhe! A arena inteira dividida, mas os juízes viram a vitória de {atacante} sobre {adversario}!",
            "Uma das lutas mais parelhas do ano! Por decisão dividida, o braço erguido é o de {atacante}!",
            "Essa poderia ir para qualquer um, mas {atacante} sobrevive e vence na Decisão Dividida!",
            "Que luta parelha! O ginásio prende a respiração... e {atacante} leva na decisão dividida!",
            "Nas papeletas, um resultado apertado! Dois juízes deram a vitória para {atacante} contra um de {adversario}!",
            "Uma guerra de estilos que terminou na prancheta. Vitória suada e dividida para {atacante}!",
            "O público vaia, o público aplaude! Por decisão dividida, o vencedor da noite é {atacante}!",
            "Metade do ginásio acha que {adversario} ganhou, mas os juízes decretam: vitória dividida de {atacante}!",
            "Ninguém queria estar na pele dos juízes hoje! Após muita contagem, {atacante} triunfa por decisão dividida!",
            "Foi um verdadeiro jogo de xadrez e a diferença foi mínima. Decisão dividida a favor de {atacante}!",
            "Haja coração na hora da leitura dos cartões! E por 2 a 1, a vitória vai para {atacante}!",
            "{adversario} balança a cabeça em discordância, mas a decisão dividida premia o esforço de {atacante}!",
            "Um embate que poderia ter ido para qualquer lado. No fim, a matemática sorri para {atacante} em decisão dividida!"
    };

    public static final String[] EMPATES = {
            "ACABOU! ACABOU A LUTA! Que guerra insana! Os juízes declaram EMPATE MAJORITÁRIO entre {atacante} e {adversario}!",
            "Inacreditável! Ninguém leva a melhor hoje! Temos um EMPATE TÉCNICO no ginásio!",
            "Os deuses do boxe não quiseram escolher um vencedor! Um empate épico!"
    };

    // frases de eventos separados por intensidade

    public static final String[][] DANO_BRUTO = {
            // baixa
            {
                    "{atacante} tenta um golpe forte, mas escorrega e apenas empurra {adversario}.",
                    "Um cruzado telegrafado de {atacante} que morre na luva de {adversario} sem muito perigo."
            },
            // media
            {
                    "{atacante} joga o peso do corpo e acerta {adversario}, que balança mas continua inteiro.",
                    "{atacante} encontra um espaço na guarda e conecta um sólido cruzado no peito de {adversario}.",
                    "{atacante} encurta a distância e acerta um upper que levanta a cabeça de {adversario}.",
                    "Um, dois... {atacante} pontua com golpes secos e bem colocados contra {adversario}."
            },
            // alta
            {
                    "Um direto de direita! E {adversario} sentiu! {atacante} está farejando sangue!",
                    "{adversario} está nas cordas! Não tem para onde ir! {atacante} descarrega uma chuva de pedradas!",
                    "De onde {atacante} tirou forças para esse golpe? O protetor bucal de {adversario} quase voou!",
                    "A mão pesada de {atacante} entra rasgando a guarda de {adversario}! Deu pra ouvir o estalo daqui!",
                    "O corpo de {adversario} quer parar, mas {atacante} não perdoa com um gancho destruidor nas costelas!"
            }
    };

    public static final String[][] DANO_RAPIDO = {
            // baixa
            {
                    "{atacante} tenta uma combinação rápida, mas falta energia e {adversario} afasta com facilidade.",
                    "Faltou velocidade para {atacante}... O combo parou na defesa de {adversario}."
            },
            // media
            {
                    "{atacante} solta um jab rápido de estudo, testando os reflexos de {adversario}.",
                    "Os dois se estudam no centro... {atacante} arrisca um-dois que raspa o ombro de {adversario}.",
                    "{atacante} pontua de longe, tocando repetidamente a guarda de {adversario} com a mão esquerda."
            },
            // alta
            {
                    "Mas que velocidade! {atacante} solta um combo de três golpes antes mesmo de {adversario} piscar!",
                    "{atacante} parece uma metralhadora! Chove socos rápidos furando a guarda de {adversario}!",
                    "Um jab na velocidade da luz! {atacante} estala o rosto de {adversario} sem dar chance de defesa!",
                    "Que combinação perfeita! {atacante} dança e bate, castigando o rosto de {adversario} repetidas vezes!"
            }
    };

    public static final String[][] ESQUIVAS = {
            // baixa
            {
                    "{atacante} tenta esquivar, mas toma o golpe de raspão de {adversario}.",
                    "Desengonçado, {atacante} quase cai ao tentar fugir do ataque de {adversario}."
            },
            // media
            {
                    "{atacante} recua a tempo, apenas tirando o corpo do jab lançado por {adversario}.",
                    "Um passo meio travado, mas {atacante} consegue evitar o golpe de {adversario} no puro susto.",
                    "{atacante} levanta a guarda fechada e absorve bem a ofensiva simples de {adversario}.",
                    "Eles entram na curta distância e {atacante} clincha estrategicamente, amarrando a luta e travando {adversario}."
            },
            // alta
            {
                    "Voa como borboleta, ferroa como abelha! {atacante} zomba da lentidão de {adversario}!",
                    "QUE REFLEXO! {atacante} balança o corpo como um fantasma e deixa o cruzado de {adversario} passar no vazio!",
                    "{atacante} encosta nas cordas usando a clássica estratégia do rope-a-dope, frustrando completamente {adversario}!",
                    "Matrix no ringue! {atacante} faz um pêndulo perfeito no último milissegundo, enquanto {adversario} golpeia o ar!",
                    "A arena inteira de pé para aplaudir a esquiva absurda de {atacante} contra a investida de {adversario}!"
            }
    };

    // comentarios aleatorios do narrador e torcida
    public static final String[] COMENTARIOS_ALEATORIOS = {
            "Rapaz, se esse soco pega na orelha, o cara só escuta rádio AM pelo resto da vida! Não é mesmo, comentarista?",
            "Olha o jeito que ele esquivou, meus amigos! O maluco acha que tá na Matrix, só pode!",
            "Você que está acompanhando pelo canal Combate, não pisque! A torcida VIP até derrubou o champanhe depois dessa!",
            "Dá pra ouvir o estalo do impacto daqui da cabine! E olha que o nosso vidro é à prova de som, hein?!",
            "Como diria a lenda Mike Tyson: 'Todo mundo tem um plano, até tomar o primeiro soco na cara!'",
            "O técnico no córner tá gritando tanto que já perdeu a voz. Daqui a pouco vai ter que passar instruções por mímica pro coitado!",
            "Isso me lembra o grande Rocky Balboa: 'Não importa o quão forte você bate, mas sim o quanto aguenta apanhar e continuar lutando!'",
            "Esse aí tem o queixo de granito, campeão! Tomou uma verdadeira marretada e nem piscou!",
            "Um jogo de pernas tão liso que parece até que ele tá dançando no ringue. Uma aula de movimentação para quem está assistindo!",
            "Como dizia o mestre Muhammad Ali: 'Suas mãos não podem bater naquilo que seus olhos não veem!' Que esquiva absurda!",
            "Certeza que o nariz dele entortou um pouquinho agora. Vai ter que respirar por Wi-Fi depois dessa luta!",
            "O árbitro chegou mais perto ali... Aposto com vocês que ele tá perguntando que dia é hoje pra ver se o cara ainda tá consciente.",
            "Reparem como a base das pernas está sempre alinhada. É puro fundamento de boxe sendo mostrado ao vivo pra vocês!",
            "Ele tenta fintar com o ombro, uma artimanha clássica muito usada pelos grandes de antigamente. Bela leitura de luta!",
            "A movimentação lateral dele é um absurdo! O cara é um fantasma no ringue, o que você acha disso, parceiro?",
            "Como diz o velho ditado dos ringues: 'O negócio aqui é ir pro pau!' A trocação ficou franca, meus amigos!",
            "Ele faz a leitura de alcance usando a mão da frente como um radar. Muito inteligente taticamente, meus amigos do boxe!",
            "Presta atenção: se o protetor bucal voar, alguém da terceira fileira vai ganhar um souvenir bizarro hoje à noite!",
            "A energia da arena é contagiante! Tem gente até de pé roendo as unhas de nervoso! Que clima de Copa do Mundo!",
            "Ele soltou o ar pela boca no impacto! Essa é a técnica milenar para não esvaziar o tanque de gás, muito bem lembrado pelo nosso comentarista!",
            "Eu nunca vi tanta raça em cima de um ringue! Parece que estou vendo a reencarnação do 'Thrilla in Manila' hoje!"
    };
}