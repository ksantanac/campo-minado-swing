package br.com.cod3r.cm.modelo;

// Classe que representa o resultado de um evento no jogo
public class ResultadoEvento {

    private final boolean ganhou; // Indica se o jogador ganhou o jogo

    /**
     * Construtor que inicializa o estado do resultado do jogo
     * param ganhou true se o jogador venceu, false caso contrário
     */
    public ResultadoEvento(boolean ganhou) {
        this.ganhou = ganhou;
    }

    /**
     * Método para verificar se o jogador ganhou o jogo
     * return true se venceu, false se perdeu
     */
    public boolean isGanhou() {
        return ganhou;
    }
}
