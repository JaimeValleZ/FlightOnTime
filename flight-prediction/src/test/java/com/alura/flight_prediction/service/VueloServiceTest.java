package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.MlPredictionMicroServicePy;
import com.alura.flight_prediction.dto.DatosConsultaVuelo;
import com.alura.flight_prediction.dto.DatosRespuestaVuelo;
import com.alura.flight_prediction.entity.Vuelo;
import com.alura.flight_prediction.repository.VueloRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VueloServiceTest {

    @Mock
    private MlPredictionMicroServicePy mlClient;

    @Mock
    private VueloRepository vueloRepository;

    @InjectMocks
    private VueloService vueloService;

    @Test
    @DisplayName("Buen clima → En hora")
    void vueloEnHora() {

        DatosConsultaVuelo datos = new DatosConsultaVuelo(
                "LATAM", "MAD", "SCL",
                LocalDateTime.now().plusDays(2),
                10700,
                22.0, 0.0, 4.0
        );

        when(vueloRepository.existsByAerolineaAndOrigenAndDestinoAndFechaPartidaAndDistancia(
                datos.aerolinea(), datos.origen(), datos.destino(), datos.fechaPartida(), datos.distancia()
        )).thenReturn(false);

        when(mlClient.predictChurn2(any(DatosConsultaVuelo.class)))
                .thenReturn(new DatosRespuestaVuelo("En hora", 0.2));

        DatosRespuestaVuelo respuesta = vueloService.obtenerPrediccion2(datos);

        assertThat(respuesta.prevision()).isEqualTo("En hora");
        assertThat(respuesta.probabilidad()).isLessThan(0.3);

        verify(vueloRepository).save(any(Vuelo.class));
    }

    @Test
    @DisplayName("Mal clima → Retraso probable")
    void retrasoProbable() {

        DatosConsultaVuelo datos = new DatosConsultaVuelo(
                "LATAM", "MAD", "SCL",
                LocalDateTime.now().plusDays(1),
                10700,
                10.0, 85.0, 20.0
        );

        when(vueloRepository.existsByAerolineaAndOrigenAndDestinoAndFechaPartidaAndDistancia(
                datos.aerolinea(), datos.origen(), datos.destino(), datos.fechaPartida(), datos.distancia()
        )).thenReturn(false);

        when(mlClient.predictChurn2(any(DatosConsultaVuelo.class)))
                .thenReturn(new DatosRespuestaVuelo("Retraso probable", 0.85));

        DatosRespuestaVuelo respuesta = vueloService.obtenerPrediccion2(datos);

        assertThat(respuesta.prevision()).isEqualTo("Retraso probable");
        assertThat(respuesta.probabilidad()).isGreaterThan(0.7);

        verify(vueloRepository).save(any(Vuelo.class));
    }

    @Test
    @DisplayName("Riesgo moderado")
    void riesgoModerado() {

        DatosConsultaVuelo datos = new DatosConsultaVuelo(
                "LATAM", "JFK", "SCL",
                LocalDateTime.now().plusDays(3),
                12500,
                18.0, 10.0, 8.0
        );

        when(vueloRepository.existsByAerolineaAndOrigenAndDestinoAndFechaPartidaAndDistancia(
                datos.aerolinea(), datos.origen(), datos.destino(), datos.fechaPartida(), datos.distancia()
        )).thenReturn(false);

        when(mlClient.predictChurn2(any(DatosConsultaVuelo.class)))
                .thenReturn(new DatosRespuestaVuelo("Retraso posible", 0.45));

        DatosRespuestaVuelo respuesta = vueloService.obtenerPrediccion2(datos);

        assertThat(respuesta.probabilidad()).isBetween(0.2, 0.6);
        assertThat(respuesta.prevision()).contains("Retraso");

        verify(vueloRepository).save(any(Vuelo.class));
    }
}
