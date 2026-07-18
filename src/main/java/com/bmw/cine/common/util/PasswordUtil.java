package com.bmw.cine.common.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Envuelve jBCrypt para que nadie fuera de aquí llame a BCrypt directo
 * ni decida el costo del hash a mano.
 */
public class PasswordUtil {
    private static final int COSTO_HASH = 12; // más alto = más lento pero más seguro; 12 es un buen balance

    private PasswordUtil() {}

    // Genera un hash nuevo (con salt aleatorio incluido) para guardar en BD.
    public static String hash(String contrasenaPlano) {
        return BCrypt.hashpw(contrasenaPlano, BCrypt.gensalt(COSTO_HASH));
    }

    // Compara una contraseña en texto plano contra un hash ya guardado.
    public static boolean verificar(String contrasenaPlano, String hashGuardado) {
        return BCrypt.checkpw(contrasenaPlano, hashGuardado);
    }
}
