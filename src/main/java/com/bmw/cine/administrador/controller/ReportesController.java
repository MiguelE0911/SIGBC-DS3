package com.bmw.cine.administrador.controller;

import com.bmw.cine.administrador.controller.InformesPDF.ReporteFinancieroPDF;
import com.bmw.cine.administrador.view.ReportesView;
import com.bmw.cine.administrador.controller.InformesPDF.ReporteBoletosPDF;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ReportesController {

    private final ReportesView vista;

    public ReportesController(ReportesView vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {

        vista.getBtnGenerarPDF().setOnAction(e -> {

            try {

                ReporteFinancieroPDF reporte = new ReporteFinancieroPDF();

                reporte.generar(
                        vista.getScene().getWindow()
                );

            } catch (Exception ex) {

                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setHeaderText(null);
                alerta.setContentText(
                        "No fue posible generar el reporte.\n\n"
                                + ex.getMessage()
                );
                alerta.showAndWait();
            }
        });
        vista.getBtnReporteBoletosPDF().setOnAction(e -> {

            try {

                ReporteBoletosPDF reporte = new ReporteBoletosPDF();

                reporte.generar(
                        vista.getScene().getWindow()
                );

            } catch (Exception ex) {

                Alert alerta = new Alert(AlertType.ERROR);
                alerta.setHeaderText(null);
                alerta.setContentText(
                        "No fue posible generar el reporte.\n\n"
                                + ex.getMessage()
                );
                alerta.showAndWait();
            }

        });
    }
}