package com.alura.flight_prediction.dto.flight;

public record AirLabsFlightDTO(

        // Aerolínea
        String airline_iata,
        String airline_name,

        // Vueloairline_name
        String flight_iata,
        String flight_number,
        String status,
        Integer duration,

        // Origen
        String dep_name,
        String dep_iata,
        String dep_city,
        String dep_country,
        String dep_time,
        String dep_time_utc,

        // Destino
        String arr_name,
        String arr_iata,
        String arr_city,
        String arr_country,
        String arr_time,
        String arr_time_utc
) {
}
