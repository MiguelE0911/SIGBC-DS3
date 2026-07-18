package com.bmw.cine.common.dao;

/**
 * Excepción para errores de acceso a datos. Las implementaciones en
 * dao.impl deben capturar SQLException y relanzarla envuelta en esta,
 * así ni los controllers ni las vistas necesitan conocer JDBC.
 */

public class DAOException extends RuntimeException{
    public DAOException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public DAOException(String mensaje) {
        super(mensaje);
    }
}
