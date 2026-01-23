package com.alura.flight_prediction.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class AutenticacionServiceTest {

    @Autowired
    private AutenticacionService autenticacionService;

    @Test
    void servicioSeInyectaCorrectamente() {
        assertNotNull(autenticacionService);
    }
}
