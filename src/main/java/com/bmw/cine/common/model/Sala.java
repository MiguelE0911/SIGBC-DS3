package com.bmw.cine.common.model;

public class Sala {
    private int id;
    private String nombre;
    private int filas;
    private int columnas;

    public Sala() {}

    public Sala(int id, String nombre, int filas, int columnas) {
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public int getFilas() {return filas;}
    public void setFilas(int filas) {this.filas = filas;}

    public int getColumnas() {return columnas;}
    public void setColumnas(int columnas) {this.columnas = columnas;}

    public int getCapacidadTotal() {return filas * columnas;}
}