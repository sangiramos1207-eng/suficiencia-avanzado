package com.uniremington.inscripcionacademica.service;

import com.uniremington.inscripcionacademica.entity.Estudiante;
import com.uniremington.inscripcionacademica.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Servicio encargado de la lógica de negocio relacionada con los estudiantes.
// Actúa como intermediario entre el controlador y el repositorio.
@Service
public class EstudianteService {

    // Repositorio utilizado para acceder y gestionar los datos de los estudiantes.
    private final EstudianteRepository estudianteRepository;

    // Constructor que inyecta el repositorio de estudiantes.
    public EstudianteService(
            EstudianteRepository estudianteRepository
    ) {
        this.estudianteRepository = estudianteRepository;
    }

    // Obtiene la lista completa de estudiantes registrados.
    public List<Estudiante> obtenerTodos() {
        return estudianteRepository.findAll();
    }

    // Busca un estudiante por su identificador único.
    public Optional<Estudiante> obtenerPorId(Long id) {
        return estudianteRepository.findById(id);
    }

    // Guarda un nuevo estudiante o actualiza la información de uno existente.
    public Estudiante guardar(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    // Elimina un estudiante utilizando su identificador.
    public void eliminar(Long id) {
        estudianteRepository.deleteById(id);
    }
}