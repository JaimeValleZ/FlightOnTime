package com.alura.flight_prediction.client;

import com.alura.flight_prediction.dto.route.AirLabsRoutesResponseDTO;
import com.alura.flight_prediction.dto.airport.AirLabsAirportResponseDTO;
import com.alura.flight_prediction.dto.flight.AirLabsFlightResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "airlabs-client",
        url = "https://airlabs.co/api/v9/"
)
public interface AirLabsClient {

    //Obtener los vuelos segun aerolinea
    @GetMapping("/routes")
    AirLabsRoutesResponseDTO getRoutes(
            @RequestParam("airline_iata") String airlineIata,
            @RequestParam("api_key") String apiKey);


    //Obtener los datos de un vuelo especifico
    @GetMapping("/flight")
    AirLabsFlightResponseDTO getFlight(
            @RequestParam("flight_iata") String flightIata,
            @RequestParam("api_key") String apiKey);

    //Obtener los datos de un vuelo especifico
    @GetMapping("/airports")
    AirLabsAirportResponseDTO getAirport(
            @RequestParam("iata_code") String airportIata,
            @RequestParam("api_key") String apiKey);
}
