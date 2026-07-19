package com.bmw.cine.common.dao.impl;

import com.bmw.cine.common.dao.DAOException;
import com.bmw.cine.common.dao.SalaDAO;
import com.bmw.cine.common.db.Conexion;
import com.bmw.cine.common.model.Sala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaDAOImpl implements SalaDAO {

    private static final String TABLA = "sala";

    @Override
    public List<Sala> listarTodas() {
        String sql = "SELECT id, nombre, filas, columnas FROM " + TABLA + " ORDER BY nombre";

        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Sala> salas = new ArrayList<>();
            while (rs.next()) {
                salas.add(new Sala(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("filas"),
                        rs.getInt("columnas")
                ));
            }
            return salas;
        } catch (SQLException e) {
            throw new DAOException("Error al listar las salas", e);
        }
    }
}