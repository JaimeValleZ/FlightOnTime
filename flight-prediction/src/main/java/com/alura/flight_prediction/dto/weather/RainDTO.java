package com.alura.flight_prediction.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RainDTO(
        @JsonProperty("1h") Double oneHour
) {}