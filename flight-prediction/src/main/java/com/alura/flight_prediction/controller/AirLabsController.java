package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.PageResponse;
import com.alura.flight_prediction.dto.route.FlightRouteDTO;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.service.AirLabsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/vuelo/{flight}")
    public FlightDetailDTO getFlight(@PathVariable String flight) {
        return airLabsService.getFlightDetail(flight);
    }

    @GetMapping("/airport/{airport}")
    public List<AirportDTO> getAirports(@PathVariable String airport) {
        return airLabsService.getAirportsByCity(airport);
    }

    @GetMapping("/{airline}/future")
    public PageResponse<FlightRouteDTO> getFutureRoutes(
            @PathVariable String airline,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return airLabsService.getFutureRoutesByAirline(
                airline,
                page,
                size
        );
    }


}
