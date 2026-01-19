package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.stats.VuelosStatsDTO;
import com.alura.flight_prediction.repository.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VueloStatsService {
    @Autowired
    private final VueloRepository repository;
    public VueloStatsService(VueloRepository repository){
        this.repository = repository;
    }
    public VuelosStatsDTO obtenerEstadisticasPorAerolinea(String aerolinea){
        long total = repository.countTotalByAeorlinea(aerolinea);
        long puntuales = repository.countByAeorlineaAndPrevision(aerolinea,"Puntual");
        long retrasados = repository.countByAeorlineaAndPrevision(aerolinea, "Retrasado");

        return new VuelosStatsDTO(total,puntuales,retrasados);
    }
}
