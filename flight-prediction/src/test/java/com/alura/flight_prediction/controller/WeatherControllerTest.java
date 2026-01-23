package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import com.alura.flight_prediction.service.ConsultaVueloService;
import com.alura.flight_prediction.service.VueloService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VueloController.class)  // Prueba el controlador principal
class WeatherControllerTest {  // Mantiene nombre original

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private VueloService vueloService;
    @MockBean private ConsultaVueloService consultaVueloService;

    @Test
    @DisplayName("GET /prediccion/test → 'Test OK' 200")
    void testEndpoint() throws Exception {
        mockMvc.perform(get("/prediccion/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Test OK"));
    }

    @Test
    @DisplayName("POST /prediccion/predict-from-flight/LA123 → 200 predicción OK")
    void predictFromFlightOk() throws Exception {
        // Arrange: mocks de servicios
        DatosConsultaVuelo datosMock = new DatosConsultaVuelo(
                "LATAM", "MAD", "SCL",
                LocalDateTime.now().plusDays(2),
                10700, 20.0, 0.0, 5.0
        );

        when(consultaVueloService.construirConsultaVuelo("LA123"))
                .thenReturn(datosMock);
        when(vueloService.obtenerPrediccion2(any()))
                .thenReturn(new DatosRespuestaVuelo("En hora", 0.15));

        // Act & Assert
        mockMvc.perform(get("/prediccion/predict-from-flight/LA123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prevision").value("En hora"))
                .andExpect(jsonPath("$.probabilidad").value(0.15));
    }

    @Test
    @DisplayName("POST /predict-from-flight vuelo inválido → 400/500")
    void predictFromFlightInvalido() throws Exception {
        // Arrange: simular error en servicio
        when(consultaVueloService.construirConsultaVuelo("ZZ999"))
                .thenThrow(new IllegalArgumentException("Vuelo no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/prediccion/predict-from-flight/ZZ999"))
                .andExpect(status().isBadGateway())  // O is5xxServerError()
                .andExpect(jsonPath("$.error").exists());
    }
}
