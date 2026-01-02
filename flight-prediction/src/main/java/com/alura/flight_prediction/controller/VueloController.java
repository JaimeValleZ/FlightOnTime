package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.ChurnRequest;
import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import com.alura.flight_prediction.service.ConsultaVueloService;
import com.alura.flight_prediction.service.VueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediccion")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    @Autowired
    private ConsultaVueloService consultaVueloService;

    @GetMapping("/test")
    private String test() {
        return "Test OK";
    }

    @PostMapping("/predict-churn")
    public ResponseEntity<String> predictChurn(
            @RequestBody ChurnRequest request) {

        String prediction = vueloService.obtenerPrediccion(request);
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/predict")
    public ResponseEntity<DatosRespuestaVuelo> predictChurn(
            @RequestBody DatosConsultaVuelo request) {

        DatosRespuestaVuelo prediction = vueloService.obtenerPrediccion2(request);
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/predict-from-flight/{flightIata}")
    public ResponseEntity<DatosRespuestaVuelo> predictFromFlight(
            @PathVariable String flightIata) {

        DatosConsultaVuelo datos = consultaVueloService.construirConsultaVuelo(flightIata);
        DatosRespuestaVuelo respuesta = vueloService.obtenerPrediccion2(datos);

        return ResponseEntity.ok(respuesta);
    }



}
