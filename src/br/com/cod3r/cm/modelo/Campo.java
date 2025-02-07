package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    // Definição das coordenadas do campo no tabuleiro
    private final int linha;
    private final int coluna;

    // Estado do campo
    private boolean aberto = false;
    private boolean minado = false;
    private boolean marcado = false;

    // Lista de vizinhos e observadores do campo
    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    // Construtor da classe Campo
    public Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    // Método para registrar um observador do campo
    public void registrarObservador(CampoObservador observador) {
        observadores.add(observador);
    }

    // Método para notificar todos os observadores sobre um evento
    private void notificarObservadores(CampoEvento evento) {
        observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
    }

    // Método para adicionar um vizinho ao campo (se for uma posição válida)
    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        // Vizinhos podem ser adicionados se estiverem ao lado ou na diagonal
        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    // Método para alternar a marcação do campo (bandeira)
    public void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;

            if (marcado) {
                notificarObservadores(CampoEvento.MARCAR);
            } else {
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    // Método para abrir o campo
    public boolean abrir() {
        if (!aberto && !marcado) {
            if (minado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);

            // Se a vizinhança for segura, abrir os vizinhos recursivamente
            if (vizinhacaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }

            return true;
        } else {
            return false;
        }
    }

    // Método para verificar se todos os vizinhos são seguros (não minados)
    public boolean vizinhacaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    // Método para minar o campo
    void minar() {
        minado = true;
    }

    // Métodos de acesso ao estado do campo
    public boolean isMinado() {
        return minado;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
        if (aberto) {
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    public boolean isAberto() {
        return !aberto;
    }

    public boolean isFechado() {
        return !isAberto();
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    // Verifica se o campo atingiu seu objetivo (aberto ou corretamente marcado)
    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    // Conta o número de minas na vizinhança
    public int minasNaVizinhanca() {
        return (int) vizinhos.stream().filter(v -> v.minado).count();
    }

    // Reinicia o campo para um novo jogo
    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }
}