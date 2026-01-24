package com.alura.flight_prediction.service;

import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.airport.AirportDTO;
import com.alura.flight_prediction.dto.flight.FlightDetailDTO;
import com.alura.flight_prediction.dto.weather.WeatherMLDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaVueloServiceTest {

    @Mock
    private AirLabsService airLabsService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private ConsultaVueloService consultaVueloService;

    @Test
    @DisplayName("construirConsultaVuelo → construye DatosConsultaVuelo correctamente")
    void construirConsultaVueloExitosa() {

        FlightDetailDTO vuelo = new FlightDetailDTO(
                "LATAM",
                "LA",
                "LA123",
                "Santiago",
                "SCL",
                "Chile",
                "Santiago",
                "Madrid",
                "MAD",
                "España",
                "Madrid",
                "2026-01-25 10:00",
                "2026-01-26 05:00",
                780,
                null
        );

        AirportDTO origen = new AirportDTO(
                "SCL",
                -33.3929,
                -70.7858,
                "CL",
                "Arturo Merino Benítez"
        );

        AirportDTO destino = new AirportDTO(
                "MAD",
                40.4983,
                -3.5676,
                "ES",
                "Barajas"
        );

        WeatherMLDTO clima = new WeatherMLDTO(
                18.5,
                0.0,
                6.2
        );

        when(airLabsService.getFlightDetail("LA123"))
                .thenReturn(vuelo);

        when(airLabsService.getAirportsByCity("SCL"))
                .thenReturn(List.of(origen));

        when(airLabsService.getAirportsByCity("MAD"))
                .thenReturn(List.of(destino));

        when(weatherService.obtenerClimaParaVueloML(
                eq(destino.lat()),
                eq(destino.lng()),
                eq("Madrid"),
                eq("España"),
                any(LocalDateTime.class)
        )).thenReturn(clima);

        DatosConsultaVuelo datos =
                consultaVueloService.construirConsultaVuelo("LA123");

        assertThat(datos.aerolinea()).isEqualTo("LA");
        assertThat(datos.origen()).isEqualTo("SCL");
        assertThat(datos.destino()).isEqualTo("MAD");
        assertThat(datos.distancia()).isGreaterThan(10_000);
        assertThat(datos.temp_mean()).isEqualTo(18.5);
        assertThat(datos.precipitation()).isZero();
        assertThat(datos.wind_speed()).isEqualTo(6.2);

        verify(airLabsService).getFlightDetail("LA123");
        verify(airLabsService).getAirportsByCity("SCL");
        verify(airLabsService).getAirportsByCity("MAD");
        verify(weatherService).obtenerClimaParaVueloML(
                anyDouble(), anyDouble(), any(), any(), any()
        );
    }

    @Test
    @DisplayName("vuelo no encontrado → lanza excepción")
    void vueloNoEncontrado() {

        when(airLabsService.getFlightDetail("ZZ999"))
                .thenReturn(null);

        assertThatThrownBy(() ->
                consultaVueloService.construirConsultaVuelo("ZZ999")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No se pudo obtener el vuelo");

        verify(airLabsService).getFlightDetail("ZZ999");
        verifyNoInteractions(weatherService);
    }
}
