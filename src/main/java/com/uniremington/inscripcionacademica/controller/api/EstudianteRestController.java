package com.uniremington.inscripcionacademica.controller.api;

/*
 * REST controller para Estudiante.
 * Proporciona endpoints para listar estudiantes y obtener detalles por id.
 * Usa EstudianteService para separar la lógica de negocio de la capa de controlador.
 */

import com.uniremington.inscripcionacademica.entity.Estudiante;
import com.uniremington.inscripcionacademica.service.EstudianteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteRestController {

    private final EstudianteService estudianteService;

    public EstudianteRestController(
            EstudianteService estudianteService
    ) {
        this.estudianteService = estudianteService;
    }

    /**
     * GET /api/estudiantes
     * Devuelve todos los estudiantes registrados.
     * Delegación al servicio para obtener la lista completa.
     */
    @GetMapping
    public List<Estudiante> listar() {
        // Obtener lista desde la capa de servicio
        return estudianteService.obtenerTodos();
    }

    /**
     * GET /api/estudiantes/{id}
     * Obtiene un estudiante por su id. Lanza NoSuchElementException si no existe.
     * Se delega la búsqueda al servicio; el controlador no implementa lógica de negocio.
     *
     * @param id identificador del estudiante
     * @return Estudiante encontrado
     */
    @GetMapping("/{id}")
    public Estudiante obtenerPorId(
            @PathVariable Long id
    ) {
        // Delegar al servicio y propagar excepción si no se encuentra
        return estudianteService
                .obtenerPorId(id)
                .orElseThrow();
    }
}