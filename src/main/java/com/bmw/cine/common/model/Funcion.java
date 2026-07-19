package com.bmw.cine.common.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Funcion {
    private int id;
    private int peliculaId;
    private int salaId;
    private LocalDateTime horario;
    private BigDecimal precioBase;

    public Funcion() {}

    public Funcion(int id, int peliculaId, int salaId, LocalDateTime horario, BigDecimal precioBase) {
        this.id = id;
        this.peliculaId = peliculaId;
        this.salaId = salaId;
        this.horario = horario;
        this.precioBase = precioBase;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getPeliculaId() {return peliculaId;}
    public void setPeliculaId(int peliculaId) {this.peliculaId = peliculaId;}

    public int getSalaId() {return salaId;}
    public void setSalaId(int salaId) {this.salaId = salaId;}

    public LocalDateTime horario() {return horario;}

    public LocalDateTime getHorario() {return horario;}
    public void setHorario(LocalDateTime horario) {this.horario = horario;}

    public BigDecimal precioBase() {return precioBase;}
    public void setPrecioBase(BigDecimal precioBase) {this.precioBase = precioBase;}
}
