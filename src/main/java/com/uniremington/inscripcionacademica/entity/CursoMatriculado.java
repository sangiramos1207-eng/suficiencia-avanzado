package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa la matrícula o registro de un estudiante en un curso específico durante un ciclo académico.
// Se mapea con la tabla "cursos_matriculados" en la base de datos.
@Entity
@Table(name = "cursos_matriculados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoMatriculado {

    // Identificador único de este registro de matrícula, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Estado de la matrícula en el curso (ej. "Inscrito", "Aprobado", "Reprobado", "Cancelado").
    @Column(name = "estado_curso", length = 20)
    private String estadoCurso;

    // Calificación o nota final que el estudiante obtiene en este curso específico.
    @Column(name = "nota_final")
    private Double notaFinal;

    // Periodo académico en el que se cursa la materia (ej. "2026-1").
    @Column(length = 10)
    private String periodo;

    // El curso programado al cual el estudiante se está inscribiendo.
    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    // El estudiante que está matriculando este curso.
    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;
}