package com.bmw.cine.common.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

/*
 * Pool de conexiones hacia CockroachDB (driver de PostgreSQL, ya que
 * CockroachDB implementa el protocolo de wire de PostgreSQL).*/
public class Conexion {

    private static final String CONDUCTOR = "org.postgresql.Driver";
    private static final int TIMEOUT_CONEXION_MS = 15_000;

    private static Conexion instancia;
    private final HikariDataSource pool;

    private Conexion() {
        try {
            Class.forName(CONDUCTOR);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver de PostgreSQL en el classpath", e);
        }

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String url = obtenerVariable(dotenv, "CLOUD_ADDRESS");
        String usuario = obtenerVariable(dotenv, "CLOUD_USER");
        String contrasena = obtenerVariable(dotenv, "CLOUD_PASSWORD");

        this.pool = crearPool("PoolCine", url, usuario, contrasena, TIMEOUT_CONEXION_MS);
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    private static HikariDataSource crearPool(String nombre, String url, String usuario, String contrasena, int timeoutConexionMs) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(usuario);
        config.setPassword(contrasena);
        config.setPoolName(nombre);
        config.setConnectionTimeout(timeoutConexionMs);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        /* Si el nodo al que estabas conectado se reinicia (mantenimiento,
        caída, etc.), fuerza a renovar conexiones periódicamente en vez
        de acumular conexiones "muertas" en el pool.*/
        config.setMaxLifetime(1_800_000); // 30 min
        config.setKeepaliveTime(300_000); // 5 min, evita que Tailscale/NAT cierre la conexión por inactividad
        config.setInitializationFailTimeout(-1); // no bloquea el arranque si al inicio ningún nodo responde
        // Mejora el rendimiento de INSERTs repetidos como en solicitarBoletos().
        config.addDataSourceProperty("reWriteBatchedInserts", "true");
        return new HikariDataSource(config);
    }

    private static String obtenerVariable(Dotenv dotenv, String clave) {
        String valor = dotenv.get(clave);
        if (valor == null || valor.isBlank()) {
            throw new RuntimeException("Falta la variable '" + clave + "' en el archivo .env");
        }
        return valor;
    }

    public Connection conectar() {
        try {
            return pool.getConnection();
        } catch (SQLException e) {
            // Antes esto retornaba null silenciosamente: con un clúster de
            // 2 nodos sobre Tailscale, un fallo de conexión transitorio es
            // mucho más probable que con un solo servidor MariaDB local, y
            // un null aquí se traduce en un NullPointerException confuso
            // varias capas más arriba. Mejor fallar rápido y explícito.
            throw new RuntimeException("No se pudo obtener una conexión del pool hacia CockroachDB", e);
        }
    }

    public void cerrar() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
        }
    }
}