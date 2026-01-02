package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.AirLabsClient;
import com.alura.flight_prediction.dto.airport.AirLabsAirportResponseDTO;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirLabsClient airLabsClient;

    @Value("2f8529ef-7d84-4e4b-869e-c43ddf1fb526")
    private String apiKey;

    public AirportService(AirLabsClient airLabsClient) {
        this.airLabsClient = airLabsClient;
    }

    public List<AirportDTO> getAirportsByCity(String airportCode){

        AirLabsAirportResponseDTO response =
                airLabsClient.getAirport(airportCode, apiKey);

        return response.response().stream()
                .map(this::mapToInternalDTO)
                .toList();
    }

    private AirportDTO mapToInternalDTO(AirportDTO a) {
        return new AirportDTO(
                a.iata_code(),
                a.lat(),
                a.lng(),
                a.country_code(),
                a.name()
        );
    }
}
