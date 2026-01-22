package com.alura.flight_prediction.dto.flight;

import com.alura.flight_prediction.dto.weather.WeatherDTO;

public record FlightDetailDTO(
        String aerolinea,
        String aerolineaIata,
        String vuelo,
        String origen,
        String origenIata,
        String paisOrigen,
        String ciudadOrigen,
        String destino,
        String destinoIata,
        String paisDestino,
        String ciudadDestino,
        String horaSalida,
        String horaLlegada,
        Integer duracion,
        WeatherDTO climaActual


) {
}
