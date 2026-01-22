package com.alura.flight_prediction.dto.route;

import java.util.List;

public record AirLabsRoutesResponseDTO(
        List<AirLabsRouteDTO> response
) {
}
