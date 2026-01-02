package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.FlightRouteDTO;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.service.AirportService;
import com.alura.flight_prediction.service.FlightDetailService;
import com.alura.flight_prediction.service.FlightRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class FlightRouteController {

    @Autowired
    private FlightRouteService flightRouteService;

    @Autowired
    private FlightDetailService flightDetailService;

    @Autowired
    private AirportService airportService;

    @GetMapping("/{airline}")
    public List<FlightRouteDTO> getRoutes(@PathVariable String airline) {
        return flightRouteService.getRoutesByAirline(airline);
    }

    @GetMapping("/vuelo/{flight}")
    public FlightDetailDTO getFlight(@PathVariable String flight) {
        return flightDetailService.getFlightDetail(flight);
    }

    @GetMapping("/airport/{airport}")
    public List<AirportDTO> getAirports(@PathVariable String airport) {
        return airportService.getAirportsByCity(airport);
    }

}
