package com.alura.flight_prediction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean("airlabsExecutor")
    public Executor airlabsExecutor() {
        return Executors.newFixedThreadPool(6);//Maximo 6 llamadas simultaneas, controlando el consumo
    }
}
