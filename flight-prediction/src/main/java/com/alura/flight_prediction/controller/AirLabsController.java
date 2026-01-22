package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.route.FlightRouteDTO;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.service.AirLabsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@SecurityRequirement(name = "bearer-key")
public class AirLabsController {

    @Autowired
    private AirLabsService airLabsService;

    //Detalles de un vuelo
    @GetMapping("/vuelo/{flight}")
    public ResponseEntity<FlightDetailDTO> getFlight(@PathVariable String flight) {
        FlightDetailDTO flightDetail = airLabsService.getFlightDetail(flight);
        return ResponseEntity.ok(flightDetail);
    }

    @GetMapping("/airport/{airport}")
    public ResponseEntity<List<AirportDTO>> getAirports(@PathVariable String airport) {
        List<AirportDTO> airports = airLabsService.getAirportsByCity(airport);
        return ResponseEntity.ok(airports);
    }

    //Rutas futuras por aerolinea
    @GetMapping("/{airline}/future")
    public ResponseEntity<List<FlightRouteDTO>> getFutureRoutes(@PathVariable String airline) {
        List<FlightRouteDTO> routes = airLabsService.getFutureRoutesByAirline(airline);
        return ResponseEntity.ok(routes);
    }


}
