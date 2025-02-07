package br.com.cod3r.cm.modelo;

// Enumeração que representa os eventos possíveis em um campo
public enum CampoEvento {

    ABRIR,       // Evento para abertura de um campo
    MARCAR,      // Evento para marcar um campo com bandeira
    DESMARCAR,   // Evento para desmarcar um campo previamente marcado
    EXPLODIR,    // Evento para indicar que um campo minado foi aberto
    REINICIAR    // Evento para reiniciar o estado do campo
}
