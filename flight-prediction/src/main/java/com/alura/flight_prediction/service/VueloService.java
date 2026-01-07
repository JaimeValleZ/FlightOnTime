package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.MlPredictionMicroServicePy;
import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import com.alura.flight_prediction.entity.Vuelo;
import com.alura.flight_prediction.repository.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VueloService {

    @Autowired
    private VueloRepository vueloRepository;

    private final MlPredictionMicroServicePy mlClient;

    public VueloService(MlPredictionMicroServicePy mlClient) {
        this.mlClient = mlClient;
    }

    public DatosRespuestaVuelo obtenerPrediccion2(DatosConsultaVuelo request) {
        var response = mlClient.predictChurn2(request);
        var vuelo = new Vuelo(null, request.aerolinea(), request.origen(), request.destino(),
                request.fechaPartida(), request.distancia(), response.prevision(), response.probabilidad());
        vueloRepository.save(vuelo);
        return mlClient.predictChurn2(request);
    }
}
