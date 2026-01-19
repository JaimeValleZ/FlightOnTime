package com.alura.flight_prediction.dto;

public record VueloStatsDTO(
        long totalVuelos,
        long vuelosPuntuales,
        long vuelosRetrasados
) {

    public double porcentajePuntual() {
        return totalVuelos > 0
                ? (vuelosPuntuales * 100.0) / totalVuelos
                : 0.0;
    }

    public double porcentajeRetrasado() {
        return totalVuelos > 0
                ? (vuelosRetrasados * 100.0) / totalVuelos
                : 0.0;
    }
}

