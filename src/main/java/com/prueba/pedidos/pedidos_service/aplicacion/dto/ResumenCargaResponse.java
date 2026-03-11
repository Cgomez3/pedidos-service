package com.prueba.pedidos.pedidos_service.aplicacion.dto;


import java.util.List;

public class ResumenCargaResponse {

    private int totalProcesados;
    private int guardados;
    private int conError;

    private List<ErrorFila> erroresPorFila;

    private List<ErrorAgrupado> erroresAgrupados;

    public ResumenCargaResponse(int totalProcesados,
                                int guardados,
                                int conError,
                                List<ErrorFila> erroresPorFila,
                                List<ErrorAgrupado> erroresAgrupados) {

        this.totalProcesados = totalProcesados;
        this.guardados = guardados;
        this.conError = conError;
        this.erroresPorFila = erroresPorFila;
        this.erroresAgrupados = erroresAgrupados;
    }

    public int getTotalProcesados() { return totalProcesados; }

    public int getGuardados() { return guardados; }

    public int getConError() { return conError; }

    public List<ErrorFila> getErroresPorFila() { return erroresPorFila; }

    public List<ErrorAgrupado> getErroresAgrupados() { return erroresAgrupados; }
}