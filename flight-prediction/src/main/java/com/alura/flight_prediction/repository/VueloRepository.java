package com.alura.flight_prediction.repository;


import com.alura.flight_prediction.entity.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
}
