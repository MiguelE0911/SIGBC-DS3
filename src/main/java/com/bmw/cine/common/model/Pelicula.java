package com.bmw.cine.common.model;

public class Pelicula {
    private int id;
    private String titulo;
    private String sinopsis;
    private String genero;
    private int duracionMinutos;
    private String rutaPoster;     // ruta o URL de la imagen del póster
    private boolean activa;       // false = fuera de cartelera, sin borrar el registro

    public Pelicula() {}

    public Pelicula(int id, String titulo, String sinopsis, String genero,
                    int duracionMinutos, String rutaPoster, boolean activa) {
        this.id = id;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.rutaPoster = rutaPoster;
        this.activa = activa;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getSinopsis() {return sinopsis;}
    public void setSinopsis(String sinopsis) {this.sinopsis = sinopsis;}

    public String getGenero() {return genero;}
    public void setGenero(String genero) {this.genero = genero;}

    public int getDuracionMinutos() {return duracionMinutos;}
    public void setDuracionMinutos(int duracionMinutos) {this.duracionMinutos = duracionMinutos;}

    public String getPosterUrl() {return rutaPoster;}
    public void setPosterUrl(String rutaPoster) {this.rutaPoster = rutaPoster;}

    public boolean isActiva() {return activa;}
    public void setActiva(boolean activa) {this.activa = activa;}
}
