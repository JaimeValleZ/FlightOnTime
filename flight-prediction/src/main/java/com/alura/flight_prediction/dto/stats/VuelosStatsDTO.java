package com.alura.flight_prediction.dto.stats;

import lombok.Getter;

@Getter
public class VuelosStatsDTO {
    private long totalVuelos;
    private long vuelosPuntuales;
    private long vuelosRetrasados;

    private double porcentajePuntual;
    private double porcentajeRetrasado;

    public VuelosStatsDTO(long total, long puntual, long retrsasado){
        this.totalVuelos = total;
        this.vuelosPuntuales = puntual;
        this.vuelosRetrasados = retrsasado;
        if(total > 0){
            this.porcentajePuntual = (puntual * 100.0) / total;
            this.porcentajeRetrasado = (retrsasado * 100.0) / total;
        }else{
            this.porcentajePuntual = 0.0;
            this.porcentajeRetrasado = 0.0;
        }
    }
}
