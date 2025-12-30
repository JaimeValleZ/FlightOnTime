Este README te guía para clonar, configurar y ejecutar los microservicios del proyecto FlightOnTime, incluyendo documentación con Swagger, seguridad básica y flujo de trabajo colaborativo en Git. 
La estructura del repositorio incluye: config-server, eureka. gateway y flight-prediction

Visión general del proyecto
- Arquitectura: Microservicios con Spring Boot.
- Servicios:
- config-server: Centraliza configuraciones.
- eureka: Registro y descubrimiento de servicios.
- gateway: Enrutamiento y entrada única.
- flight-prediction: API de predicción de vuelos con Swagger y seguridad básica.
- Lenguaje: Java.
- Repositorio: https://github.com/JaimeValleZ/FlightOnTime.

Prerrequisitos
- Java: JDK 17 (o la versión usada en el proyecto).
- Maven: 3.8+.
- Git: Última versión.
- IDE recomendado: IntelliJ IDEA.
- Puertos libres: 8080 (API), 8761 (Eureka), 8888 (Config Server), 8081/8082 (Gateway u otros, según configuración).

Tip: Si usas Windows, ejecuta IntelliJ como administrador si algún puerto está ocupado por firewall.


Clonar y abrir el proyecto
1. Clonar el repositorio:
git clone https://github.com/JaimeValleZ/FlightOnTime.git
cd FlightOnTime
2. Abrir en IntelliJ:
- File → Open → selecciona la carpeta FlightOnTime.
- Espera a que Maven resuelva dependencias.
3. Verifica la estructura:
- Debes ver las carpetas: .mvn, config-server, eureka, gateway, flight-prediction, pom.xml.

Orden de arranque de servicios
1- Config Server (puerto 8888):
- Abre config-server.
- Ejecuta la clase principal (por ejemplo, ConfigServerApplication).
- Verifica en consola: “Started … on port 8888”.
2. Eureka Server (puerto 8761):
- Abre eureka.
- Ejecuta EurekaServerApplication.
- Navega a: http://localhost:8761 para ver el dashboard.
3. Gateway (puerto 8081/8082, según config):
- Abre gateway.
- Ejecuta GatewayApplication.
- Verifica rutas configuradas hacia flight-prediction.
4. Flight Prediction (puerto 8080):
- Abre flight-prediction.
- Ejecuta FlightPredictionApplication.
- Verifica en consola: “Tomcat started on port(s): 8080”.
Nota: La carpeta flight-prediction contiene SecurityConfig, SwaggerConfig y GlobalExceptionHandler ya versionados.

Configuración de seguridad y Swagger
- Seguridad básica (Basic Auth):
- SecurityConfig.java protege endpoints con autenticación.
- Usa credenciales definidas en tu configuración (por ejemplo, usuario/contraseña en application.yml o variables de entorno).
- Swagger/OpenAPI:
- SwaggerConfig.java habilita la documentación interactiva.
- Accede a:
http://localhost:8080/swagger-ui/index.html
- Si el endpoint está protegido, Swagger pedirá autenticación.
- Manejo global de errores:
- GlobalExceptionHandler.java devuelve respuestas JSON consistentes para errores controlados.
Los archivos anteriores fueron agregados en el último commit y están visibles en flight-prediction/src/main/java/com/alura/flight_prediction/....


Probar la API
- Desde Swagger UI:
- Abre http://localhost:8080/swagger-ui/index.html.
- Explora y prueba los endpoints (ej. /predict si está disponible).
- Usa el botón “Authorize” para Basic Auth.
- Con curl (ejemplo):
curl -u usuario:password -X POST \
  http://localhost:8080/api/predict \
  -H "Content-Type: application/json" \
  -d '{"flightNumber":"LA123","departure":"SCL","arrival":"LIM","scheduledTime":"2025-12-31T10:00:00"}'
  - Con Postman:
- Importa la URL base http://localhost:8080.
- Configura “Authorization: Basic Auth”.
- Envía requests con JSON y valida respuestas.

Flujo de trabajo con Git (equipo)
- Actualizar tu trabajo:
git add .
git commit -m "Agrego configuración Swagger y corrección de estructura"
git pull origin main
git push origin main
- Ramas por feature (recomendado):
git checkout -b feature/swagger-docs
# trabaja y commitea
git push origin feature/swagger-docs
- Abre un Pull Request en GitHub para revisión.
- Verificar en GitHub:
- Entra al repo y revisa la carpeta flight-prediction y el historial de commits.

Solución de problemas comunes
- Swagger UI no carga (ERR_CONNECTION_REFUSED):
- Asegúrate de que FlightPredictionApplication esté corriendo.
- Verifica que el puerto 8080 esté libre.
- Revisa la consola por errores de arranque.
- Error de bean no encontrado (ej. VueloRepository):
- Crea la interfaz en com.alura.flight_prediction.repository:

package com.alura.flight_prediction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alura.flight_prediction.entity.Vuelo;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {}
- Asegúrate de que Vuelo esté anotada con @Entity.
- Verifica que el paquete raíz de la app sea com.alura.flight_prediction para que Spring escanee todo.
- Paquetes y rutas de archivos:
- Si ves “Package name … does not correspond to the file path …”, mueve la clase al paquete correcto (ej. config) y ajusta la línea package ….
- Autenticación en Swagger:
- Si los endpoints no aparecen o fallan, revisa SecurityConfig y las reglas de autorización.
- Usa “Authorize” en Swagger con tus credenciales.

Estructura del repositorio (resumen)
- config-server: Configuración centralizada.
- eureka: Registro de servicios.
- gateway: Enrutamiento y entrada única.
- flight-prediction: API con seguridad y Swagger.
- pom.xml: Proyecto raíz y módulos.
Puedes ver esta estructura y los últimos commits directamente en GitHub.


Próximos pasos sugeridos
- Documentar /predict con anotaciones:
- @Operation, @ApiResponses, @Parameter.
- Agregar ejemplos de request/response en Swagger.
- README técnico por servicio:
- Añadir puertos, variables, y dependencias por módulo.

- CI/CD (opcional):
- Configurar GitHub Actions para build y tests.


