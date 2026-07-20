package com.bmw.cine.administrador.controller;

import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.UsuarioDAO;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.dao.impl.UsuarioDAOImpl;
import com.bmw.cine.common.dto.UsuarioDTO;
import com.bmw.cine.common.model.Funcion;
import com.bmw.cine.common.model.Pelicula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DashboardServices {

    private final FuncionDAO funcionDAO;
    private final PeliculaDAO peliculaDAO;
    private final UsuarioDAO usuarioDAO;

    public DashboardServices() {
        funcionDAO = new FuncionDAOImpl();
        peliculaDAO = new PeliculaDAOImpl();
        usuarioDAO = new UsuarioDAOImpl();
    }

    public DashboardInfo obtenerResumen() {

        BigDecimal gananciasMes = BigDecimal.ZERO;

        Map<String, Integer> boletosPorPelicula = new HashMap<>();

        LocalDate hoy = LocalDate.now();

        List<Funcion> funciones = funcionDAO.listarTodas();

        for (Funcion funcion : funciones) {

            int boletosVendidos =
                    funcionDAO.listarAsientosOcupados(funcion.getId()).size();

            Optional<Pelicula> peliculaOpt =
                    peliculaDAO.buscarPorId(funcion.getPeliculaId());

            if (peliculaOpt.isEmpty()) {
                continue;
            }

            Pelicula pelicula = peliculaOpt.get();

            boletosPorPelicula.merge(
                    pelicula.getTitulo(),
                    boletosVendidos,
                    Integer::sum
            );

            if (funcion.horario().getMonth() == hoy.getMonth()
                    && funcion.horario().getYear() == hoy.getYear()) {

                BigDecimal ganancia =
                        funcion.getPrecioBase().multiply(
                                BigDecimal.valueOf(boletosVendidos));

                gananciasMes = gananciasMes.add(ganancia);
            }
        }

        String peliculaTop = "-";
        int mayorCantidad = -1;

        for (Map.Entry<String, Integer> entry : boletosPorPelicula.entrySet()) {

            if (entry.getValue() > mayorCantidad) {

                mayorCantidad = entry.getValue();
                peliculaTop = entry.getKey();
            }
        }

        int usuariosActivos = 0;

        List<UsuarioDTO> usuarios = usuarioDAO.listarTodos();

        for (UsuarioDTO usuario : usuarios) {

            if (usuario.isActivo()) {
                usuariosActivos++;
            }
        }

        return new DashboardInfo(
                gananciasMes,
                peliculaTop,
                usuariosActivos
        );
    }

    public static class DashboardInfo {

        private final BigDecimal gananciasMes;
        private final String peliculaMasVendida;
        private final int usuariosActivos;

        public DashboardInfo(
                BigDecimal gananciasMes,
                String peliculaMasVendida,
                int usuariosActivos) {

            this.gananciasMes = gananciasMes;
            this.peliculaMasVendida = peliculaMasVendida;
            this.usuariosActivos = usuariosActivos;
        }

        public BigDecimal getGananciasMes() {
            return gananciasMes;
        }

        public String getPeliculaMasVendida() {
            return peliculaMasVendida;
        }

        public int getUsuariosActivos() {
            return usuariosActivos;
        }
    }
}