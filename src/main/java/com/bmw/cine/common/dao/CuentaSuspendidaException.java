package com.bmw.cine.common.dao;

/**
 * Se lanza desde UsuarioDAO.autenticar() cuando el correo y la
 * contraseña son correctos, pero la cuenta está suspendida
 * (usuario.activo = false). No es un error de acceso a datos —
 * es una regla de negocio, por eso no extiende DAOException.
 *
 * El LoginController debe capturarla aparte para mostrar un mensaje
 * distinto a "correo o contraseña incorrectos".
 */
public class CuentaSuspendidaException extends RuntimeException {

    public CuentaSuspendidaException(String mensaje) {
        super(mensaje);
    }
}
