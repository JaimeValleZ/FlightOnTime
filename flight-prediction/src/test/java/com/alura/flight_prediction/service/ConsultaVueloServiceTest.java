package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.service.AirLabsService;
import com.alura.flight_prediction.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests para ConsultaVueloService - Orquesta datos para ML
 * Valida: construirConsultaVuelo(flightIata) → integra AirLabs + Weather
 */
@ExtendWith(MockitoExtension.class)
class ConsultaVueloServiceTest {
/*
    @Mock private AirLabsService airLabsService;
    @Mock private WeatherService weatherService;

    @InjectMocks private ConsultaVueloService consultaVueloService;

    @Test
    @DisplayName("construirConsultaVuelo('LA123') → DatosConsultaVuelo completo")
    void construirConsultaVueloExitosa() {
        // Arrange: mocks de servicios externos
        when(airLabsService.getFlightDetail("LA123"))
                .thenReturn(/* mock FlightDetailDTO con SCL→MAD, 10700km );

        when(weatherService.obtenerClimaParaVueloML(
                33.39, -70.79,  // SCL lat/lon
                -40.49, -74.18, // MAD lat/lon
                "Santiago", "CL",
                LocalDateTime.now().plusDays(2)
        ))
                .thenReturn(new WeatherMLDTO(20.0, 0.0, 5.0));

        // Act
        DatosConsultaVuelo datos = consultaVueloService.construirConsultaVuelo("LA123");

        // Assert
        assertThat(datos.aerolinea()).isEqualTo("LATAM");
        assertThat(datos.origen()).isEqualTo("SCL");
        assertThat(datos.destino()).isEqualTo("MAD");
        assertThat(datos.distancia()).isEqualTo(10700);
        assertThat(datos.temp_mean()).isEqualTo(20.0);
        assertThat(datos.precipitation()).isZero();
        verify(airLabsService, times(1)).getFlightDetail("LA123");
        verify(weatherService, times(1)).obtenerClimaParaVueloML(anyDouble(), anyDouble(), any(), any(), any());
    }

    @Test
    @DisplayName("construirConsultaVuelo vuelo inexistente → excepción")
    void construirConsultaVueloInexistente() {
        // Arrange
        when(airLabsService.getFlightDetail("ZZ999"))
                .thenThrow(new RuntimeException("Vuelo no encontrado"));

        // Act & Assert
        assertThatThrownBy(() -> consultaVueloService.construirConsultaVuelo("ZZ999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    @DisplayName("Vuelo doméstico corto → consulta rápida")
    void construirConsultaVueloDomestico() {
        // Arrange: Santiago → Temuco (corto)
        when(airLabsService.getFlightDetail("LA100"))
                .thenReturn(/* mock SCL-TCO 680km );

        // Act
        DatosConsultaVuelo datos = consultaVueloService.construirConsultaVuelo("LA100");

        // Assert
        assertThat(datos.distancia()).isLessThan(1000);
        assertThat(datos.origen()).isEqualTo("SCL");
        assertThat(datos.destino()).isEqualTo("ZCO");
    }

    @Test
    @DisplayName("Verifica orden: AirLabs → Weather → DatosConsultaVuelo")
    void verificaFlujoCompleto() {
        // Act
        consultaVueloService.construirConsultaVuelo("LA456");

        // Assert: orden correcto de llamadas
        verify(airLabsService, times(1)).getFlightDetail("LA456");
        verify(weatherService, atLeastOnce()).obtenerClimaParaVueloML(anyDouble(), anyDouble(), any(), any(), any());
    }
    */
}
