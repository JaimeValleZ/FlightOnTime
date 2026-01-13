package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultaVueloServiceTest {

    @Mock
    private AirLabsService airLabsService;

    @InjectMocks
    private ConsultaVueloService consultaVueloService;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void debeConstruirConsultaVueloCorrectamente() {
        // Arrange: simulamos los datos que devuelve AirLabsService

        FlightDetailDTO vueloSimulado = new FlightDetailDTO(
                "LATAM", "SCL", "JFK", "2026-01-20 10:00"
        );

        AirportDTO origenSimulado = new AirportDTO("SCL", "Santiago", -33.45, -70.66);
        AirportDTO destinoSimulado = new AirportDTO("JFK", "New York", 40.64, -73.78);

        when(airLabsService.getFlightDetail("LA123")).thenReturn(vueloSimulado);
        when(airLabsService.getAirportsByCity("SCL")).thenReturn(List.of(origenSimulado));
        when(airLabsService.getAirportsByCity("JFK")).thenReturn(List.of(destinoSimulado));

        // Act: ejecutamos el método del servicio
        DatosConsultaVuelo resultado = consultaVueloService.construirConsultaVuelo("LA123");

        // Assert: validamos que el DTO tenga los datos esperados
        assertNotNull(resultado);
        assertEquals("LATAM", resultado.aerolinea());
        assertEquals("JFK", resultado.destino());
        assertEquals("SCL", resultado.origen());
        assertEquals(LocalDateTime.of(2026, 1, 20, 10, 0), resultado.fechaPartida());
        assertTrue(resultado.distanciaKm() > 0);
    }
}