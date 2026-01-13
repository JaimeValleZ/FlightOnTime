package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.dto.weather.WeatherMLDTO;
import com.alura.flight_prediction.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class ConsultaVueloService {

    private final AirLabsService airLabsService;
    private final WeatherService weatherService;

    public ConsultaVueloService(AirLabsService airLabsService, WeatherService weatherService) {
        this.airLabsService = airLabsService;
        this.weatherService = weatherService;
    }

    public DatosConsultaVuelo construirConsultaVuelo(String flightIata) {

        FlightDetailDTO vuelo =
                airLabsService.getFlightDetail(flightIata);

        if (vuelo == null) {
            throw new IllegalArgumentException("No se pudo obtener el vuelo");
        }

        AirportDTO origen =
                airLabsService.getAirportsByCity(vuelo.origenIata())
                        .get(0);

        AirportDTO destino =
                airLabsService.getAirportsByCity(vuelo.destinoIata())
                        .get(0);

        int distanciaKm = GeoUtils.calcularDistanciaKm(
                origen.lat(), origen.lng(),
                destino.lat(), destino.lng()
        );

        LocalDateTime fechaPartida = LocalDateTime.parse(
                vuelo.horaSalida(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        WeatherMLDTO clima =
                weatherService.obtenerClimaParaVueloML(
                        destino.lat(),
                        destino.lng(),
                        vuelo.ciudadDestino(),
                        vuelo.paisDestino(),
                        fechaPartida
                );

        return new DatosConsultaVuelo(
                vuelo.aerolineaIata(),
                vuelo.destinoIata(),
                vuelo.origenIata(),
                fechaPartida,
                distanciaKm,
                clima.temp_mean(),
                clima.precipitation(),
                clima.wind_speed()
        );
    }

}
