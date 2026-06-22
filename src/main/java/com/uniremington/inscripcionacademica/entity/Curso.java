package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa un curso específico o grupo que se programa para una asignatura en un periodo determinado.
// Se mapea con la tabla "cursos" en la base de datos.
@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    // Identificador único del curso programado, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Salón de clases o espacio físico/virtual asignado para el curso.
    @Column(name = "aula", length = 30)
    private String aula;

    // Límite máximo de estudiantes permitidos para registrarse en este curso.
    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    // Días y horas en los que se dictará el curso (ej. "Lunes y Miércoles 8-10 AM").
    @Column(name = "horario", length = 20)
    private String horario;

    // Periodo lectivo en el que se ofrece el curso (ej. "2026-1").
    @Column(name = "periodo", length = 10)
    private String periodo;

    // La asignatura teórica a la que está vinculada la programación de este curso.
    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    // El docente o profesor encargado de impartir las clases del curso.
    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;
}