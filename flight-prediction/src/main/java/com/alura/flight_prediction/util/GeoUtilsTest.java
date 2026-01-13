package com.alura.flight_prediction.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoUtilsTest {

    @Test
    void debeCalcularDistanciaCorrectaEntreSCLyJFK() {
        // Coordenadas de Santiago (SCL) y New York (JFK)
        double latSCL = -33.45;
        double lngSCL = -70.66;
        double latJFK = 40.64;
        double lngJFK = -73.78;

        // Act: calculamos la distancia
        int distanciaKm = GeoUtils.calcularDistanciaKm(latSCL, lngSCL, latJFK, lngJFK);

        // Assert: validamos que la distancia esté en el rango esperado (~8200 km)
        assertTrue(distanciaKm > 8000 && distanciaKm < 8500,
                "La distancia debería estar entre 8000 y 8500 km, pero fue: " + distanciaKm);
    }

    @Test
    void debeRetornarCeroSiCoordenadasSonIguales() {
        double lat = -33.45;
        double lng = -70.66;

        int distanciaKm = GeoUtils.calcularDistanciaKm(lat, lng, lat, lng);

        assertEquals(0, distanciaKm, "La distancia entre el mismo punto debe ser 0 km");
    }
}