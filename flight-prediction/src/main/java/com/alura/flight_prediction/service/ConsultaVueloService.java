package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ConsultaVueloService {

    private final AirLabsService airLabsService;

    public ConsultaVueloService(
            AirLabsService airLabsService) {
        this.airLabsService = airLabsService;
    }

    public DatosConsultaVuelo construirConsultaVuelo(String flightIata) {

        // 1️⃣ Datos del vuelo
        FlightDetailDTO vuelo = airLabsService.getFlightDetail(flightIata);

        // 2️⃣ Aeropuertos
        AirportDTO origen = airLabsService.getAirportsByCity(vuelo.origenIata()).get(0);
        AirportDTO destino = airLabsService.getAirportsByCity(vuelo.destinoIata()).get(0);

        // 3️⃣ Distancia
        int distanciaKm = GeoUtils.calcularDistanciaKm(
                origen.lat(), origen.lng(),
                destino.lat(), destino.lng()
        );

        // 4️⃣ Fecha de salida
        LocalDateTime fechaPartida = LocalDateTime.parse(
                vuelo.horaSalida(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        // 5️⃣ DTO final para ML
        return new DatosConsultaVuelo(
                vuelo.aerolineaIata(),
                vuelo.destinoIata(),
                vuelo.origenIata(),
                fechaPartida,
                distanciaKm
        );
    }
}
