package com.alura.flight_prediction.client;


import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "microservice-py",
        url= "https://controversial-marigold-cunn-caca67d7.koyeb.app/"
        //url = "http://127.0.0.1:8000/" //Cambiar URL real a una variable de entorno o en el .yml
        //url = "http://flight-ml:8000/" //Cambiar URL real a una variable de entorno o en el .yml
)
public interface MlPredictionMicroServicePy {

    @PostMapping("/predict")
    DatosRespuestaVuelo predictChurn2(@RequestBody DatosConsultaVuelo request);
}
