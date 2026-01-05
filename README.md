FlightOnTime es un proyecto de predicción de retrasos en vuelos, desarrollado como MVP para un hackathon.
El sistema combina Data Science (modelo predictivo en Python) y Back-End (API REST en Java/Spring Boot), permitiendo consultar en tiempo real si un vuelo tiene riesgo de retraso.

 Estructura del proyecto
• 	config-server → Configuración centralizada de microservicios
• 	eureka → Servicio de descubrimiento
• 	gateway → API Gateway con seguridad y enrutamiento
• 	flight-prediction → Microservicio principal que expone el endpoint /predict

 Requisitos
• 	Java 17+
• 	Maven 3+
• 	Spring Boot 3+
• 	Docker (opcional, para contenerización)
• 	Postman/cURL (para pruebas)

 Ejecución local
1.	Clonar el repositorio:

git clone https://github.com/JaimeValleZ/FlightOnTime.git
cd FlightOnTime

2.	  Levantar los servicios en orden:

# Config Server
cd config-server
mvn spring-boot:run

# Eureka Server
cd ../eureka
mvn spring-boot:run

# Gateway
cd ../gateway
mvn spring-boot:run

# Flight Prediction
cd ../flight-prediction
mvn spring-boot:run


3.	  Acceder al endpoint principal vía Gateway:
http://localhost:8080/predict

Seguridad
El Gateway protege los endpoints con autenticación básica.
Credenciales por defecto (configurables en application.yml):
spring: 
   security: 
     user: 
       name: admin 
          password: admin123 
Ejemplo de petición con autenticación:
curl -u admin:admin123 http://localhost:8080/predict \ 
-H "Content-Type: application/json" \ 
-d '{ 
"aerolinea": "AZ", 
"origen": "GIG", 
"destino": "GRU", 
"fechaPartida": "2025-11-10T14:30:00", 
"distanciaKm": 350 
}' 

Ejemplo de respuesta
{ 
"prevision": "Retrasado", 
"probabilidad": 0.78 
} 

Funcionalidades actuales
•	Endpoint POST /predict → devuelve predicción y probabilidad
•	Validación de entradas con DTOs
•	Seguridad básica en el Gateway
•	Arquitectura distribuida con microservicios
🚀 Funcionalidades opcionales (en desarrollo)
•	GET /stats → estadísticas agregadas de vuelos
•	Persistencia en base de datos (H2/PostgreSQL)
•	Dashboard visual (Streamlit/HTML)
•	Integración con API externa de clima
•	Batch prediction (CSV)
•	Contenerización con Docker Compose
Equipo
•	Data Science → Modelo predictivo en Python (Pandas, scikit-learn)
•	Back-End → API REST en Java (Spring Boot, Gateway, Eureka, Config Server)



