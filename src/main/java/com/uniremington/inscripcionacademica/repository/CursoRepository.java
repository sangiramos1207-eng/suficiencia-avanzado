package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz gestiona la comunicación con la base de datos para la entidad Curso.
// Permite realizar consultas, inserciones y actualizaciones de los cursos o grupos académicos programados, aprovechando las funciones de JpaRepository.
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
