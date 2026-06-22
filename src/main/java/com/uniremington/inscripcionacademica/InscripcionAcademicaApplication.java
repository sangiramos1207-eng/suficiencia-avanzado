package com.uniremington.inscripcionacademica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta es la clase de entrada principal de nuestro proyecto.
// La anotación @SpringBootApplication se encarga de habilitar la configuración automática y el escaneo de todos nuestros componentes y servicios.
@SpringBootApplication
public class InscripcionAcademicaApplication {

    // Este es el método principal (punto de inicio) que arranca por completo la aplicación de Spring Boot.
    public static void main(String[] args) {
        SpringApplication.run(
                InscripcionAcademicaApplication.class,
                args
        );
    }
}
