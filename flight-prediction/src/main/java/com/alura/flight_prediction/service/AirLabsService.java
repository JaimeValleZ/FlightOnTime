package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.AirLabsClient;
import com.alura.flight_prediction.dto.PageResponse;
import com.alura.flight_prediction.dto.airport.AirLabsAirportResponseDTO;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.AirLabsFlightDTO;
import com.alura.flight_prediction.dto.flight.AirLabsFlightResponseDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.dto.route.AirLabsRouteDTO;
import com.alura.flight_prediction.dto.route.AirLabsRoutesResponseDTO;
import com.alura.flight_prediction.dto.route.FlightRouteDTO;
import com.alura.flight_prediction.dto.weather.WeatherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AirLabsService {

    private static final Logger log = LoggerFactory.getLogger(AirLabsService.class);

    private final AirLabsClient airLabsClient;
    private final WeatherService weatherService;


    @Value("191bb5fa-7be7-44ab-866a-adc19c190906")
    private String apiKey;

    public AirLabsService(AirLabsClient airLabsClient, WeatherService weatherService
    ) {
        this.airLabsClient = airLabsClient;
        this.weatherService = weatherService;
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

    public FlightDetailDTO getFlightDetail(String flightIata) {

        AirLabsFlightResponseDTO response =
                airLabsClient.getFlight(flightIata, apiKey);

        if (response == null || response.response() == null) {
            return null;
        }

        AirLabsFlightDTO f = response.response();

        LocalDateTime fechaVuelo = LocalDateTime.parse(
                f.dep_time_utc(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        AirportDTO destino =
                getAirportsByCity(f.dep_iata()).get(0);

        WeatherDTO clima = weatherService.obtenerClimaParaVuelo(
                destino.lat(),
                destino.lng(),
                f.dep_city(),
                f.dep_country(),
                fechaVuelo
        );

        return new FlightDetailDTO(
                f.airline_name(),
                f.airline_iata(),
                f.flight_iata(),
                f.dep_name(),
                f.dep_iata(),
                f.dep_country(),
                f.dep_city(),
                f.arr_name(),
                f.arr_iata(),
                f.arr_country(),
                f.arr_city(),
                f.dep_time(),
                clima
        );
    }

    //Obtener rutas por paginacion
    public PageResponse<FlightRouteDTO> getFutureRoutesByAirline(
            String airlineIata,
            int page,
            int size
    ) {

        AirLabsRoutesResponseDTO response =
                airLabsClient.getRoutes(airlineIata, apiKey);

        LocalDateTime now = LocalDateTime.now();

        List<FlightRouteDTO> filtered = response.response().stream()

                // 1️⃣ Enriquecer
                .map(r -> {
                    try {
                        FlightDetailDTO detail = getFlightDetail(r.flight_iata());
                        return mapToInternalDTO(r, detail);
                    } catch (Exception e) {
                        log.warn("Vuelo descartado {} por error", r.flight_iata());
                        return null;
                    }
                })

                // 2️⃣ Eliminar nulos
                .filter(f -> f != null)

                // 3️⃣ Información completa
                .filter(f ->
                        f.fecha() != null &&
                                f.origen() != null &&
                                f.destino() != null
                )

                // 4️⃣ Solo vuelos futuros
                .filter(f -> {
                    try {
                        LocalDateTime salida = LocalDateTime.parse(
                                f.fecha(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                        );
                        return salida.isAfter(now);
                    } catch (Exception e) {
                        return false;
                    }
                })

                // 5️⃣ Ordenar por fecha
                .sorted((a, b) -> a.horaSalida().compareTo(b.horaSalida()))

                .toList();

        // 6️⃣ Paginación manual
        int total = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);

        List<FlightRouteDTO> pageContent =
                filtered.subList(fromIndex, toIndex);

        return new PageResponse<>(
                pageContent,
                page,
                size,
                total,
                (int) Math.ceil((double) total / size)
        );
    }

}
