package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz gestiona la comunicación con la base de datos para la entidad Asignatura.
// Al heredar de JpaRepository, tiene acceso a métodos estándar para buscar, guardar, actualizar y borrar materias de la base de datos.
@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
}
