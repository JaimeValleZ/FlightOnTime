package com.alura.flight_prediction.dto.route;

import java.time.LocalDateTime;

//DTO temporal para exponer rutas
public record FlightRouteDTO(
        String aerolinea,
        String vuelo,
        String origenIata,
        String destinoIata,
        String origen,
        String destino,
        String fecha,
        String horaSalida,
        String horaLlegada
) {
}
