package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa un programa académico o carrera universitaria ofrecida por la institución.
// Se mapea con la tabla "programas_academicos" en la base de datos.
@Entity
@Table(name = "programas_academicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramaAcademico {

    // Identificador único del programa académico, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Duración estimada de la carrera (normalmente expresada en semestres). Este campo es obligatorio.
    @Column(nullable = false)
    private Integer duracion;

    // Facultad universitaria que administra la carrera (ej. "Facultad de Ingeniería"). Campo obligatorio.
    @Column(nullable = false, length = 50)
    private String facultad;

    // Nombre oficial del programa de estudio (ej. "Ingeniería de Sistemas"). Campo obligatorio.
    @Column(name = "nombre_programa", nullable = false, length = 80)
    private String nombrePrograma;

    // Título profesional que se le otorga al estudiante al graduarse (ej. "Ingeniero de Sistemas"). Campo obligatorio.
    @Column(name = "titulo_otorgado", nullable = false, length = 50)
    private String tituloOtorgado;
}