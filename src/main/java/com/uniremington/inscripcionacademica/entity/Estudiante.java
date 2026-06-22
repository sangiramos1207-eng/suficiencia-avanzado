package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa a los estudiantes inscritos en la institución académica.
// Se mapea con la tabla "estudiantes" en la base de datos.
@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudiante {

    // Identificador único del estudiante, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Apellidos del estudiante. Este campo es obligatorio.
    @Column(nullable = false, length = 50)
    private String apellidos;

    // Documento de identidad del estudiante (cédula, tarjeta de identidad, etc.). Debe ser único.
    @Column(length = 15, unique = true)
    private String identificacion;

    // Nombres del estudiante. Este campo es obligatorio.
    @Column(nullable = false, length = 50)
    private String nombres;

    // Semestre académico que el estudiante está cursando actualmente.
    @Column(name = "semestre_actual")
    private Integer semestreActual;

    // Carrera o programa académico al que pertenece el estudiante (ej. "Ingeniería de Sistemas").
    @ManyToOne
    @JoinColumn(name = "programa_academico_id")
    private ProgramaAcademico programaAcademico;
}
