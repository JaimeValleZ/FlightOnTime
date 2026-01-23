package com.alura.flight_prediction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de INTEGRACIÓN COMPLETA
 * Valida que toda la aplicación arranque sin errores:
 * - Config Server / Eureka desactivados (profile test)
 * - Base de datos H2 funcionando
 * - Todos los servicios y controllers correctamente inyectados
 * - Seguridad básica desactivada para pruebas
 */
@SpringBootTest
@ActiveProfiles("test")
class FlightPredictionApplicationTest {

    @Test
    @DisplayName("✅ Contexto completo arranca correctamente")
    void contextLoads() {
        // Spring Boot valida automáticamente todos los beans
    }
}
