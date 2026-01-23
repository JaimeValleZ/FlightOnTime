# FlightOnTime (GOFLY) — Predicción de Retrasos en Vuelos 

FlightOnTime (GOFLY) es un MVP que predice si un vuelo saldrá **Puntual** o **Retrasado**, entregando además la probabilidad asociada a ese resultado. 
La solución combina un modelo de Machine Learning (Data Science) entrenado con datos históricos y un backend con API REST (Java/Spring Boot) para consumir la predicción en tiempo real. 

---

## Contexto y problema 

En aviación civil/logística, los retrasos generan costos operativos, problemas de conexión, insatisfacción del pasajero y fricción operacional (aerolíneas y aeropuertos). 
GOFLY busca anticipar ese riesgo usando datos del vuelo (origen/destino, hora, aerolínea, distancia) y opcionalmente variables como clima. 

---

## Arquitectura (visión general) [file:2]

El proyecto se plantea con una arquitectura distribuida basada en microservicios: `config-server`, `eureka`, `flight-prediction` y un `gateway` que actualmente “ya no se usa para acceder a endpoints”. 
El microservicio principal es `flight-prediction`, que expone endpoints para predicción y estadísticas agregadas. 

**Microservicios**
- `config-server`: configuración centralizada. 
- `eureka`: service discovery. 
- `flight-prediction`: endpoints de predicción y estadísticas. 
- `gateway`: componente presente en la arquitectura, pero actualmente no se usa para acceder a los endpoints. 

---

## Data Science — qué se ha hecho / entregables 

El equipo de Data Science entrena un modelo supervisado a partir de datos históricos de vuelos (por ejemplo: aerolínea, aeropuerto, hora, día de la semana, distancia; y potencialmente clima). 
El entregable esperado es un Notebook (Jupyter/Colab) con EDA/limpieza, feature engineering, entrenamiento de modelos (p. ej. Logistic Regression, Random Forest), evaluación (Accuracy, Precision, Recall, F1-score) y exportación del modelo serializado (joblib/pickle). 
En la demo/explicación del desafío se menciona que el modelo fue optimizado, serializado en formato Joblib y expuesto como microservicio. 

**Pipeline recomendado (detalle)**
- **EDA y limpieza**: tratamiento de nulos, tipos, outliers y validación de consistencia. 
- **Feature engineering** (ejemplos):
  - Hora del vuelo (bucketización por franja horaria).
  - Día de la semana.
  - Aeropuerto origen/destino.
  - Aerolínea.
  - Distancia (km). 
- **Modelado**:
  - Clasificación binaria: 0 = Puntual, 1 = Retrasado. 
  - Modelos sugeridos: LogisticRegression o RandomForestClassifier. 
- **Evaluación**:
  - Métricas: Accuracy, Precision, Recall, F1-score. 
- **Serialización**:
  - Exportación de modelo con `joblib`/`pickle` (para carga posterior o despliegue). 

**Contrato de integración DS → Backend (JSON)**
Entrada estándar (ejemplo): 
```json
{
  "aerolinea": "AZ",
  "origen": "GIG",
  "destino": "GRU",
  "fecha_partida": "2025-11-10T14:30:00",
  "distancia_km": 350
}

Salida estándar (ejemplo):
Json
{
  "prevision": "Puntual",
  "probabilidad": 0.22
}
Back-End — qué se ha hecho / funcionalidades actuales [file:1][file:2]
El backend es una API REST en Java/Spring Boot orientada a exponer predicciones y estadísticas, con validación de entradas usando DTOs y respuestas JSON estandarizadas. 
Según el documento, el MVP actual incluye un endpoint de predicción por número de vuelo y un endpoint de estadísticas agregadas por aerolínea. 
Endpoints principales (MVP)
•	Predicción: POST /predict/fromFlight/{numeroVuelo} → retorna "prevision" y "probabilidad". 
•	Estadísticas: GET /vuelos/stats/{aerolinea} → retorna totales agregados (total_vuelos, puntuales, retrasados). 
Ejemplo — Predicción
POST /predict/fromFlight/{numeroVuelo} 
Json
{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}

Ejemplo — Estadísticas
GET /vuelos/stats/{aerolinea}
Json
{
  "aerolinea": "AZ",
  "total_vuelos": 120,
  "puntuales": 95,
  "retrasados": 25
}
Servicios de clima
El documento menciona endpoints de clima y DTOs asociados: /forecast (pronóstico) y /weather (clima actual). 
DTOs mencionados: WeatherMLDTO(double temp_mean, double precipitation, double wind_speed) y WeatherDTO(double temperature, double precipitation, double windSpeed, boolean forecast).

Requisitos técnicos [file:2]
•	Java 17+ 
•	Maven 3+
•	Spring Boot 3+ 
•	MySQL (base de datos) 
•	Docker (opcional) 
•	Postman/cURL (para probar endpoints) 

Configuración inicial (local) 
Base de datos 
Crear la base de datos:
Sql
CREATE DATABASE flight_prediction;
Ajustar credenciales/URL de conexión MySQL en la configuración del proyecto. 
API Keys externas 
•	AirLabs: registrarse y reemplazar la API Key en AirLabService. 
•	OpenWeatherMap: registrarse y reemplazar la API Key en WeatherService. 
Ejemplo (según documento):
Java
@Value("API_KEY")
private String apiKey;

Ejecución local (microservicios) 
Clonar el repositorio:
git clone https://github.com/JaimeValleZ/FlightOnTime.git
cd FlightOnTime
Levantar servicios (en terminales separadas): 
1.	Config Server
Bash
cd config-server
mvn spring-boot:run

2.	Eureka
Bash
cd ../eureka
mvn spring-boot:run

3.	Flight Prediction
Bash
cd ../flight-prediction
mvn spring-boot:run

Pruebas automatizadas (rama test-automatizados)
Esta rama está orientada a incorporar/expandir pruebas automatizadas del backend (unitarias e integración), alineado con la funcionalidad “Pruebas automatizadas” considerada en el alcance del proyecto. 
Sugerencia de alcance mínimo: tests de controller/service para /predict/fromFlight/{numeroVuelo}, validaciones de DTO, y tests de integración con @SpringBootTest para verificar status codes y contratos JSON.

Próximos pasos (sugeridos en el documento) 
•	Levantar el front-end GOFLY. 
•	Levantar el microservicio Python del modelo ML. 
•	Opcionales: persistencia, batch prediction, explicabilidad del modelo, Docker Compose y mejoras de observabilidad. 

Licencia 
Este proyecto se distribuye bajo licencia MIT. 







