package com.uniremington.inscripcionacademica.controller;

import com.uniremington.inscripcionacademica.entity.Asignatura;
import com.uniremington.inscripcionacademica.entity.Curso;
import com.uniremington.inscripcionacademica.entity.CursoMatriculado;
import com.uniremington.inscripcionacademica.entity.Estudiante;
import com.uniremington.inscripcionacademica.entity.AsignaturaCursada;
import com.uniremington.inscripcionacademica.repository.CursoRepository;
import com.uniremington.inscripcionacademica.repository.EstudianteRepository;
import com.uniremington.inscripcionacademica.repository.AsignaturaCursadaRepository;
import com.uniremington.inscripcionacademica.service.CursoMatriculadoService;
import com.uniremington.inscripcionacademica.repository.CursoMatriculadoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador responsable de las acciones de matrícula de cursos para estudiantes.
// Valida prerrequisitos, evita duplicados, controla el límite de créditos y delega la persistencia al servicio de matrícula.
@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    // Servicio que encapsula la persistencia y operaciones sobre CursoMatriculado.
    private final CursoMatriculadoService cursoMatriculadoService;

    // Repositorio para obtener información de estudiantes desde la base de datos.
    private final EstudianteRepository estudianteRepository;

    // Repositorio que permite consultar los cursos disponibles.
    private final CursoRepository cursoRepository;

    // Repositorio para consultar si un estudiante ya cursó una asignatura determinada.
    private final AsignaturaCursadaRepository asignaturaCursadaRepository;

    // Repositorio para consultas específicas sobre matrículas (existencia, listados por periodo, etc.).
    private final CursoMatriculadoRepository cursoMatriculadoRepository;

    // Constructor que recibe las dependencias necesarias por inyección.
    // Mantiene el controlador centrado en la orquestación de la petición HTTP y delega la lógica de datos.
    public MatriculaController(
            CursoMatriculadoService cursoMatriculadoService,
            EstudianteRepository estudianteRepository,
            CursoRepository cursoRepository,
            AsignaturaCursadaRepository asignaturaCursadaRepository,
            CursoMatriculadoRepository cursoMatriculadoRepository
    ) {
        this.cursoMatriculadoService = cursoMatriculadoService;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.asignaturaCursadaRepository = asignaturaCursadaRepository;
        this.cursoMatriculadoRepository = cursoMatriculadoRepository;
    }

    // Muestra el formulario de matrícula para un estudiante determinado.
    // Carga el estudiante y la lista de cursos disponibles en el modelo para la vista.
    @GetMapping("/nueva/{estudianteId}")
    public String nuevaMatricula(
            @PathVariable Long estudianteId,
            Model model
    ) {

        // Recupera el estudiante por su id; orElseThrow() lanza si no existe.
        Estudiante estudiante =
                estudianteRepository.findById(estudianteId)
                        .orElseThrow();

        // Añade al modelo el estudiante y la lista completa de cursos para el select del formulario.
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("cursos", cursoRepository.findAll());

        // Obtener asignaturas cursadas por el estudiante (materias aprobadas)
        List<AsignaturaCursada> asignaturasCursadas =
                asignaturaCursadaRepository
                        .findByEstudiante(estudiante);

        // Obtener matrículas actuales del periodo específico (ejemplo 2026-1)
        List<CursoMatriculado> matriculasActuales =
                cursoMatriculadoRepository
                        .findByEstudianteAndPeriodo(
                                estudiante,
                                "2026-1"
                        );

        int creditosActuales = 0;

        for (CursoMatriculado matricula : matriculasActuales) {

            creditosActuales +=
                    matricula.getCurso()
                            .getAsignatura()
                            .getNumeroCreditos();
        }

        // Enviar datos académicos al modelo para la vista
        model.addAttribute(
                "asignaturasCursadas",
                asignaturasCursadas
        );

        model.addAttribute(
                "matriculasActuales",
                matriculasActuales
        );

        model.addAttribute(
                "creditosActuales",
                creditosActuales
        );

        // Retorna el nombre de la plantilla Thymeleaf que muestra el formulario.
        return "matricula-form";
    }

    /**
     * Elimina una matrícula por su identificador.
     *
     * Flujo:
     *  - Recupera la entidad CursoMatriculado por id (lanza si no existe).
     *  - Obtiene el id del estudiante relacionado para regresar a su vista de detalle.
     *  - Elimina la matrícula y redirige al detalle del estudiante eliminado.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarMatricula(
            @PathVariable Long id
    ) {

        // Buscar la matrícula; orElseThrow interrumpe si el id no es válido.
        CursoMatriculado matricula =
                cursoMatriculadoRepository
                        .findById(id)
                        .orElseThrow();

        // Guardar el id del estudiante antes de borrar la matrícula para poder redirigir.
        Long estudianteId =
                matricula.getEstudiante().getId();

        // Borrar la matrícula de la base de datos.
        cursoMatriculadoRepository.delete(matricula);

        // Redirigir al detalle del estudiante para que la interfaz muestre el estado actualizado.
        return "redirect:/estudiantes/detalle/" + estudianteId;
    }

    // Procesa el envío del formulario de matrícula.
    // Realiza varias validaciones antes de crear la matrícula:
    // 1) Verifica prerrequisito (si existe).
    // 2) Impide que el estudiante vuelva a cursar una asignatura ya aprobada.
    // 3) Evita duplicar la inscripción en el mismo curso.
    // 4) Controla el máximo de créditos permitidos por periodo.
    @PostMapping("/guardar")
    public String guardarMatricula(
            @RequestParam Long estudianteId,
            @RequestParam Long cursoId
    ) {

        // Cargar estudiante y curso seleccionados desde la base de datos.
        Estudiante estudiante =
                estudianteRepository.findById(estudianteId)
                        .orElseThrow();

        Curso curso =
                cursoRepository.findById(cursoId)
                        .orElseThrow();

        // 1) Comprobar prerrequisito: si el curso tiene una asignatura prerrequisito,
        //    verificar que el estudiante ya la haya cursado (usando AsignaturaCursadaRepository).
        Asignatura prerrequisito =
                curso.getAsignatura()
                        .getPrerrequisito();

        if (prerrequisito != null) {

            boolean cumplePrerrequisito =
                    asignaturaCursadaRepository
                            .existsByEstudianteAndAsignatura(
                                    estudiante,
                                    prerrequisito
                            );

            // Si no cumple el prerrequisito, redirigir con un código de error.
            if (!cumplePrerrequisito) {

                return "redirect:/estudiantes?error=prerrequisito";
            }
        }

        // 2) Evitar que el estudiante vuelva a cursar la misma asignatura si ya la completó.
        boolean yaCursoAsignatura =
                asignaturaCursadaRepository
                        .existsByEstudianteAndAsignatura(
                                estudiante,
                                curso.getAsignatura()
                        );

        if (yaCursoAsignatura) {

            // Redirige con un parámetro de error que la vista interpreta para mostrar mensaje.
            return "redirect:/estudiantes?error=asignatura-cursada";
        }

        // 3) Verificar que el estudiante no esté ya matriculado en este mismo curso.
        boolean yaMatriculado =
                cursoMatriculadoRepository
                        .existsByEstudianteAndCurso(
                                estudiante,
                                curso
                        );

        if (yaMatriculado) {
            // Evitar duplicados: redirige con un código de error específico.
            return "redirect:/estudiantes?error=curso-duplicado";
        }

        // 4) Control de créditos: sumar los créditos de las matrículas actuales del mismo periodo
        //    y comparar con el máximo permitido (22 en este sistema).
        List<CursoMatriculado> matriculasActuales =
                cursoMatriculadoRepository
                        .findByEstudianteAndPeriodo(
                                estudiante,
                                curso.getPeriodo()
                        );

        int totalCreditos = 0;

        // Sumar los créditos de cada asignatura ya matriculada en ese periodo.
        for (CursoMatriculado matriculaExistente : matriculasActuales) {

            totalCreditos +=
                    matriculaExistente
                            .getCurso()
                            .getAsignatura()
                            .getNumeroCreditos();
        }

        // Obtener créditos del curso que se intenta agregar.
        int creditosNuevoCurso =
                curso.getAsignatura().getNumeroCreditos();

        // Si la suma excede 22 créditos, impedir la matrícula y devolver error.
        if ((totalCreditos + creditosNuevoCurso) > 22) {

            return "redirect:/estudiantes?error=max-creditos";
        }

        // Si todas las validaciones pasan, construir la entidad CursoMatriculado y persistirla.
        CursoMatriculado matricula =
                CursoMatriculado.builder()
                        .estudiante(estudiante)
                        .curso(curso)
                        .estadoCurso("INSCRITO") // Estado inicial al matricularse.
                        .notaFinal(0.0) // Valor por defecto hasta que se registre la nota.
                        .periodo(curso.getPeriodo())
                        .build();

        // Delegar la operación de guardado al servicio.
        cursoMatriculadoService.guardar(matricula);

        // Redirigir con un parámetro de éxito para que la vista pueda mostrar confirmación.
        return "redirect:/estudiantes?success=matricula-ok";
    }
}
