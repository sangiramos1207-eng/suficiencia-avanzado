package com.uniremington.inscripcionacademica.service;

import com.uniremington.inscripcionacademica.entity.CursoMatriculado;
import com.uniremington.inscripcionacademica.repository.CursoMatriculadoRepository;
import com.uniremington.inscripcionacademica.entity.Estudiante;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio que encapsula operaciones relacionadas con las matrículas de cursos.
// Centraliza la lógica de acceso a datos a través de CursoMatriculadoRepository y ofrece métodos
// para guardar y recuperar matrículas; mantiene la capa de controlador libre de detalles de persistencia.
@Service
public class CursoMatriculadoService {

    private final CursoMatriculadoRepository cursoMatriculadoRepository;

    public CursoMatriculadoService(
            CursoMatriculadoRepository cursoMatriculadoRepository
    ) {
        this.cursoMatriculadoRepository = cursoMatriculadoRepository;
    }

    // Guarda o actualiza un registro de matrícula en la base de datos.
    // Recibe un objeto CursoMatriculado ya validado por el controlador y devuelve la entidad persistida.
    public CursoMatriculado guardar(
            CursoMatriculado cursoMatriculado
    ) {
        return cursoMatriculadoRepository.save(cursoMatriculado);
    }

    // Recupera todas las matrículas registradas.
    // Útil para listados y operaciones administrativas; delega a CursoMatriculadoRepository.findAll().
    public List<CursoMatriculado> obtenerTodos() {
        return cursoMatriculadoRepository.findAll();
    }

    /**
     * Recupera todas las matrículas asociadas a un estudiante específico.
     * Utiliza el repositorio para obtener el listado completo y lo devuelve tal cual
     * para que los controladores o vistas puedan procesarlo (por ejemplo, calcular créditos).
     *
     * @param estudiante el estudiante cuyas matrículas se desean obtener
     * @return lista de CursoMatriculado del estudiante
     */
    public List<CursoMatriculado> obtenerPorEstudiante(
            Estudiante estudiante
    ) {
        return cursoMatriculadoRepository
                .findByEstudiante(estudiante);
    }
}