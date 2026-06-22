# Sistema de Inscripción Académica

Proyecto Spring Boot para gestionar estudiantes y matrículas.

## Resumen
Aplicación web en Java 17 con Spring Boot, Spring MVC, Spring Data JPA, Spring Security y Thymeleaf.
Incluye:
- Gestión de estudiantes (listar, crear, editar, eliminar).
- Matrículas con validaciones (prerrequisitos, límites de créditos).
- Exportar reportes a Excel y generación de PDF.
- Subida de archivos a disco (uploads).
- Recursos estáticos: CSS en `src/main/resources/static/css/style.css`.

## Tecnologías
- Java 17
- Spring Boot (parent: 4.1.0)
- Spring Security
- Spring Data JPA (MySQL)
- Thymeleaf (plantillas en `src/main/resources/templates`)
- Apache POI (exportar Excel)
- OpenPDF (generación PDF)

## Estructura principal
- src/main/java/com/uniremington/inscripcionacademica : código fuente
  - controller : controladores web (HomeController, LoginController, etc.)
  - config : configuración (ej. SecurityConfig.java, DataInitializer.java)
  - entity, repository, service : modelo y lógica de negocio
- src/main/resources
  - templates : vistas Thymeleaf (`login.html`, `estudiantes.html`, `estudiante-form.html`, ...)
  - static : recursos estáticos (`/css/style.css`, imágenes, JS)
- pom.xml : configuración Maven y dependencias

## Variables de entorno (requeridas)
La aplicación usa variables de entorno para la conexión a la base de datos y el puerto.
- SPRING_DATASOURCE_URL: URL JDBC
- SPRING_DATASOURCE_USERNAME: usuario MySQL
- SPRING_DATASOURCE_PASSWORD: contraseña MySQL
- PORT (opcional): puerto HTTP (por defecto 8080)

## Compilar y ejecutar
- Compilar: `mvn clean package`
- Ejecutar desde Maven: `mvn spring-boot:run`
- Ejecutable JAR: `java -jar target/suficiencia-avanzado-0.0.1-SNAPSHOT.jar`

## Datos y archivos generados en arranque
- `DataInitializer` (src/.../config/DataInitializer.java) siembra datos de ejemplo al iniciar la aplicación si la BD está vacía.
- Carpeta de uploads: los archivos subidos se guardan en `<raíz-del-proyecto>/uploads` (creada automáticamente).
- Recursos estáticos: `src/main/resources/static/css/style.css` contiene estilos y es referenciado desde las plantillas.

## Credenciales de desarrollo
La configuración actual incluye un usuario en memoria (solo para desarrollo):
- usuario: `admin`
- contraseña: `1234`

En producción:
- No usar InMemoryUserDetailsManager.
- Usar BCryptPasswordEncoder y persistir usuarios en la base de datos.
- Revisar CSRF/CORS y habilitar HTTPS.

## Endpoints y vistas importantes
- GET `/` -> redirige a `/estudiantes`
- GET `/estudiantes` -> lista de estudiantes (plantilla `estudiantes.html`)
- GET `/estudiantes/nuevo` -> formulario para crear estudiante (`estudiante-form.html`)
- POST `/estudiantes/guardar` -> guardar/actualizar estudiante
- GET `/estudiantes/detalle/{id}` -> detalle del estudiante (`estudiante-detalle.html`)
- GET `/reportes/estudiantes/excel` -> descarga Excel
- GET `/reportes/estudiante/{id}/pdf` -> descarga PDF de un estudiante
- GET `/archivos` -> página para subir archivos (`archivo-form.html`)
- POST `/archivos/subir` -> subida de archivos (guarda en `uploads`)
- GET `/login` -> formulario de inicio de sesión (plantilla `login.html`)
- POST `/login` -> autenticación (procesada por Spring Security)
- POST `/logout` -> cerrar sesión

## Despliegue
Se ha documentado despliegue en Railway; validar la configuración remota (variables, credenciales y driver MySQL) antes de confiar en ella.

## Notas de seguridad y producción
- CSRF está deshabilitado en `SecurityConfig.java` (revisar y habilitar según requisitos).
- Usuario en memoria (`admin`/`1234`) es para pruebas: cambiar antes de producción.
- Forzar uso de BCryptPasswordEncoder y políticas de CORS/CSRF/HTTPS en producción.
- Revisar compatibilidad entre la versión de Spring Boot (4.1.0) y la JDK en el entorno de despliegue.

## Archivos relevantes para revisar
- `src/main/java/.../config/SecurityConfig.java` — configuración de seguridad y usuario de prueba
- `src/main/java/.../config/DataInitializer.java` — carga inicial de datos de ejemplo
- `src/main/resources/templates/login.html`, `estudiantes.html`, `estudiante-form.html`, `estudiante-detalle.html` — vistas Thymeleaf
- `src/main/resources/static/css/style.css` — estilos principales
- `pom.xml` — dependencias (incluye Thymeleaf, Apache POI, OpenPDF)
