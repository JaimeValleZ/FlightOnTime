package com.alura.flight_prediction.dto;

import java.util.List;

public record AirLabsRoutesResponseDTO(
        AirLabsRequestDTO request,
        List<AirLabsRouteDTO> response
) {
}
