package com.bmw.cine.common.session;

public class OpcionModulo {
    private final String icono;  // emoji o glifo simple
    private final String titulo;     // ej: "Cartelera"
    private final String descripcion; // ej: "Compra boletos para ti mismo"
    private final Runnable accion;   // qué hacer al presionar "Entrar"

    public OpcionModulo(String icono, String titulo, String descripcion, Runnable accion) {
        this.icono = icono;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.accion = accion;
    }

    public String getIcono() {return icono;}
    public String getTitulo() {return titulo;}
    public String getDescripcion() {return descripcion;}
    public Runnable getAccion() {return accion;}
}
