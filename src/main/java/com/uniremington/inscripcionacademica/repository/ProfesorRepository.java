package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz maneja la persistencia y lectura de los docentes en la base de datos.
// Proporciona operaciones automáticas de base de datos a través de JpaRepository para gestionar la información de los profesores de la universidad.
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
