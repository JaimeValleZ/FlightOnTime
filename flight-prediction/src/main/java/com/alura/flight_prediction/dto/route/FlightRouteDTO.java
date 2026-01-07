package com.alura.flight_prediction.dto.route;

//DTO temporal para exponer rutas
public record FlightRouteDTO(
        String aerolinea,
        String vuelo,
        String origen,
        String destino
) {
}
