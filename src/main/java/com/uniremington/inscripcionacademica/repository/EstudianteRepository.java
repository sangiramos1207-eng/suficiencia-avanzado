package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz sirve para administrar la comunicación con la base de datos de los estudiantes registrados.
// Hereda de JpaRepository para facilitar tareas habituales como guardar, consultar la ficha del estudiante y modificar su información académica.
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}
