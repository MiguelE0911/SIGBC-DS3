package com.bmw.cine.administrador.controller.InformesPDF;

import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.model.Funcion;
import com.bmw.cine.common.model.Pelicula;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReporteBoletosService {

    private final FuncionDAO funcionDAO = new FuncionDAOImpl();
    private final PeliculaDAO peliculaDAO = new PeliculaDAOImpl();

    public static class RegistroBoleto {

        private final String pelicula;
        private final String funcion;
        private final int boletosVendidos;

        public RegistroBoleto(String pelicula,
                              String funcion,
                              int boletosVendidos) {

            this.pelicula = pelicula;
            this.funcion = funcion;
            this.boletosVendidos = boletosVendidos;
        }

        public String getPelicula() {
            return pelicula;
        }

        public String getFuncion() {
            return funcion;
        }

        public int getBoletosVendidos() {
            return boletosVendidos;
        }
    }

    public List<RegistroBoleto> obtenerReporte() {

        List<RegistroBoleto> reporte = new ArrayList<>();

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<Funcion> funciones = funcionDAO.listarTodas();

        for (Funcion funcion : funciones) {

            Pelicula pelicula = peliculaDAO
                    .buscarPorId(funcion.getPeliculaId())
                    .orElse(null);

            if (pelicula == null) {
                continue;
            }

            int boletosVendidos = funcionDAO
                    .listarAsientosOcupados(funcion.getId())
                    .size();

            reporte.add(new RegistroBoleto(
                    pelicula.getTitulo(),
                    funcion.getHorario().format(formato),
                    boletosVendidos
            ));
        }
        return reporte;
    }
}