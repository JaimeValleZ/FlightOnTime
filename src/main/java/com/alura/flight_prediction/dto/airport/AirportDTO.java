package com.alura.flight_prediction.dto.airport;

public record AirportDTO(
        String iata_code,
        Double lat,
        Double lng,
        String country_code,
        String name

) {
}
