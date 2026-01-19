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
import com.alura.flight_prediction.dto.weather.WeatherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class AirLabsService {

    @Autowired
    @Qualifier("airlabsExecutor")
    private Executor airlabsExecutor;

    private static final Logger log = LoggerFactory.getLogger(AirLabsService.class);

    private final AirLabsClient airLabsClient;
    private final WeatherService weatherService;


    @Value("e24bb432-5985-4b2b-8187-4204a45b9f2d")
    private String apiKey;

    public AirLabsService(AirLabsClient airLabsClient, WeatherService weatherService
    ) {
        this.airLabsClient = airLabsClient;
        this.weatherService = weatherService;
    }

    /* Obtener detalles de un vuelo sin llamar a la API del clima
     *  y evitar llamar la funcion de obtener aeropuerto.
     *  Creado para reducir el tiempo de respuesta de la API.*/
    @Cacheable(
            value = "flight-detail",
            key = "#flightIata",
            unless = "#result == null"
    )
    public FlightDetailDTO getFlightSpecificDetail(String flightIata) {

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
                f.arr_time(),
                f.duration(),
                null
        );
    }


    /*Obtener informacion de aeropuertos*/
    public List<AirportDTO> getAirportsByCity(String airportCode) {

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

    /* Metodo para obtener detalles de un vuelo con clima*/
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
                f.arr_time(),
                f.duration(),
                clima
        );
    }

    // Obtener rutas futuras filtradas (sin paginación)
    public List<FlightRouteDTO> getFutureRoutesByAirline(String airlineIata) {

        AirLabsRoutesResponseDTO response =
                airLabsClient.getRoutes(airlineIata, apiKey);

        if (response == null || response.response() == null) {
            return List.of();
        }

        LocalDateTime now = LocalDateTime.now();

        List<CompletableFuture<FlightRouteDTO>> futures =
                response.response().stream()

                        // filtro rápido ANTES de llamar APIs
                        .filter(r -> r.flight_iata() != null && r.dep_time() != null)

                        .map(r -> CompletableFuture.supplyAsync(() -> {
                            try {
                                FlightDetailDTO detail =
                                        getFlightSpecificDetail(r.flight_iata());
                                return mapToInternalDTO(r, detail);
                            } catch (Exception e) {
                                log.warn("Descartado vuelo {}", r.flight_iata());
                                return null;
                            }
                        }, airlabsExecutor))

                        .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .filter(f -> f.fecha() != null)
                .filter(f -> f.fechaDateTime().isAfter(now))
                .sorted(Comparator.comparing(FlightRouteDTO::horaSalida))
                .toList();
    }

    /* Mapear DTO con informacion de rutas y detalle de vuelo*/
    private FlightRouteDTO mapToInternalDTO(
            AirLabsRouteDTO r,
            FlightDetailDTO detail
    ) {
        if (detail == null || detail.horaSalida() == null) return null;

        LocalDateTime salida = LocalDateTime.parse(
                detail.horaSalida(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        return new FlightRouteDTO(
                r.airline_iata(),
                r.flight_iata(),
                r.dep_iata(),
                r.arr_iata(),
                detail.origen(),
                detail.destino(),
                detail.horaSalida(),
                salida,
                r.dep_time(),
                r.arr_time()
        );
    }



}
