package com.alura.flight_prediction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Vuelo")
@Table(name = "Vuelos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aerolinea;
    private String origen;
    private String destino;
    private LocalDateTime fechaPartida;
    private String prevision;
    private Float probabilidad;

}
