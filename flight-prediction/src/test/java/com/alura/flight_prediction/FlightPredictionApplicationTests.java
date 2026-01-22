package com.alura.flight_prediction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de INTEGRACIÓN COMPLETA
 * Valida que toda la aplicación arranque sin errores:
 * - Config Server/Eureka OFF (application-test.properties)
 * - H2 database OK
 * - Todos los servicios/controllers inyectados
 * - Seguridad básica desactivada en test
 */
@SpringBootTest
@ActiveProfiles("test")  // ← Usa config test perfecta
class FlightPredictionApplicationTests {

    @Test
    @DisplayName("✅ Contexto completo arranca - Todos los beans OK")
    void contextLoads() {
    }
    // Spring Boot valida automáticamente todos los beans

}
