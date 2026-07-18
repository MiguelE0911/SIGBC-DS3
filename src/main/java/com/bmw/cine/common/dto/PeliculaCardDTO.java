package com.bmw.cine.common.dto;

/**
 * Lo mínimo que necesita una tarjeta de Cartelera para pintarse.
 * No expone sinopsis completa ni el flag "activa" — eso es detalle
 * de administración, no de la vista de Espectador.
 */

public class PeliculaCardDTO {
    private final int peliculaId;
    private final String titulo;
    private final String rutaPoster;
    private final String genero;
    private final int duracionMinutos;

    public PeliculaCardDTO(int peliculaId, String titulo, String rutaPoster,
                           String genero, int duracionMinutos) {
        this.peliculaId = peliculaId;
        this.titulo = titulo;
        this.rutaPoster = rutaPoster;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
    }

    public int getPeliculaId() {return peliculaId;}
    public String getTitulo() {return titulo;}
    public String getRutaPoster() {return rutaPoster;}
    public String getGenero() {return genero;}
    public int getDuracionMinutos() {return duracionMinutos;}
}
