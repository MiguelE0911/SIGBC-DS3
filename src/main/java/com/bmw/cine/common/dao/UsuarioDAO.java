package com.bmw.cine.common.dao;

import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos para Usuario. Cubre login/registro
 * (Espectador) y gestión de cuentas (Administrador).
 *
 * Nota: las implementaciones NUNCA deben comparar contraseñas en texto
 * plano contra la BD. El hash se calcula en la capa DAO o en un util
 * (BCrypt) antes de guardar/comparar — el llamador siempre pasa el
 * texto plano, nunca un hash.
 *
 * Importante: hacia afuera de este DAO solo circula UsuarioDTO, nunca
 * Usuario (la entidad trae contrasenaHash, que no debe llegar ni a
 * controllers ni a vistas).
 */

public interface UsuarioDAO {
    /**
     * Valida credenciales. Devuelve el UsuarioDTO si son correctas,
     * Optional.empty() si el correo no existe o la contraseña no coincide.
     * No lanza excepción por credenciales inválidas — eso no es un error
     * de acceso a datos, es un resultado de negocio normal.
     */
    Optional<UsuarioDTO> autenticar(String correoOUsername, String contrasenaPlano);
    UsuarioDTO registrar(Usuario usuario, String contrasenaPlano); // rol espectador por defecto, lanza DAOException si el correo ya existe
    boolean existeCorreo(String correo);
    boolean existeUsername(String username);
    Optional<UsuarioDTO> buscarPorId(int id);
    List<UsuarioDTO> listarTodos(); // Para el panel de Administrador: listado completo de cuentas.
    boolean actualizarEstadoActivo(int usuarioId, boolean activo); // Suspender o reactivar una cuenta.
    boolean actualizarRol(int usuarioId, int nuevoRol); // Cambiar el rol de un usuario (ver constantes ROL_* en Usuario).
    List<UsuarioDTO> buscarPorTexto(String texto); // Búsqueda parcial por nombre, correo o username (activos). Usado por Personal al emitir boletos manualmente.
    boolean actualizarPerfil(int usuarioId, String nombre, String correo, String username);
    boolean existeCorreoExcluyendo(String correo, int usuarioIdExcluir);  // Igual que existeCorreo, pero ignora al propio usuario (para editar su perfil sin chocar consigo mismo).
    boolean existeUsernameExcluyendo(String username, int usuarioIdExcluir);
}
