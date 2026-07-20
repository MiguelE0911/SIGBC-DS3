package com.bmw.cine.common.dao.impl;
import com.bmw.cine.common.dao.CuentaSuspendidaException;
import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.db.Conexion;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;
import com.bmw.cine.common.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Implementación real de UsuarioDAO contra MariaDB, alineada a la tabla
 * `usuario` real: id, nombre, correo, username, password_hash, rol_id,
 * activo, fecha_registro.
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    private static final String TABLA = "usuario";

    @Override
    public Optional<UsuarioDTO> autenticar(String correoOUsername, String contrasenaPlano) {
        String sql = "SELECT id, nombre, correo, username, password_hash, rol_id, activo " // Acepta login por correo o username indistintamente (ambos son UNIQUE).
                + "FROM " + TABLA + " WHERE correo = ? OR username = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correoOUsername);
            ps.setString(2, correoOUsername);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty(); // no existe esa cuenta
                }

                String hashGuardado = rs.getString("password_hash");
                if (!PasswordUtil.verificar(contrasenaPlano, hashGuardado)) {
                    return Optional.empty(); // contraseña incorrecta
                }

                boolean activo = rs.getBoolean("activo");
                if (!activo) {
                    throw new CuentaSuspendidaException(
                            "La cuenta de " + correoOUsername + " está suspendida. Contacta a un administrador.");
                }

                return Optional.of(mapearFila(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al autenticar al usuario " + correoOUsername, e);
        }
    }

    @Override
    public UsuarioDTO registrar(Usuario usuario, String contrasenaPlano) {
        if (existeCorreo(usuario.getCorreo())) {
            throw new DAOException("Ya existe una cuenta registrada con el correo " + usuario.getCorreo());
        }
        if (existeUsername(usuario.getUsername())) {
            throw new DAOException("Ya existe una cuenta registrada con el username " + usuario.getUsername());
        }

        String sql = "INSERT INTO " + TABLA
                + " (nombre, correo, username, password_hash, rol_id, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getUsername());
            ps.setString(4, PasswordUtil.hash(contrasenaPlano));
            ps.setInt(5, Usuario.ROL_ESPECTADOR); // el registro público siempre crea Espectadores
            ps.setBoolean(6, true);

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                int idGenerado = claves.next() ? claves.getInt(1) : -1;
                return new UsuarioDTO(idGenerado, usuario.getNombre(), usuario.getCorreo(),
                        usuario.getUsername(), Usuario.ROL_ESPECTADOR, true);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al registrar al usuario " + usuario.getCorreo(), e);
        }
    }

    @Override
    public boolean existeCorreo(String correo) {
        return existeValorEnColumna("correo", correo);
    }

    @Override
    public boolean existeUsername(String username) {
        return existeValorEnColumna("username", username);
    }

    private boolean existeValorEnColumna(String columna, String valor) {
        String sql = "SELECT 1 FROM " + TABLA + " WHERE " + columna + " = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al verificar " + columna + " = " + valor, e);
        }
    }

    @Override
    public Optional<UsuarioDTO> buscarPorId(int id) {
        String sql = "SELECT id, nombre, correo, username, password_hash, rol_id, activo "
                + "FROM " + TABLA + " WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar el usuario con id " + id, e);
        }
    }

    @Override
    public List<UsuarioDTO> listarTodos() {
        String sql = "SELECT id, nombre, correo, username, password_hash, rol_id, activo "
                + "FROM " + TABLA + " ORDER BY nombre";

        List<UsuarioDTO> usuarios = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearFila(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new DAOException("Error al listar los usuarios", e);
        }
    }

    @Override
    public boolean actualizarEstadoActivo(int usuarioId, boolean activo) {
        String sql = "UPDATE " + TABLA + " SET activo = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, usuarioId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el estado del usuario " + usuarioId, e);
        }
    }

    @Override
    public boolean actualizarRol(int usuarioId, int nuevoRol) {
        String sql = "UPDATE " + TABLA + " SET rol_id = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoRol);
            ps.setInt(2, usuarioId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el rol del usuario " + usuarioId, e);
        }
    }

    /*
     * Convierte la fila actual del ResultSet a UsuarioDTO. Nunca lee ni
     * expone password_hash hacia afuera de esta clase.
     */
    private UsuarioDTO mapearFila(ResultSet rs) throws SQLException {
        return new UsuarioDTO(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("username"),
                rs.getInt("rol_id"),
                rs.getBoolean("activo")
        );
    }

    @Override
    public List<UsuarioDTO> buscarPorTexto(String texto) {
        String sql = "SELECT id, nombre, correo, username, rol_id, activo FROM usuario " +
                "WHERE (nombre LIKE ? OR correo LIKE ? OR username LIKE ?) AND activo = TRUE " +
                "ORDER BY nombre LIMIT 20";
        String like = "%" + texto + "%";
        List<UsuarioDTO> resultados = new ArrayList<>();
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new UsuarioDTO(
                            rs.getInt("id"), rs.getString("nombre"), rs.getString("correo"),
                            rs.getString("username"), rs.getInt("rol_id"), rs.getBoolean("activo")
                    ));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al buscar usuarios", ex);
        }
        return resultados;
    }

    @Override
    public boolean actualizarPerfil(int usuarioId, String nombre, String correo, String username) {
        String sql = "UPDATE " + TABLA + " SET nombre = ?, correo = ?, username = ? WHERE id = ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, username);
            ps.setInt(4, usuarioId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el perfil del usuario " + usuarioId, e);
        }
    }

    @Override
    public boolean existeCorreoExcluyendo(String correo, int usuarioIdExcluir) {
        return existeValorExcluyendo("correo", correo, usuarioIdExcluir);
    }

    @Override
    public boolean existeUsernameExcluyendo(String username, int usuarioIdExcluir) {
        return existeValorExcluyendo("username", username, usuarioIdExcluir);
    }

    private boolean existeValorExcluyendo(String columna, String valor, int usuarioIdExcluir) {
        String sql = "SELECT 1 FROM " + TABLA + " WHERE " + columna + " = ? AND id <> ?";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, valor);
            ps.setInt(2, usuarioIdExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Error al verificar " + columna + " = " + valor, e);
        }
    }
}