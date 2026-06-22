package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.ProgramaAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz se encarga del acceso a datos para la entidad ProgramaAcademico.
// Hereda de JpaRepository, permitiendo guardar, modificar, borrar y consultar los programas académicos (carreras) registrados en la institución.
@Repository
public interface ProgramaAcademicoRepository extends JpaRepository<ProgramaAcademico, Long> {
}
