package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.weather.WeatherMLDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaVueloServiceTest {

    @Mock private AirLabsService airLabsService;
    @Mock private WeatherService weatherService;

    @InjectMocks private ConsultaVueloService consultaVueloService;

    @Test
    @DisplayName("construirConsultaVuelo('LA123') → DatosConsultaVuelo completo")
    void construirConsultaVueloExitosa() {
        // Arrange
        when(airLabsService.getFlightDetail("LA123"))
                .thenReturn(flightDetailLA123());

        when(weatherService.obtenerClimaParaVueloML(
                anyDouble(),
                anyDouble(),
                anyString(),
                anyString(),
                any(LocalDateTime.class)
        )).thenReturn(new WeatherMLDTO(20.0, 0.0, 5.0));

        // Act
        DatosConsultaVuelo datos = consultaVueloService.construirConsultaVuelo("LA123");

        // Assert
        assertThat(datos.aerolinea()).isEqualTo("LATAM");
        assertThat(datos.origen()).isEqualTo("SCL");
        assertThat(datos.destino()).isEqualTo("MAD");
        assertThat(datos.temp_mean()).isEqualTo(20.0);
        assertThat(datos.precipitation()).isZero();

        verify(airLabsService, times(1)).getFlightDetail("LA123");
        verify(weatherService, times(1))
                .obtenerClimaParaVueloML(anyDouble(), anyDouble(), anyString(), anyString(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("construirConsultaVuelo vuelo inexistente → excepción")
    void construirConsultaVueloInexistente() {
        when(airLabsService.getFlightDetail("ZZ999"))
                .thenThrow(new RuntimeException("Vuelo no encontrado"));

        assertThatThrownBy(() -> consultaVueloService.construirConsultaVuelo("ZZ999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    @DisplayName("Vuelo doméstico corto → consulta rápida")
    void construirConsultaVueloDomestico() {
        when(airLabsService.getFlightDetail("LA100"))
                .thenReturn(flightDetailLA100());

        when(weatherService.obtenerClimaParaVueloML(
                anyDouble(),
                anyDouble(),
                anyString(),
                anyString(),
                any(LocalDateTime.class)
        )).thenReturn(new WeatherMLDTO(18.0, 0.0, 4.0));

        DatosConsultaVuelo datos = consultaVueloService.construirConsultaVuelo("LA100");

        assertThat(datos.origen()).isEqualTo("SCL");
        assertThat(datos.destino()).isEqualTo("ZCO");
    }

    @Test
    @DisplayName("Verifica orden: AirLabs → Weather → DatosConsultaVuelo")
    void verificaFlujoCompleto() {
        when(airLabsService.getFlightDetail("LA456"))
                .thenReturn(flightDetailLA456());

        when(weatherService.obtenerClimaParaVueloML(
                anyDouble(),
                anyDouble(),
                anyString(),
                anyString(),
                any(LocalDateTime.class)
        )).thenReturn(new WeatherMLDTO(19.0, 0.0, 3.0));

        consultaVueloService.construirConsultaVuelo("LA456");

        verify(airLabsService, times(1)).getFlightDetail("LA456");
        verify(weatherService, atLeastOnce())
                .obtenerClimaParaVueloML(anyDouble(), anyDouble(), anyString(), anyString(), any(LocalDateTime.class));
    }

    private static FlightDetailDTO flightDetailLA123() {
        return new FlightDetailDTO(
                "LATAM",
                "LA",
                "LA123",
                "Arturo Merino Benítez",
                "SCL",
                "CL",
                "Santiago",
                "Madrid-Barajas",
                "MAD",
                "ES",
                "Madrid",
                "2026-01-23 10:00",
                "2026-01-23 22:00",
                800,     // duration (ajústalo si tu app lo usa distinto)
                null     // clima (lo mockeas por separado)
        );
    }

    private static FlightDetailDTO flightDetailLA100() {
        return new FlightDetailDTO(
                "LATAM",
                "LA",
                "LA100",
                "Arturo Merino Benítez",
                "SCL",
                "CL",
                "Santiago",
                "La Araucanía",
                "ZCO",
                "CL",
                "Temuco",
                "2026-01-22 10:00",
                "2026-01-22 11:20",
                80,
                null
        );
    }

    private static FlightDetailDTO flightDetailLA456() {
        return new FlightDetailDTO(
                "LATAM",
                "LA",
                "LA456",
                "Arturo Merino Benítez",
                "SCL",
                "CL",
                "Santiago",
                "Jorge Chávez",
                "LIM",
                "PE",
                "Lima",
                "2026-01-23 12:00",
                "2026-01-23 15:00",
                180,
                null
        );
    }
}
