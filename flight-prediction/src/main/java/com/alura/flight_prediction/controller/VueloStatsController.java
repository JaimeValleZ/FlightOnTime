package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.VueloStatsDTO;
import com.alura.flight_prediction.service.VueloStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vuelos/stats")
public class VueloStatsController {

    @Autowired
    private VueloStatsService service;

    @GetMapping("/{aerolinea}")
    public VueloStatsDTO obtenerStats(@PathVariable String aerolinea){
        return service.obtenerEstadisticasPorAerolinea(aerolinea);
    }
}