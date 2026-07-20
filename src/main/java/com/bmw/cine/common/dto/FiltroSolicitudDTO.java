package com.bmw.cine.common.dto;

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