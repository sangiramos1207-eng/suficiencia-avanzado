package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa una materia o asignatura que se dicta en la universidad.
// Se mapea con la tabla "asignaturas" en la base de datos.
@Entity
@Table(name = "asignaturas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asignatura {

    // Identificador único de la asignatura, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la asignatura (por ejemplo: "Cálculo", "Algoritmos"). No puede ser vacío.
    @Column(nullable = false, length = 80)
    private String nombre;

    // Cantidad de créditos que vale la materia para el plan de estudios.
    @Column(name = "numero_creditos")
    private Integer numeroCreditos;

    // Semestre o nivel académico sugerido para cursar la materia.
    @Column(name = "semestre_nivel")
    private Integer semestreNivel;

    // Departamento académico responsable de dictar la materia (ej. "Ingeniería", "Idiomas").
    @Column(length = 50)
    private String departamento;

    // Plan de estudios al cual pertenece esta asignatura.
    @ManyToOne
    @JoinColumn(name = "plan_estudio_id")
    private PlanEstudio planEstudio;

    // Indica si esta materia tiene otra asignatura como prerrequisito obligatorio.
    @ManyToOne
    @JoinColumn(name = "prerrequisito")
    private Asignatura prerrequisito;
}