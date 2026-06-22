package com.uniremington.inscripcionacademica.repository;

import com.uniremington.inscripcionacademica.entity.PlanEstudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz se encarga del acceso a datos para la entidad PlanEstudio.
// Hereda de JpaRepository para proveer métodos de consulta, guardado y actualización de los diferentes planes de estudio o mallas curriculares vigentes.
@Repository
public interface PlanEstudioRepository extends JpaRepository<PlanEstudio, Long> {
}
