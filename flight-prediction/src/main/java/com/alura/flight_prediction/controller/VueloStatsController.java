package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.stats.VuelosStatsDTO;
import com.alura.flight_prediction.service.VueloStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vuelos/stats")
public class VueloStatsController {
    private final VueloStatsService service;

    public VueloStatsController(VueloStatsService service){
        this.service = service;
    }
    @GetMapping("/{aerolinea}")
    public VuelosStatsDTO obtenerStats(@PathVariable String aerolinea){
        return service.obtenerEstadisticasPorAerolinea(aerolinea);
    }
}
