package com.bmw.cine.common.dao.impl;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.db.Conexion;
import com.bmw.cine.common.dto.FuncionDTO;
import com.bmw.cine.common.model.Funcion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionDAOImpl implements FuncionDAO {

    private static final String TABLA = "funcion";

    @Override
    public List<FuncionDTO> listarPorPelicula(int peliculaId) {
        // Asientos disponibles = capacidad de la sala - boletos ya tomados
        // (PENDIENTE también reserva el asiento por el UNIQUE(funcion_id, asiento_codigo)).
        String sql = "SELECT f.id, f.horario, s.nombre AS nombre_sala, f.precio_base, "
                + "(s.filas * s.columnas - COUNT(b.id)) AS disponibles "
                + "FROM funcion f "
                + "JOIN sala s ON f.sala_id = s.id "
                + "LEFT JOIN boleto b ON b.funcion_id = f.id "
                + "WHERE f.pelicula_id = ? "
                + "GROUP BY f.id, f.horario, s.nombre, f.precio_base, s.filas, s.columnas "
                + "ORDER BY f.horario";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, peliculaId);
            List<FuncionDTO> funciones = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    funciones.add(new FuncionDTO(
                            rs.getInt("id"),
                            rs.getTimestamp("horario").toLocalDateTime(),
                            rs.getString("nombre_sala"),
                            rs.getBigDecimal("precio_base"),
                            rs.getInt("disponibles")
                    ));
                }
            }
            return funciones;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las funciones de la película " + peliculaId, e);
        }
    }

    @Override
    public Optional<Funcion> buscarPorId(int id) {
        String sql = "SELECT id, pelicula_id, sala_id, horario, precio_base FROM " + TABLA + " WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar la función con id " + id, e);
        }
    }

    @Override
    public List<String> listarAsientosOcupados(int funcionId) {
        // Cuenta PENDIENTE y CONFIRMADO por igual: un asiento solicitado
        // (aunque no esté aprobado todavía) ya está reservado por el UNIQUE.
        String sql = "SELECT asiento_codigo FROM boleto WHERE funcion_id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, funcionId);
            List<String> ocupados = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ocupados.add(rs.getString("asiento_codigo"));
                }
            }
            return ocupados;
        } catch (SQLException e) {
            throw new DAOException("Error al listar los asientos ocupados de la función " + funcionId, e);
        }
    }

    @Override
    public List<Funcion> listarTodas() {
        String sql = "SELECT id, pelicula_id, sala_id, horario, precio_base FROM " + TABLA + " ORDER BY horario";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Funcion> funciones = new ArrayList<>();
            while (rs.next()) {
                funciones.add(mapearFila(rs));
            }
            return funciones;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las funciones", e);
        }
    }

    @Override
    public Funcion crear(Funcion funcion) {
        String sql = "INSERT INTO " + TABLA + " (pelicula_id, sala_id, horario, precio_base) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, funcion.getPeliculaId());
            ps.setInt(2, funcion.getSalaId());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(funcion.horario()));
            ps.setBigDecimal(4, funcion.precioBase());

            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    funcion.setId(claves.getInt(1));
                }
            }
            return funcion;
        } catch (SQLException e) {
            throw new DAOException("Error al crear la función", e);
        }
    }

    @Override
    public boolean actualizar(Funcion funcion) {
        String sql = "UPDATE " + TABLA + " SET pelicula_id = ?, sala_id = ?, horario = ?, precio_base = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, funcion.getPeliculaId());
            ps.setInt(2, funcion.getSalaId());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(funcion.horario()));
            ps.setBigDecimal(4, funcion.precioBase());
            ps.setInt(5, funcion.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar la función " + funcion.getId(), e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar la función " + id, e);
        }
    }

    private Funcion mapearFila(ResultSet rs) throws SQLException {
        return new Funcion(
                rs.getInt("id"),
                rs.getInt("pelicula_id"),
                rs.getInt("sala_id"),
                rs.getTimestamp("horario").toLocalDateTime(),
                rs.getBigDecimal("precio_base")
        );
    }

    @Override
    public int[] obtenerDimensionesSala(int funcionId) throws DAOException {
        String sql = "SELECT s.filas, s.columnas FROM funcion f " +
                "JOIN sala s ON s.id = f.sala_id WHERE f.id = ?";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, funcionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new int[]{ rs.getInt("filas"), rs.getInt("columnas") };
                throw new DAOException("Función no encontrada: " + funcionId);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al obtener dimensiones de sala", ex);
        }
    }
}