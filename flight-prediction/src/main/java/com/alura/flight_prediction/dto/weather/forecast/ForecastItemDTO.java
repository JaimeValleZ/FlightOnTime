package com.alura.flight_prediction.dto.weather.forecast;

import com.alura.flight_prediction.dto.weather.MainDTO;
import com.alura.flight_prediction.dto.weather.WindDTO;

//Item por cada 3 horas
public record ForecastItemDTO(
        long dt,
        MainDTO main,
        WindDTO wind,
        Double pop
) {}