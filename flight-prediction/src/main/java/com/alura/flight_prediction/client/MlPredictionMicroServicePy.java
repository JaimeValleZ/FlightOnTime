package com.alura.flight_prediction.client;


import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "microservice-py",
        url= "http://flight-ml:8000/"
)
public interface MlPredictionMicroServicePy {

    @PostMapping("/predict")
    DatosRespuestaVuelo predictChurn2(@RequestBody DatosConsultaVuelo request);
}
