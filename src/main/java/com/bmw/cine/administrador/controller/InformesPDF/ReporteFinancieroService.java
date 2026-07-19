package com.bmw.cine.administrador.controller.InformesPDF;

import com.bmw.cine.common.dao.FuncionDAO;
import com.bmw.cine.common.dao.PeliculaDAO;
import com.bmw.cine.common.dao.impl.FuncionDAOImpl;
import com.bmw.cine.common.dao.impl.PeliculaDAOImpl;
import com.bmw.cine.common.model.Funcion;
import com.bmw.cine.common.model.Pelicula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReporteFinancieroService {

    private final FuncionDAO funcionDAO;
    private final PeliculaDAO peliculaDAO;

    public ReporteFinancieroService() {
        this.funcionDAO = new FuncionDAOImpl();
        this.peliculaDAO = new PeliculaDAOImpl();
    }

    public List<FilaReporteFinanciero> obtenerReporte() {

        List<FilaReporteFinanciero> reporte = new ArrayList<>();

        List<Funcion> funciones = funcionDAO.listarTodas();

        for (Funcion funcion : funciones) {

            Optional<Pelicula> peliculaOpt = peliculaDAO.buscarPorId(funcion.getPeliculaId());

            if (peliculaOpt.isEmpty()) {
                continue;
            }

            Pelicula pelicula = peliculaOpt.get();

            int asistentes = funcionDAO
                    .listarAsientosOcupados(funcion.getId())
                    .size();

            BigDecimal precio = funcion.precioBase();

            BigDecimal ganancia = precio.multiply(BigDecimal.valueOf(asistentes));

            reporte.add(new FilaReporteFinanciero(
                    pelicula.getTitulo(),
                    funcion.horario().toString(),
                    asistentes,
                    precio,
                    ganancia
            ));
        }

        return reporte;
    }

    public static class FilaReporteFinanciero {

        private final String pelicula;
        private final String funcion;
        private final int asistentes;
        private final BigDecimal precio;
        private final BigDecimal ganancia;

        public FilaReporteFinanciero(
                String pelicula,
                String funcion,
                int asistentes,
                BigDecimal precio,
                BigDecimal ganancia) {

            this.pelicula = pelicula;
            this.funcion = funcion;
            this.asistentes = asistentes;
            this.precio = precio;
            this.ganancia = ganancia;
        }

        public String getPelicula() {
            return pelicula;
        }

        public String getFuncion() {
            return funcion;
        }

        public int getAsistentes() {
            return asistentes;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public BigDecimal getGanancia() {
            return ganancia;
        }
    }
}