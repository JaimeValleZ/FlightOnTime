package com.alura.flight_prediction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FlightPredictionApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * ✅ Verifica que el contexto de Spring Boot se cargue correctamente.
	 */
	@Test
	void contextLoads() {
	}

	/**
	 * ✅ Prueba mínima de integración:
	 * Con un request válido, el endpoint /predict devuelve 200 OK
	 * y contiene los campos esperados en la respuesta.
	 */
	@Test
	void predictFlight_withValidRequest_returnsPrediction() throws Exception {
		Map<String, Object> request = Map.of(
				"aerolinea", "AZ",
				"origen", "GIG",
				"destino", "GRU",
				"fechaPartida", "2025-11-10T14:30:00",
				"distanciaKm", 350
		);

		mockMvc.perform(post("/predict")
						.with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123"))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.prevision").exists())
				.andExpect(jsonPath("$.probabilidad").isNumber());
	}
}
