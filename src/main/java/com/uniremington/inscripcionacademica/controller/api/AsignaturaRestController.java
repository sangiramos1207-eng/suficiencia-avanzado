package com.uniremington.inscripcionacademica.controller.api;

/*
 * REST controller para Asignatura (materias).
 * Proporciona endpoints API para obtener la lista de asignaturas.
 * Respeta convenciones REST y delega la lógica de acceso a datos al repositorio.
 */

import com.uniremington.inscripcionacademica.entity.Asignatura;
import com.uniremington.inscripcionacademica.repository.AsignaturaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaRestController {

    private final AsignaturaRepository asignaturaRepository;

    public AsignaturaRestController(
            AsignaturaRepository asignaturaRepository
    ) {
        this.asignaturaRepository = asignaturaRepository;
    }

    /**
     * GET /api/asignaturas
     * Devuelve la lista completa de asignaturas.
     * La responsabilidad de obtención la tiene el repositorio inyectado.
     */
    @GetMapping
    public List<Asignatura> listar() {
        // Delegar al repositorio para recuperar todas las asignaturas
        return asignaturaRepository.findAll();
    }
}