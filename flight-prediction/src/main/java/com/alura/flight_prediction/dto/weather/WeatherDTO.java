package com.alura.flight_prediction.dto.weather;


public record WeatherDTO(
        double temperature,      // °C
        double precipitation,    // mm o %
        double windSpeed,        // m/s
        boolean forecast         // true si viene de /forecast
) {}