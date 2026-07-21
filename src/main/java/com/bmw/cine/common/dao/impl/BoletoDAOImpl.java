package com.bmw.cine.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.bmw.cine.common.dao.BoletoDAO;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.db.Conexion;
import com.bmw.cine.common.db.PgErrores;
import com.bmw.cine.common.dto.FiltroSolicitudDTO;
import com.bmw.cine.common.dto.SolicitudBoletoDTO;

public class BoletoDAOImpl implements BoletoDAO {
    private static final String SQL_BASE =
            "SELECT b.id AS boleto_id, u.nombre AS nombre_usuario, p.titulo AS titulo_pelicula, "
                    + "f.horario AS horario_funcion, s.nombre AS nombre_sala, b.asiento_codigo, "
                    + "b.estado, b.fecha_solicitud "
                    + "FROM boleto b "
                    + "JOIN usuario u ON b.usuario_id = u.id "
                    + "JOIN funcion f ON b.funcion_id = f.id "
                    + "JOIN pelicula p ON f.pelicula_id = p.id "
                    + "JOIN sala s ON f.sala_id = s.id";

    @Override
    public List<SolicitudBoletoDTO> listarSolicitudes(FiltroSolicitudDTO filtro) {
        StringBuilder sql = new StringBuilder(SQL_BASE);
        List<Object> parametros = new ArrayList<>();
        List<String> condiciones = new ArrayList<>();

        if (filtro != null) {
            if (filtro.getPeliculaId() != null) {
                condiciones.add("p.id = ?");
                parametros.add(filtro.getPeliculaId());
            }
            if (filtro.getFuncionId() != null) {
                condiciones.add("f.id = ?");
                parametros.add(filtro.getFuncionId());
            }
            if (filtro.getEstado() != null) {
                condiciones.add("b.estado = ?::estado_boleto");
                parametros.add(filtro.getEstado().toString());
            }
        }

        if (!condiciones.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", condiciones));
        }
        sql.append(" ORDER BY b.fecha_solicitud ASC"); // FIFO: la más vieja primero

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            List<SolicitudBoletoDTO> solicitudes = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    solicitudes.add(new SolicitudBoletoDTO(
                            rs.getInt("boleto_id"),
                            rs.getString("nombre_usuario"),
                            rs.getString("titulo_pelicula"),
                            rs.getTimestamp("horario_funcion").toLocalDateTime(),
                            rs.getString("nombre_sala"),
                            rs.getString("asiento_codigo"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_solicitud").toLocalDateTime()
                    ));
                }
            }
            return solicitudes;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las solicitudes de boletos", e);
        }
    }

    //NUEVO METODO SOLICITUD BOLETO
    @Override
    public List<SolicitudBoletoDTO> listarPorUsuario(int usuarioId) {
        String sql = SQL_BASE + " WHERE b.usuario_id = ? ORDER BY b.fecha_solicitud DESC";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            List<SolicitudBoletoDTO> boletos = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    boletos.add(new SolicitudBoletoDTO(
                            rs.getInt("boleto_id"),
                            rs.getString("nombre_usuario"),
                            rs.getString("titulo_pelicula"),
                            rs.getTimestamp("horario_funcion").toLocalDateTime(),
                            rs.getString("nombre_sala"),
                            rs.getString("asiento_codigo"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_solicitud").toLocalDateTime()
                    ));
                }
            }
            return boletos;
        } catch (SQLException e) {
            throw new DAOException("Error al listar los boletos del usuario " + usuarioId, e);
        }
    }

    @Override
    public boolean aprobarSolicitud(int boletoId, int aprobadoPorUsuarioId) {
        String sql = "UPDATE boleto SET estado = 'CONFIRMADO', aprobado_por = ?, fecha_confirmacion = ? "
                + "WHERE id = ? AND estado = 'PENDIENTE'";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, aprobadoPorUsuarioId);
            ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(3, boletoId);

            return ps.executeUpdate() > 0; // 0 filas = ya no estaba PENDIENTE (otro staff ya la procesó)
        } catch (SQLException e) {
            throw new DAOException("Error al aprobar el boleto " + boletoId, e);
        }
    }

    @Override
    public boolean rechazarSolicitud(int boletoId) {
        // No hay estado "RECHAZADO" en el esquema: rechazar = borrar la fila,
        // así el UNIQUE(funcion_id, asiento_codigo) deja libre el asiento.
        String sql = "DELETE FROM boleto WHERE id = ? AND estado = 'PENDIENTE'";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, boletoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al rechazar el boleto " + boletoId, e);
        }
    }

    @Override
    public List<String> listarAsientosOcupados(int funcionId) {
        String sql = "SELECT asiento_codigo FROM boleto WHERE funcion_id = ?";
        List<String> ocupados = new ArrayList<>();
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, funcionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ocupados.add(rs.getString("asiento_codigo"));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al consultar asientos ocupados", ex);
        }
        return ocupados;
    }

    @Override
    public int emitirConfirmado(int usuarioId, int funcionId, String asientoCodigo, int aprobadoPorUsuarioId) {
        String sql = "INSERT INTO boleto (usuario_id, funcion_id, asiento_codigo, estado, aprobado_por, " +
                "fecha_solicitud, fecha_confirmacion) VALUES (?, ?, ?, 'CONFIRMADO', ?, NOW(), NOW())";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, funcionId);
            ps.setString(3, asientoCodigo);
            ps.setInt(4, aprobadoPorUsuarioId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                throw new DAOException("No se obtuvo el id del boleto generado");
            }
        } catch (SQLException ex) {
            if (PgErrores.esViolacionUnica(ex)) {
                throw new DAOException("ASIENTO_OCUPADO: el asiento " + asientoCodigo + " ya fue tomado.", ex);
            }
            if (PgErrores.esErrorReintentable(ex)) {
                throw new DAOException("CONFLICTO_CONCURRENCIA: reintenta la operación.", ex);
            }
            throw new DAOException("Error al emitir boleto", ex);
        }
    }

    //Solicitad boleto agregado
    @Override
    public List<Integer> solicitarBoletos(int usuarioId, int funcionId, List<String> asientosCodigos) {
        String sql = "INSERT INTO boleto (usuario_id, funcion_id, asiento_codigo, estado, fecha_solicitud) " +
                "VALUES (?, ?, ?, 'PENDIENTE', NOW())";

        List<Integer> idsGenerados = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (String asiento : asientosCodigos) {
                    ps.setInt(1, usuarioId);
                    ps.setInt(2, funcionId);
                    ps.setString(3, asiento);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            idsGenerados.add(keys.getInt(1));
                        } else {
                            throw new DAOException("No se obtuvo el id del boleto generado para el asiento " + asiento);
                        }
                    }
                }

                con.commit();
                return idsGenerados;

            } catch (SQLException ex) {
                con.rollback();
                if (PgErrores.esViolacionUnica(ex)) {
                    throw new DAOException("ASIENTO_OCUPADO: uno o más asientos ya fueron reservados por otro usuario.", ex);
                }
                if (PgErrores.esErrorReintentable(ex)) {
                    throw new DAOException("CONFLICTO_CONCURRENCIA: reintenta la solicitud de boletos.", ex);
                }
                throw new DAOException("Error al solicitar los boletos", ex);
            }

        } catch (SQLException ex) {
            throw new DAOException("Error de conexión al solicitar boletos", ex);
        }
    }
}