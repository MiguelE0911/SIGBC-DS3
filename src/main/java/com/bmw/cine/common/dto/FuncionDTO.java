package com.bmw.cine.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lo que ve el Espectador al elegir horario en el Detalle de Película:
 * une datos de Funcion + nombre de Sala + cuántos asientos quedan,
 * sin que la vista tenga que hacer 3 consultas ni conocer los IDs de sala.
 */

public class FuncionDTO {
    private final int funcionId;
    private final LocalDateTime horario;
    private final String nombreSala;
    private final BigDecimal precioBase;
    private final int asientosDisponibles;

    public FuncionDTO(int funcionId, LocalDateTime horario, String nombreSala,
                      BigDecimal precioBase, int asientosDisponibles) {
        this.funcionId = funcionId;
        this.horario = horario;
        this.nombreSala = nombreSala;
        this.precioBase = precioBase;
        this.asientosDisponibles = asientosDisponibles;
    }

    public int getFuncionId() {return funcionId;}
    public LocalDateTime getHorario() {return horario;}
    public String getNombreSala() {return nombreSala;}
    public BigDecimal getPrecioBase() {return precioBase;}
    public int getAsientosDisponibles() {return asientosDisponibles;}
}
