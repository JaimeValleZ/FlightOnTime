package com.alura.flight_prediction.dto.weather;

public record WeatherMLDTO(
        double temp_mean,
        double precipitation,
        double wind_speed
) {}