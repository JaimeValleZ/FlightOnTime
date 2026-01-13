package com.alura.flight_prediction.controller;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import com.alura.flight_prediction.service.ConsultaVueloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VueloController.class)
class VueloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaVueloService consultaVueloService;

    @Test
    void debeRetornarRespuestaDeVuelo() throws Exception {
        // Arrange: simulamos el servicio
        DatosConsultaVuelo consulta = new DatosConsultaVuelo("SCL", "JFK", "2026-01-20");
        DatosRespuestaVuelo respuesta = new DatosRespuestaVuelo("SCL", "JFK", "2026-01-20", "LATAM", "10:00");

        when(consultaVueloService.consultarVuelo(consulta)).thenReturn(respuesta);

        // Act & Assert: enviamos un POST al controlador y validamos la respuesta
        mockMvc.perform(post("/vuelos/consulta")
                        .contentType("application/json")
                        .content("{\"origen\":\"SCL\",\"destino\":\"JFK\",\"fecha\":\"2026-01-20\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origen").value("SCL"))
                .andExpect(jsonPath("$.destino").value("JFK"))
                .andExpect(jsonPath("$.aerolinea").value("LATAM"));
    }
}