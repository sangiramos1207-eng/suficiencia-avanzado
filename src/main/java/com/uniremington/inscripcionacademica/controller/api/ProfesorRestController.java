package com.uniremington.inscripcionacademica.controller.api;

/*
 * REST controller para Profesor.
 * Expone endpoint para obtener un profesor por id.
 * La obtención se delega al repositorio correspondiente.
 */

import com.uniremington.inscripcionacademica.entity.Profesor;
import com.uniremington.inscripcionacademica.repository.ProfesorRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorRestController {

    private final ProfesorRepository profesorRepository;

    public ProfesorRestController(
            ProfesorRepository profesorRepository
    ) {
        this.profesorRepository = profesorRepository;
    }

    /**
     * GET /api/profesores/{id}
     * Recupera un profesor por su identificador. Si no existe, lanza NoSuchElementException.
     * @param id id del profesor
     * @return Profesor encontrado
     */
    @GetMapping("/{id}")
    public Profesor obtenerPorId(
            @PathVariable Long id
    ) {
        // Uso directo del repositorio: devolver el profesor o lanzar excepción si está ausente
        return profesorRepository
                .findById(id)
                .orElseThrow();
    }
}