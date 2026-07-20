package com.bmw.cine.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
