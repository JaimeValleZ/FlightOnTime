package com.alura.flight_prediction.dto.airport;

import java.util.List;

public record AirLabsAirportResponseDTO(
        List<AirportDTO> response
) {
}
