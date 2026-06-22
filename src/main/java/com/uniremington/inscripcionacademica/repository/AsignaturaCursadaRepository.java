package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.Asignatura;
import com.uniremington.inscripcionacademica.entity.AsignaturaCursada;
import com.uniremington.inscripcionacademica.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar registros de asignaturas que los estudiantes ya han cursado.
 *
 * Permite comprobar de forma eficiente si un estudiante completó una asignatura concreta,
 * lo que es útil para validaciones de prerrequisitos y para evitar que un estudiante vuelva
 * a cursar la misma materia.
 */
@Repository
public interface AsignaturaCursadaRepository
        extends JpaRepository<AsignaturaCursada, Long> {

    /**
     * Indica si existe un registro que relacione al estudiante con la asignatura dada,
     * es decir, si el estudiante ya cursó esa asignatura.
     *
     * @param estudiante el estudiante a consultar
     * @param asignatura la asignatura a verificar
     * @return true si existe al menos un registro de AsignaturaCursada para ese par
     */
    boolean existsByEstudianteAndAsignatura(
            Estudiante estudiante,
            Asignatura asignatura
    );

    java.util.List<AsignaturaCursada> findByEstudiante(
            Estudiante estudiante
    );
}