package com.alura.flight_prediction.service;

import com.alura.flight_prediction.client.OpenWeatherClient;
import com.alura.flight_prediction.dto.weather.OpenWeatherResponseDTO;
import com.alura.flight_prediction.dto.weather.WeatherDTO;
import com.alura.flight_prediction.dto.weather.WeatherMLDTO;
import com.alura.flight_prediction.dto.weather.forecast.ForecastItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Service
public class WeatherService {

    private final OpenWeatherClient weatherClient;

    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    public WeatherService(OpenWeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    /* =========================
       🌤️ CLIMA ACTUAL
       ========================= */

    public WeatherDTO obtenerClimaActual(String ciudad, String pais) {

        OpenWeatherResponseDTO response =
                weatherClient.getWeather(ciudad + "," + pais, apiKey);

        double temp = response.main().temp() - 273.15;

        double precipitation =
                response.rain() != null && response.rain().oneHour() != null
                        ? response.rain().oneHour()
                        : 0.0;

        return new WeatherDTO(
                round(temp),
                precipitation,
                round(response.wind().speed()),
                false
        );
    }

    /* =========================
       🌦️ FORECAST (≤ 5 días)
       ========================= */

    public WeatherDTO obtenerClimaForecast(
            double lat,
            double lon,
            LocalDateTime fechaVuelo
    ) {

        long dias = ChronoUnit.DAYS.between(LocalDateTime.now(), fechaVuelo);
        if (dias > 5) {
            throw new IllegalArgumentException(
                    "OpenWeather solo permite forecast hasta 5 días"
            );
        }

        var response =
                weatherClient.getForecast(lat, lon, apiKey, "metric");

        long targetEpoch =
                fechaVuelo.toEpochSecond(ZoneOffset.UTC);

        ForecastItemDTO closest = response.list().stream()
                .min(Comparator.comparingLong(
                        f -> Math.abs(f.dt() - targetEpoch)
                ))
                .orElseThrow();

        return new WeatherDTO(
                round(closest.main().temp()),
                closest.pop() != null ? round(closest.pop() * 100) : 0.0,
                round(closest.wind().speed()),
                true
        );
    }

    /* =========================
       🧠 DECISIÓN CENTRALIZADA
       ========================= */

    public WeatherDTO obtenerClimaParaVuelo(
            double lat,
            double lon,
            String ciudad,
            String pais,
            LocalDateTime fechaVuelo
    ) {

        long horas = java.time.Duration.between(
                LocalDateTime.now(),
                fechaVuelo
        ).toHours();

        // ⛅ Forecast: entre 6h y 5 días
        if (horas > 6 && horas <= 120) {
            return obtenerClimaForecast(lat, lon, fechaVuelo);
        }

        // 🌦️ Clima actual
        return obtenerClimaActual(ciudad, pais);
    }

    /* =========================
       🤖 ML
       ========================= */

    public WeatherMLDTO obtenerClimaParaVueloML(
            double lat,
            double lon,
            String ciudad,
            String pais,
            LocalDateTime fechaVuelo
    ) {

        WeatherDTO clima = obtenerClimaParaVuelo(
                lat, lon, ciudad, pais, fechaVuelo
        );

        return new WeatherMLDTO(
                clima.temperature(),
                clima.precipitation(),
                clima.windSpeed()
        );
    }

    /* ========================= */

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
