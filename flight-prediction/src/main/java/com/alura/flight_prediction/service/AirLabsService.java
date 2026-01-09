package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.AirLabsClient;
import com.alura.flight_prediction.dto.airport.AirLabsAirportResponseDTO;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.AirLabsFlightDTO;
import com.alura.flight_prediction.dto.flight.AirLabsFlightResponseDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.dto.route.AirLabsRouteDTO;
import com.alura.flight_prediction.dto.route.AirLabsRoutesResponseDTO;
import com.alura.flight_prediction.dto.route.FlightRouteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AirLabsService {

    private static final Logger log = LoggerFactory.getLogger(AirLabsService.class);

    private final AirLabsClient airLabsClient;

    @Value("2f8529ef-7d84-4e4b-869e-c43ddf1fb526")
    private String apiKey;

    public AirLabsService(AirLabsClient airLabsClient) {
        this.airLabsClient = airLabsClient;
    }

    //Rutas por aerolinea
    public List<FlightRouteDTO> getRoutesByAirline(String airlineIata) {

        AirLabsRoutesResponseDTO response =
                airLabsClient.getRoutes(airlineIata, apiKey);

        return response.response().stream()
                .map(r -> {
                    FlightDetailDTO detail = null;
                    try {
                        detail = getFlightDetail(r.flight_iata());
                    } catch (Exception ex) {
                        log.warn(
                                "No se pudo obtener detalle de vuelo para flight_iata={} (airline_iata={}, dep={}, arr={}). Se retorna ruta parcial. Causa: {}",
                                r.flight_iata(), r.airline_iata(), r.dep_iata(), r.arr_iata(), ex.toString()
                        );
                    }
                    return mapToInternalDTO(r, detail);
                })
                .toList();
    }

    private FlightRouteDTO mapToInternalDTO(AirLabsRouteDTO r, FlightDetailDTO detail) {
        return new FlightRouteDTO(
                r.airline_iata(),
                r.flight_iata(),
                r.dep_iata(),
                r.arr_iata(),
                detail != null ? detail.origen() : null,
                detail != null ? detail.destino() : null,
                detail != null ? detail.horaSalida() : null,
                r.dep_time(),
                r.arr_time()
        );
    }

    //Aeropuertos
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

    //Detalle de vuelo
    public FlightDetailDTO getFlightDetail(String flightIata) {
        AirLabsFlightResponseDTO flightResponse = airLabsClient.getFlight(flightIata, apiKey);

        // OJO: aquí devolvemos null en vez de reventar toda la lista
        if (flightResponse == null || flightResponse.response() == null) {
            return null;
        }

        AirLabsFlightDTO f = flightResponse.response();

        return new FlightDetailDTO(
                f.airline_name(),
                f.airline_iata(),
                f.flight_iata(),
                f.dep_name(),
                f.dep_iata(),
                f.arr_name(),
                f.arr_iata(),
                f.dep_time_utc()
        );
    }
}
