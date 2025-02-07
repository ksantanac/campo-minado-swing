package br.com.cod3r.cm.modelo;

// Interface funcional para observar eventos em um campo
@FunctionalInterface
public interface CampoObservador {

    /**
     * Método chamado quando um evento ocorre em um campo.
     * param campo Campo onde o evento aconteceu
     * param evento Tipo de evento ocorrido
     */
    void eventoOcorreu(Campo campo, CampoEvento evento);
}
