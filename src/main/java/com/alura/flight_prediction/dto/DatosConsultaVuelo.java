package com.alura.flight_prediction.dto;

import com.alura.flight_prediction.entity.Vuelo;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record DatosConsultaVuelo(
    @NotBlank String aerolinea,
    @NotBlank String destino,
    @NotBlank String origen,
    @NotNull @Future LocalDateTime fechaPartida,
    @NotNull @Positive Integer distancia
) {

}
