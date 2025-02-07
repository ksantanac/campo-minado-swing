package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Classe que representa o tabuleiro do jogo Campo Minado
public class Tabuleiro implements CampoObservador {

    private final int linhas;
    private final int colunas;
    private final int minas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    /**
     * Construtor do tabuleiro
     * @param linhas número de linhas do tabuleiro
     * @param colunas número de colunas do tabuleiro
     * @param minas número de minas espalhadas no tabuleiro
     */
    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarOsVizinhos();
        sortearMinas();
    }

    /**
     * Aplica uma função a cada campo do tabuleiro
     * @param funcao função a ser aplicada
     */
    public void paraCada(Consumer<Campo> funcao) {
        campos.forEach(funcao);
    }

    /**
     * Registra um observador para eventos do jogo
     * @param observador função que recebe o resultado do jogo
     */
    public void registrarObservador(Consumer<ResultadoEvento> observador) {
        observadores.add(observador);
    }

    /**
     * Notifica os observadores sobre o resultado do jogo
     * @param resultado true se venceu, false se perdeu
     */
    private void notificarObservadores(boolean resultado) {
        observadores.forEach(o -> o.accept(new ResultadoEvento(resultado)));
    }

    /**
     * Abre um campo específico no tabuleiro
     * @param linha linha do campo
     * @param coluna coluna do campo
     */
    public void abrir(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::abrir);
    }

    /**
     * Alterna a marcação (bandeira) de um campo específico
     * @param linha linha do campo
     * @param coluna coluna do campo
     */
    public void alternarMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }

    /**
     * Gera os campos do tabuleiro
     */
    private void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                Campo campo = new Campo(linha, coluna);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }
    }

    /**
     * Associa os vizinhos de cada campo do tabuleiro
     */
    private void associarOsVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    /**
     * Sorteia aleatoriamente as minas no tabuleiro
     */
    private void sortearMinas() {
        long minasArmadas;
        Predicate<Campo> minado = Campo::isMinado;

        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();

            minasArmadas = campos.stream().filter(minado).count();
        } while (minasArmadas < minas);
    }

    /**
     * Verifica se o objetivo do jogo foi alcançado (se o jogador venceu)
     * @return true se o jogador venceu, false caso contrário
     */
    public boolean objetivoAlcancado() {
        return campos.stream().allMatch(Campo::objetivoAlcancado);
    }

    /**
     * Reinicia o tabuleiro para um novo jogo
     */
    public void reiniciar() {
        campos.forEach(Campo::reiniciar);
        sortearMinas();
    }

    /**
     * Obtém o número de colunas do tabuleiro
     * @return número de colunas
     */
    public int getColunas() {
        return colunas;
    }

    /**
     * Obtém o número de linhas do tabuleiro
     * @return número de linhas
     */
    public int getLinhas() {
        return linhas;
    }

    /**
     * Trata eventos ocorridos em um campo do tabuleiro
     * @param campo campo onde ocorreu o evento
     * @param evento tipo de evento ocorrido
     */
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR) {
            mostrarMinas();
            notificarObservadores(false);
        } else if (objetivoAlcancado()) {
            notificarObservadores(true);
        }
    }

    /**
     * Revela todas as minas do tabuleiro quando o jogador perde
     */
    private void mostrarMinas() {
        campos.stream()
                .filter(Campo::isMinado)
                .filter(c -> !c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }
}
