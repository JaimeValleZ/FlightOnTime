package com.alura.flight_prediction.dto;

import com.alura.flight_prediction.entity.Vuelo;

public record DatosRespuestaVuelo(
        String prevision,
        Double probabilidad
) {
    public DatosRespuestaVuelo(Vuelo vuelo) {
        this(vuelo.getPrevision(), vuelo.getProbabilidad());
    }
}
