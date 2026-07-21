package com.bmw.cine.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.db.Conexion;
import com.bmw.cine.common.db.PgErrores;
import com.bmw.cine.common.dto.PeliculaCardDTO;
import com.bmw.cine.common.model.Pelicula;

public class PeliculaDAOImpl implements PeliculaDAO {

    private static final String TABLA = "pelicula";

    @Override
    public List<PeliculaCardDTO> listarCartelera() {
        String sql = "SELECT id, titulo, ruta_poster, genero, duracion_minutos "
                + "FROM " + TABLA + " WHERE visible = TRUE ORDER BY titulo";

        List<PeliculaCardDTO> tarjetas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tarjetas.add(new PeliculaCardDTO(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("ruta_poster"),
                        rs.getString("genero"),
                        rs.getInt("duracion_minutos")
                ));
            }
            return tarjetas;
        } catch (SQLException e) {
            throw new DAOException("Error al listar la cartelera", e);
        }
    }

    @Override
    public Optional<Pelicula> buscarPorId(int id) {
        String sql = "SELECT id, titulo, sinopsis, genero, duracion_minutos, ruta_poster, visible "
                + "FROM " + TABLA + " WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar la película con id " + id, e);
        }
    }

    @Override
    public List<Pelicula> listarTodas() {
        String sql = "SELECT id, titulo, sinopsis, genero, duracion_minutos, ruta_poster, visible "
                + "FROM " + TABLA + " ORDER BY titulo";

        List<Pelicula> peliculas = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                peliculas.add(mapearFila(rs));
            }
            return peliculas;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las películas", e);
        }
    }

    @Override
    public Pelicula crear(Pelicula pelicula) {
        String sql = "INSERT INTO " + TABLA
                + " (titulo, sinopsis, genero, duracion_minutos, ruta_poster, visible) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getSinopsis());
            ps.setString(3, pelicula.getGenero());
            ps.setInt(4, pelicula.getDuracionMinutos());
            ps.setString(5, pelicula.getRutaPoster());
            ps.setBoolean(6, pelicula.isActiva());

            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    pelicula.setId(claves.getInt(1));
                }
            }
            return pelicula;
        } catch (SQLException e) {
            throw new DAOException("Error al crear la película " + pelicula.getTitulo(), e);
        }
    }

    @Override
    public boolean actualizar(Pelicula pelicula) {
        String sql = "UPDATE " + TABLA
                + " SET titulo = ?, sinopsis = ?, genero = ?, duracion_minutos = ?, ruta_poster = ? "
                + "WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getSinopsis());
            ps.setString(3, pelicula.getGenero());
            ps.setInt(4, pelicula.getDuracionMinutos());
            ps.setString(5, pelicula.getRutaPoster());
            ps.setInt(6, pelicula.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar la película " + pelicula.getId(), e);
        }
    }

    @Override
    public boolean actualizarVisibilidad(int id, boolean visible) {
        String sql = "UPDATE " + TABLA + " SET visible = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, visible);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar la visibilidad de la película " + id, e);
        }
    }

    private Pelicula mapearFila(ResultSet rs) throws SQLException {
        return new Pelicula(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("sinopsis"),
                rs.getString("genero"),
                rs.getInt("duracion_minutos"),
                rs.getString("ruta_poster"),
                rs.getBoolean("visible")
        );
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id = ?";
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (PgErrores.esViolacionForeignKey(e)) {
                throw new DAOException("No se puede eliminar: la película tiene funciones asociadas", e);
            }
            throw new DAOException("Error al eliminar la película " + id, e);
        }
    }
}