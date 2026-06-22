package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa un plan de estudios o malla curricular asociada a una carrera.
// Se mapea con la tabla "planes_estudio" en la base de datos.
@Entity
@Table(name = "planes_estudio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanEstudio {

    // Identificador único del plan de estudios, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Año en el que entra en vigencia o validez este plan de estudios (ej. 2026).
    @Column(name = "anio_vigencia")
    private Integer anioVigencia;

    // Nombre o descripción corta del plan (ej. "Plan de Estudios de Sistemas v2").
    @Column(length = 50)
    private String descripcion;

    // El programa académico o carrera al cual pertenece este plan de estudios.
    @ManyToOne
    @JoinColumn(name = "programa_academico_id")
    private ProgramaAcademico programaAcademico;
}