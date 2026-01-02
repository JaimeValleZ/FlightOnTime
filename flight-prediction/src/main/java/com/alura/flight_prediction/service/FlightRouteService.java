package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.AirLabsClient;
import com.alura.flight_prediction.dto.AirLabsRouteDTO;
import com.alura.flight_prediction.dto.AirLabsRoutesResponseDTO;
import com.alura.flight_prediction.dto.FlightRouteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightRouteService {

    private final AirLabsClient airLabsClient;

    @Value("2f8529ef-7d84-4e4b-869e-c43ddf1fb526")
    private String apiKey;

    public FlightRouteService(AirLabsClient airLabsClient) {
        this.airLabsClient = airLabsClient;
    }

    public List<FlightRouteDTO> getRoutesByAirline(String airlineIata) {

        AirLabsRoutesResponseDTO response =
                airLabsClient.getRoutes(airlineIata, apiKey);

        return response.response().stream()
                .map(this::mapToInternalDTO)
                .toList();
    }

    private FlightRouteDTO mapToInternalDTO(AirLabsRouteDTO r) {
        return new FlightRouteDTO(
                r.airline_iata(),
                r.flight_iata(),
                r.dep_iata(),
                r.arr_iata());
    }
}
