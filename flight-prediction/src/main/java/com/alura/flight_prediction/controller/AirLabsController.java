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

    @GetMapping("/{airline}")
    public List<FlightRouteDTO> getRoutes(@PathVariable String airline) {
        return airLabsService.getRoutesByAirline(airline);
    }

    //Detalles de un vuelo
    @GetMapping("/vuelo/{flight}")
    public FlightDetailDTO getFlight(@PathVariable String flight) {
        return airLabsService.getFlightDetail(flight);
    }

    @GetMapping("/airport/{airport}")
    public List<AirportDTO> getAirports(@PathVariable String airport) {
        return airLabsService.getAirportsByCity(airport);
    }

    @GetMapping("/{airline}/future")
    public ResponseEntity<List<FlightRouteDTO>> getFutureRoutes(@PathVariable String airline) {
        List<FlightRouteDTO> routes = airLabsService.getFutureRoutesByAirline(airline);
        return ResponseEntity.ok(routes);
    }


}
