package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.view.DashboardView;

import java.text.NumberFormat;
import java.util.Locale;

public class DashboardController {

    private final DashboardView vista;
    private final DashboardServices service;

    public DashboardController(DashboardView vista) {

        this.vista = vista;
        this.service = new DashboardServices();

        cargarDashboard();
    }

    private void cargarDashboard() {

        DashboardServices.DashboardInfo info =
                service.obtenerResumen();

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(Locale.US);

        vista.getLblGananciasMes().setText(
                formato.format(info.getGananciasMes())
        );

        vista.getLblPeliculaMasVendida().setText(
                info.getPeliculaMasVendida()
        );

        vista.getLblUsuariosActivos().setText(
                String.valueOf(info.getUsuariosActivos())
        );
    }
}