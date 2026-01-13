package com.alura.flight_prediction.dto.weather.forecast;

import java.util.List;

public record OpenWeatherForecastResponseDTO(
        List<ForecastItemDTO> list
) {}
