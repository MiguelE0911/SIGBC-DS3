package com.bmw.cine.common.dto;

import java.time.LocalDateTime;

public class SolicitudBoletoDTO {

    private final int boletoId;
    private final String nombreUsuario;
    private final String tituloPelicula;
    private final LocalDateTime horarioFuncion;
    private final String nombreSala;
    private final String asientoCodigo;
    private final String estado; // "PENDIENTE" o "CONFIRMADO"
    private final LocalDateTime fechaSolicitud;

    public SolicitudBoletoDTO(int boletoId, String nombreUsuario, String tituloPelicula,
                              LocalDateTime horarioFuncion, String nombreSala, String asientoCodigo,
                              String estado, LocalDateTime fechaSolicitud) {
        this.boletoId = boletoId;
        this.nombreUsuario = nombreUsuario;
        this.tituloPelicula = tituloPelicula;
        this.horarioFuncion = horarioFuncion;
        this.nombreSala = nombreSala;
        this.asientoCodigo = asientoCodigo;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getBoletoId() {return boletoId;}
    public String getNombreUsuario() {return nombreUsuario;}
    public String getTituloPelicula() {return tituloPelicula;}
    public LocalDateTime getHorarioFuncion() {return horarioFuncion;}
    public String getNombreSala() {return nombreSala;}
    public String getAsientoCodigo() {return asientoCodigo;}
    public String getEstado() {return estado;}
    public LocalDateTime getFechaSolicitud() {return fechaSolicitud;}
}