package com.bmw.cine.common.dao;

import java.util.List;

import com.bmw.cine.common.dto.FiltroSolicitudDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;

public interface BoletoDAO {
    List<SolicitudBoletoDTO> listarSolicitudes(FiltroSolicitudDTO filtro);

    List<String> listarAsientosOcupados(int funcionId);
    int emitirConfirmado(int usuarioId, int funcionId, String asientoCodigo, int aprobadoPorUsuarioId);
    List<Integer> solicitarBoletos(int usuarioId, int funcionId, List<String> asientosCodigos);
    List<SolicitudBoletoDTO> listarPorUsuario(int usuarioId); //Agregue la lista para solicitud de boleto
    boolean aprobarSolicitud(int boletoId, int aprobadoPorUsuarioId);
    boolean rechazarSolicitud(int boletoId);
}