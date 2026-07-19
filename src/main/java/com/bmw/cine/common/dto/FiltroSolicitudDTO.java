package com.bmw.cine.common.dto;

/**
 * Filtros de la Bandeja de Solicitudes. Cualquier campo en null significa
 * "sin filtrar por esto". Usa Integer (no int) para que null sea válido.
 */
public class FiltroSolicitudDTO {

    private Integer peliculaId;
    private Integer funcionId;
    private String estado; // "PENDIENTE", "CONFIRMADO", o null = todos

    public FiltroSolicitudDTO() {}

    public Integer getPeliculaId() {return peliculaId;}
    public void setPeliculaId(Integer peliculaId) {this.peliculaId = peliculaId;}

    public Integer getFuncionId() {return funcionId;}
    public void setFuncionId(Integer funcionId) {this.funcionId = funcionId;}

    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}
}