package com.uniremington.inscripcionacademica.entity;

import jakarta.persistence.*;
import lombok.*;

// Esta clase representa el historial o registro de una asignatura que un estudiante ya cursó y finalizó.
// Se mapea con la tabla "asignaturas_cursadas" en la base de datos.
@Entity
@Table(name = "asignaturas_cursadas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsignaturaCursada {

    // Identificador único de este registro histórico, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Calificación o nota definitiva obtenida por el estudiante al finalizar la materia.
    @Column(name = "nota_final")
    private Double notaFinal;

    // La asignatura específica que fue cursada.
    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    // El estudiante que cursó y obtuvo la nota en esta asignatura.
    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;
}