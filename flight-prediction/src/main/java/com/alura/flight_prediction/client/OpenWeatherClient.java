package com.alura.flight_prediction.client;

import com.alura.flight_prediction.dto.weather.OpenWeatherResponseDTO;
import com.alura.flight_prediction.dto.weather.forecast.OpenWeatherForecastResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "openweather-client",
        url = "https://api.openweathermap.org/data/2.5"
)
public interface OpenWeatherClient {

    //Consultar el clima para vuelos el dia de hoy
    @GetMapping("/weather")
    OpenWeatherResponseDTO getWeather(
            @RequestParam("q") String cityCountry,
            @RequestParam("appid") String apiKey
    );

    @GetMapping("/forecast")
    OpenWeatherForecastResponseDTO getForecast(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("appid") String apiKey,
            @RequestParam("units") String units
    );
}

