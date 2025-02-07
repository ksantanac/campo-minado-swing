package br.com.cod3r.cm.visao;

import br.com.cod3r.cm.modelo.Tabuleiro;

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    // Construtor da classe TelaPrincipal
    public TelaPrincipal() {

        // Criando um tabuleiro de Campo Minado com 16 linhas, 30 colunas e 50 minas
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);

        // Adicionando o painel de tabuleiro à janela principal
        add(new PainelTabuleiro(tabuleiro));

        // Definindo o título da janela
        setTitle("Campo Minado");

        // Definindo o tamanho da janela (largura x altura)
        setSize(690, 438);

        // Centralizando a janela na tela
        setLocationRelativeTo(null);

        // Definindo o comportamento quando a janela for fechada (DISPOSE_ON_CLOSE fecha a janela sem terminar o programa)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Tornando a janela visível
        setVisible(true);
    }

    // Método principal para iniciar o programa
    public static void main(String[] args) {

        // Criando uma nova instância da tela principal
        new TelaPrincipal();
    }

}
