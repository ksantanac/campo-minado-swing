package br.com.cod3r.cm.visao;

import br.com.cod3r.cm.modelo.Campo;
import br.com.cod3r.cm.modelo.CampoEvento;
import br.com.cod3r.cm.modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Classe que representa um botão do campo no jogo Campo Minado.
 * Implementa CampoObservador para reagir a eventos do campo e MouseListener para interações do usuário.
 */
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

    // Cores padrão do botão
    private final Color BG_PADRAO = new Color(184, 184, 184); // Cor de fundo padrão
    private final Color BG_MARCAR = new Color(8, 179, 247);   // Cor de fundo quando marcado
    private final Color BG_EXPLODIR = new Color(189, 66, 68); // Cor de fundo quando explode
    private final Color TEXTO_VERDE = new Color(0, 100, 0);    // Cor do texto para 1 mina vizinha

    private Campo campo; // Referência ao campo correspondente

    /**
     * Construtor que inicializa o botão com as configurações padrão e registra observadores.
     *
     * @param campo Instância do campo associada ao botão
     */
    public BotaoCampo(Campo campo) {
        this.campo = campo;
        setBackground(BG_PADRAO); // Definindo o fundo com a cor padrão
        setOpaque(true); // Garantindo que o botão seja opaco
        setBorder(BorderFactory.createBevelBorder(0)); // Adicionando borda ao botão

        addMouseListener(this); // Adiciona o listener para eventos do mouse
        campo.registrarObservador(this); // Registra este botão como observador do campo
    }

    /**
     * Método chamado quando um evento ocorre no campo.
     *
     * @param campo  Campo onde o evento ocorreu
     * @param evento Tipo do evento ocorrido
     */
    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento) {
            case ABRIR:
                aplicarEstiloAbrir(); // Aplica estilo ao abrir o campo
                break;
            case MARCAR:
                aplicarEstiloMarcar(); // Aplica estilo ao marcar o campo
                break;
            case EXPLODIR:
                aplicarEstiloExplodir(); // Aplica estilo ao explodir o campo
                break;
            default:
                aplicarEstiloPadrao(); // Aplica o estilo padrão
        }

        // Atualiza a interface gráfica para refletir as mudanças
        SwingUtilities.invokeLater(() -> {
            repaint();  // Atualiza a renderização do botão
            validate();  // Valida a disposição dos componentes
        });
    }

    /**
     * Aplica o estilo padrão ao botão.
     */
    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO); // Cor de fundo padrão
        setBorder(BorderFactory.createBevelBorder(0)); // Remove borda
        setText(""); // Limpa o texto
    }

    /**
     * Aplica o estilo ao botão quando o campo explode (mina acionada).
     */
    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR); // Cor de fundo quando explode
        setForeground(Color.WHITE); // Cor do texto
        setText("X"); // Texto "X" indicando explosão
    }

    /**
     * Aplica o estilo ao botão quando ele é marcado pelo jogador.
     */
    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR); // Cor de fundo quando marcado
        setForeground(Color.BLACK); // Cor do texto
        setText("M"); // Texto "M" indicando marcação
    }

    /**
     * Aplica o estilo ao botão quando ele é aberto pelo jogador.
     */
    private void aplicarEstiloAbrir() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borda ao abrir

        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR); // Cor de fundo se o campo for minado
            return;
        }

        setBackground(BG_PADRAO); // Cor de fundo padrão

        // Define a cor do texto com base no número de minas vizinhas
        switch (campo.minasNaVizinhanca()) {
            case 1:
                setForeground(TEXTO_VERDE); // Texto verde para 1 mina vizinha
                break;
            case 2:
                setForeground(Color.BLUE); // Texto azul para 2 minas vizinhas
                break;
            case 3:
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED); // Texto vermelho para 4, 5 ou 6 minas vizinhas
                break;
            default:
                setForeground(Color.PINK); // Texto rosa para outras quantidades
        }

        // Define o texto do botão com o número de minas ao redor, se houver
        String valor = !campo.vizinhacaSegura() ? campo.minasNaVizinhanca() + "" : "";
        setText(valor); // Texto do botão com o número de minas vizinhas
    }

    // Implementação dos eventos do mouse

    /**
     * Método chamado quando o botão do mouse é pressionado sobre o botão.
     *
     * @param e Evento do mouse
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) { // Clique esquerdo
            campo.abrir(); // Abre o campo
        } else { // Clique direito
            campo.alternarMarcacao(); // Alterna a marcação do campo
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
