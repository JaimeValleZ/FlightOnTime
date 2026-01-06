package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.AirLabsClient;
import com.alura.flight_prediction.dto.flight.AirLabsFlightDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlightDetailService {

    private final AirLabsClient airLabsClient;

    @Value("2f8529ef-7d84-4e4b-869e-c43ddf1fb526")
    private String apiKey;

    public FlightDetailService(AirLabsClient airLabsClient) {
        this.airLabsClient = airLabsClient;
    }

    public FlightDetailDTO getFlightDetail(String flightIata){
        AirLabsFlightDTO f =
                airLabsClient.getFlight(flightIata, apiKey).response();

        return new FlightDetailDTO(
                f.airline_name(),
                f.airline_iata(),
                f.flight_iata(),
                f.dep_name(),
                f.dep_iata(),
                f.arr_name(),
                f.arr_iata(),
                f.dep_time_utc());
    }
}
