package com.bmw.cine.common.dao;

import java.util.List;

import com.bmw.cine.common.dto.FiltroSolicitudDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;


/**
 * Contrato de acceso a datos para Boleto, enfocado en la Bandeja de
 * Solicitudes de Taquilla (Meta 2) y en la compra desde el lado
 * Espectador (Meta 6).
 */

public interface BoletoDAO {

    /** Lista de solicitudes según los filtros dados (ver FiltroSolicitudDTO). */
    List<SolicitudBoletoDTO> listarSolicitudes(FiltroSolicitudDTO filtro);

    List<String> listarAsientosOcupados(int funcionId);
    int emitirConfirmado(int usuarioId, int funcionId, String asientoCodigo, int aprobadoPorUsuarioId);

    /**
     * Solicita la compra de boletos desde el lado Espectador. Crea una
     * fila por cada asiento en estado PENDIENTE, a la espera de
     * aprobación en Taquilla.
     * <p>
     * Debe validar que ninguno de los asientos ya esté ocupado para esa
     * función (a nivel de BD, idealmente con una restricción UNIQUE
     * sobre (funcion_id, asiento_codigo)). Si algún asiento ya no está
     * disponible, la implementación debe lanzar una excepción no
     * chequeada (ej. IllegalStateException) para que el llamador pueda
     * detectar el conflicto y refrescar el mapa de asientos.
     *
     * @param usuarioId        id del usuario que realiza la compra
     * @param funcionId        id de la función
     * @param asientosCodigos  códigos de asiento a reservar (ej. "A1", "B3")
     * @return lista de IDs de boleto generados, uno por cada asiento
     * @throws IllegalStateException si alguno de los asientos ya está ocupado
     */
    List<Integer> solicitarBoletos(int usuarioId, int funcionId, List<String> asientosCodigos);
    List<SolicitudBoletoDTO> listarPorUsuario(int usuarioId); //Agregue la lista para solicitud de boleto
    /**
     * Aprueba una solicitud PENDIENTE: la marca CONFIRMADO y registra
     * quién la aprobó. Devuelve false si el boleto ya no estaba
     * PENDIENTE (por ejemplo, otro miembro del staff ya la procesó).
     */
    boolean aprobarSolicitud(int boletoId, int aprobadoPorUsuarioId);

    /**
     * Rechaza una solicitud PENDIENTE. IMPORTANTE: como el estado solo
     * admite PENDIENTE/CONFIRMADO (no hay "RECHAZADO" en el esquema),
     * rechazar BORRA la fila del boleto — así el asiento queda libre de
     * inmediato para que otro lo pida.
     */
    boolean rechazarSolicitud(int boletoId);
}