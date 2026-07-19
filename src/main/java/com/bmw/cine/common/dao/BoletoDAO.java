package com.bmw.cine.common.dao;

import com.bmw.cine.common.dto.FiltroSolicitudDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;

import java.util.List;

/**
 * Contrato de acceso a datos para Boleto, enfocado en la Bandeja de
 * Solicitudes de Taquilla (Meta 2). La compra desde el lado Espectador
 * (creación de boletos PENDIENTE) va en un método aparte cuando se
 * construya ese flujo — aquí solo lo que necesita Personal.
 */

public interface BoletoDAO {

    /** Lista de solicitudes según los filtros dados (ver FiltroSolicitudDTO). */
    List<SolicitudBoletoDTO> listarSolicitudes(FiltroSolicitudDTO filtro);

    List<String> listarAsientosOcupados(int funcionId);
    int emitirConfirmado(int usuarioId, int funcionId, String asientoCodigo, int aprobadoPorUsuarioId);

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