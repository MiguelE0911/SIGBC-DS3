package com.bmw.cine.common.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class Conexion {
    private static final String CONDUCTOR = "org.mariadb.jdbc.Driver";
    private static final int TIMEOUT_CONEXION_MS = 15_000;

    private static Conexion instancia;
    private final HikariDataSource pool;

    private Conexion() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String url = obtenerVariable(dotenv, "DB_ADDRESS");
        String usuario = obtenerVariable(dotenv, "DB_USER");
        String contrasena = obtenerVariable(dotenv, "DB_PASSWORD");

        this.pool = crearPool("PoolCine", url, usuario, contrasena, TIMEOUT_CONEXION_MS);
        System.out.println("[ConexionPool] Pool inicializado contra " + url);
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
        config.setInitializationFailTimeout(-1);
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
            System.out.println("[ConexionPool] Error al obtener conexión de "
                    + pool.getPoolName() + ": " + e.getMessage());
            return null;
        }
    }

    public void cerrar() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
            System.out.println("[ConexionPool] Pool cerrado.");
        }
    }

    public static void main(String[] args){
        Conexion bd = new Conexion();
        bd.conectar();
    }
}
