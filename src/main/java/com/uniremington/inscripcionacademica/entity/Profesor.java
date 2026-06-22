package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa a los profesores o docentes de la institución educativa.
// Se mapea con la tabla "profesores" en la base de datos.
@Entity
@Table(name = "profesores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profesor {

    // Identificador único del profesor, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Apellidos del profesor. Este campo es obligatorio.
    @Column(nullable = false, length = 50)
    private String apellidos;

    // Nombres del profesor. Este campo es obligatorio.
    @Column(nullable = false, length = 50)
    private String nombres;

    // Departamento académico al que pertenece el profesor (ej. "Ciencias de la Computación").
    @Column(length = 50)
    private String departamento;

    // Especialidad o título de posgrado del profesor (ej. "Doctor en Ingeniería de Software").
    @Column(length = 80)
    private String especializacion;
}