package com.alura.flight_prediction.dto.weather;

public record OpenWeatherResponseDTO(
        MainDTO main,
        WindDTO wind,
        RainDTO rain
){}