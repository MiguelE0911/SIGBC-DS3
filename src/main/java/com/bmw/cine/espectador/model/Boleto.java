package com.bmw.cine.espectador.model;

import java.time.LocalDateTime;

public class Boleto {

    public enum EstadoBoleto {PENDIENTE, CONFIRMADO}
    private int idCompra;
    private String pelicula;
    private String asiento;
    private EstadoBoleto estado;
    private LocalDateTime fecha;

    /**
     * Constructor completo para la creación de un objeto Boleto.
     * 
     * @param idCompra Identificador único de la transacción.
     * @param pelicula Nombre o título de la película.
     * @param asiento  Código del asiento seleccionado (ej: A1).
     * @param estado   Estado actual (PENDIENTE o CONFIRMADO).
     * @param fecha    Fecha y hora de la función.
     */
    public Boleto(int idCompra, String pelicula, String asiento, EstadoBoleto estado, LocalDateTime fecha) {
        this.idCompra = idCompra;
        this.pelicula = pelicula;
        this.asiento = asiento;
        this.estado = estado;
        this.fecha = fecha;
    }

    //  Getters y Setters

    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public String getPelicula() { return pelicula; }
    public void setPelicula(String pelicula) { this.pelicula = pelicula; }

    public String getAsiento() { return asiento; }
    public void setAsiento(String asiento) { this.asiento = asiento; }

    public EstadoBoleto getEstado() { return estado; }
    public void setEstado(EstadoBoleto estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstadoTexto() {return estado.toString();} // Método auxiliar para obtener el nombre del estado en texto.
}