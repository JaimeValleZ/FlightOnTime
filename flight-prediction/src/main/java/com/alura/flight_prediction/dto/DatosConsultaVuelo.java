package com.alura.flight_prediction.dto;

import java.time.LocalDateTime;

public record DatosConsultaVuelo(
    String aerolinea,
    String destino,
    String origen,
    LocalDateTime fechaPartida
) {
}
