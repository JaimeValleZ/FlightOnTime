package com.alura.flight_prediction.dto.route;


public record AirLabsRouteDTO(
        //Informacion de la aerolinea
        String airline_iata,
        String airline_icao,

        //Informacion del vuelo
        String flight_number,
        String flight_iata,

        //Codigo IATA del aeropuerto de salida
        String dep_iata,
        String dep_time,

        //Codigo IATA del aeropuerto de llegada
        String arr_iata,
        String arr_time) {
}
