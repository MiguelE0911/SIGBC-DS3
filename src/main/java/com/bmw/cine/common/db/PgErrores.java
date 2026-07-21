package com.bmw.cine.common.db;

import java.sql.SQLException;

/*
 * Utilidad para interpretar los códigos SQLState que devuelve el driver de
 * PostgreSQL (el mismo que usa CockroachDB).
 * Referencia de códigos: https://www.postgresql.org/docs/current/errcodes-appendix.html
 */
public final class PgErrores {

    /* Clase 23 — Integrity Constraint Violation. Violación de UNIQUE. */
    private static final String VIOLACION_UNICA = "23505";

    /* Clase 23 — Integrity Constraint Violation. Violación de FOREIGN KEY. */
    private static final String VIOLACION_FOREIGN_KEY = "23503";

    /*
     * Clase 40 — Transaction Rollback. CockroachDB (a diferencia de MariaDB
     * en modo single-node) usa aislamiento SERIALIZABLE real y de forma
     * habitual puede rechazar una transacción por conflicto de concurrencia
     * aunque el SQL en sí sea válido. El cliente DEBE reintentar la
     * transacción completa cuando ve este código.
     */
    private static final String ERROR_SERIALIZACION = "40001";

    private PgErrores() {
    }

    public static boolean esViolacionUnica(SQLException e) {
        return VIOLACION_UNICA.equals(e.getSQLState());
    }

    public static boolean esViolacionForeignKey(SQLException e) {
        return VIOLACION_FOREIGN_KEY.equals(e.getSQLState());
    }

    /*
     * true si la transacción falló por contención/conflicto de concurrencia
     * y CockroachDB espera que el cliente la reintente desde cero (no es un
     * error real de datos ni de sintaxis).
     */
    public static boolean esErrorReintentable(SQLException e) {
        return ERROR_SERIALIZACION.equals(e.getSQLState());
    }
}