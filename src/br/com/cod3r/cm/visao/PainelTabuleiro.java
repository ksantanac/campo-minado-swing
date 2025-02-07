package br.com.cod3r.cm.visao;

import br.com.cod3r.cm.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    // Construtor da classe PainelTabuleiro
    public PainelTabuleiro(Tabuleiro tabuleiro) {
        // Definindo o layout do painel com base nas linhas e colunas do tabuleiro
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        // Adicionando um botão para cada campo do tabuleiro
        tabuleiro.paraCada(c -> add(new BotaoCampo(c)));

        // Registrando um observador para verificar se o jogo foi ganho ou perdido
        tabuleiro.registrarObservador(e -> {

            // Atualizando a interface gráfica na thread de evento
            SwingUtilities.invokeLater(() -> {
                // Exibindo uma mensagem caso o jogador tenha ganhado
                if (e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "Ganhou :)");
                } else {
                    // Exibindo uma mensagem caso o jogador tenha perdido
                    JOptionPane.showMessageDialog(this, "Perdeu :(");
                }

                // Reiniciando o tabuleiro para uma nova partida
                tabuleiro.reiniciar();
            });
        });
    }
}
