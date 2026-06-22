package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.Curso;
import com.uniremington.inscripcionacademica.entity.CursoMatriculado;
import com.uniremington.inscripcionacademica.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio responsable de las operaciones de persistencia para la entidad CursoMatriculado.
 *
 * Proporciona métodos específicos para:
 * - Comprobar si un estudiante ya está inscrito en un curso.
 * - Obtener las matrículas de un estudiante en un periodo determinado.
 *
 * Extiende JpaRepository para heredar las operaciones CRUD estándar.
 */
@Repository
public interface CursoMatriculadoRepository extends JpaRepository<CursoMatriculado, Long> {

    /**
     * Verifica si existe una matrícula para el estudiante en el curso proporcionado.
     * Se usa para evitar inscripciones duplicadas en el mismo curso.
     *
     * @param estudiante el estudiante a verificar
     * @param curso el curso a verificar
     * @return true si ya existe una matrícula para ese par estudiante-curso
     */
    boolean existsByEstudianteAndCurso(
            Estudiante estudiante,
            Curso curso
    );

    /**
     * Obtiene la lista de matrículas que un estudiante tiene en un periodo concreto.
     * Esto permite, por ejemplo, calcular el total de créditos actuales en ese periodo.
     *
     * @param estudiante el estudiante cuya matrícula se consulta
     * @param periodo el periodo académico (por ejemplo "2026-I")
     * @return lista de CursoMatriculado del estudiante en el periodo indicado
     */
    List<CursoMatriculado> findByEstudianteAndPeriodo(
            Estudiante estudiante,
            String periodo
    );

    /**
     * Obtiene todas las matrículas de un estudiante, sin filtrar por periodo.
     * Se utiliza para mostrar el historial o el detalle completo de inscripciones del alumno.
     *
     * @param estudiante el estudiante cuya lista de matrículas se solicita
     * @return lista de CursoMatriculado asociados al estudiante
     */
    List<CursoMatriculado> findByEstudiante(
            Estudiante estudiante
    );
}
