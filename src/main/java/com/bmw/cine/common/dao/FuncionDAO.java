package com.bmw.cine.common.dao;

import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.model.Funcion;

import java.util.List;
import java.util.Optional;

public interface FuncionDAO {
    /*
     * Horarios disponibles de una película, ya con nombre de sala y
     * asientos disponibles resueltos
     */
    List<FuncionDTO> listarPorPelicula(int peliculaId);

    Optional<Funcion> buscarPorId(int id);
    List<String> listarAsientosOcupados(int funcionId); // Para la vista de Selección de asiento: qué códigos ya están ocupados.
    List<Funcion> listarTodas(); // Gestión de horarios (Personal).
    Funcion crear(Funcion funcion);
    boolean actualizar(Funcion funcion);
    boolean eliminar(int id);

    /** @return {filas, columnas} de la sala asociada a la función */
    int[] obtenerDimensionesSala(int funcionId) throws DAOException;
    boolean existeSolapamiento(int salaId, java.time.LocalDateTime inicio,
                               java.time.LocalDateTime fin, Integer excluirFuncionId);
}
