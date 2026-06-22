package com.uniremington.inscripcionacademica.controller;

import com.uniremington.inscripcionacademica.entity.CursoMatriculado;
import com.uniremington.inscripcionacademica.entity.Estudiante;
import com.uniremington.inscripcionacademica.service.CursoMatriculadoService;
import com.uniremington.inscripcionacademica.service.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.uniremington.inscripcionacademica.repository.ProgramaAcademicoRepository;

import java.util.List;
import com.uniremington.inscripcionacademica.entity.AsignaturaCursada;
import com.uniremington.inscripcionacademica.repository.AsignaturaCursadaRepository;

// Controlador que centraliza las operaciones HTTP relacionadas con la entidad Estudiante.
// Expone endpoints para listar alumnos, mostrar formularios de creación/edición, guardar cambios y eliminar registros.
// La lógica de negocio se delega al servicio EstudianteService; el controlador únicamente orquesta entradas/salidas web y mapas de modelo.
@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    // Servicio que contiene la lógica de negocio para la gestión de estudiantes.
    private final EstudianteService estudianteService;

    // Servicio adicional para consultar matrículas relacionadas con un estudiante.
    // Se usa para mostrar en la vista los cursos en los que está inscrito y calcular totales.
    private final CursoMatriculadoService cursoMatriculadoService;

    private final ProgramaAcademicoRepository programaAcademicoRepository;

    private final AsignaturaCursadaRepository asignaturaCursadaRepository;

    // Constructor que inyecta los servicios necesarios.
    // Inyección de dependencias por constructor facilita el testing y la inmutabilidad del controlador.
    public EstudianteController(
            EstudianteService estudianteService,
            CursoMatriculadoService cursoMatriculadoService,
            ProgramaAcademicoRepository programaAcademicoRepository,
            AsignaturaCursadaRepository asignaturaCursadaRepository
    ) {
        this.estudianteService = estudianteService;
        this.cursoMatriculadoService = cursoMatriculadoService;
        this.programaAcademicoRepository = programaAcademicoRepository;
        this.asignaturaCursadaRepository = asignaturaCursadaRepository;
    }

    // Muestra la lista de todos los estudiantes registrados.
    @GetMapping
    public String listarEstudiantes(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String success,
            Model model
    ) {

        model.addAttribute(
                "estudiantes",
                estudianteService.obtenerTodos()
        );

        model.addAttribute("error", error);
        model.addAttribute("success", success);

        return "estudiantes";
    }

    // Muestra el formulario para registrar un nuevo estudiante.
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {

        model.addAttribute(
                "estudiante",
                new Estudiante()
        );

        model.addAttribute(
                "programas",
                programaAcademicoRepository.findAll()
        );

        return "estudiante-form";
    }

    // Guarda un nuevo estudiante o actualiza uno existente.
    @PostMapping("/guardar")
    public String guardarEstudiante(
            @ModelAttribute Estudiante estudiante,
            RedirectAttributes redirectAttributes) {

        estudianteService.guardar(estudiante);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Estudiante guardado correctamente.");

        return "redirect:/estudiantes";
    }

    // Carga los detalles de un estudiante existente.
    @GetMapping("/detalle/{id}")
    public String detalleEstudiante(
            @PathVariable Long id,
            Model model
    ) {

        // Recuperar estudiante; lanzar excepción si no existe para que el flujo se interrumpa.
        Estudiante estudiante =
                estudianteService
                        .obtenerPorId(id)
                        .orElseThrow();

        // Obtener todas las matrículas asociadas a ese estudiante para mostrarlas en la vista.
        List<CursoMatriculado> matriculas =
                cursoMatriculadoService
                        .obtenerPorEstudiante(estudiante);

        // Calcular el total de créditos que el estudiante está cursando actualmente.
        // Se recorre cada matrícula y se suman los créditos de su asignatura.
        int totalCreditos = 0;

        for (CursoMatriculado matricula : matriculas) {

            // Obtener créditos de la asignatura relacionada al curso matriculado.
            totalCreditos +=
                    matricula.getCurso()
                            .getAsignatura()
                            .getNumeroCreditos();
        }

        // Añadir los objetos al modelo para que la plantilla los represente:
        // - estudiante: datos personales y académicos básicos
        // - matriculas: lista de cursos donde está inscrito
        // - totalCreditos: suma de créditos mostrada en la vista
        model.addAttribute(
                "estudiante",
                estudiante
        );

        model.addAttribute(
                "matriculas",
                matriculas
        );

        model.addAttribute(
                "totalCreditos",
                totalCreditos
        );

        // Obtener asignaturas cursadas y calcular promedio
        List<AsignaturaCursada> asignaturasCursadas =
                asignaturaCursadaRepository.findByEstudiante(estudiante);

        model.addAttribute(
                "asignaturasCursadas",
                asignaturasCursadas
        );

        double promedio = 0;
        if (!asignaturasCursadas.isEmpty()) {

            double suma = 0;

            for (AsignaturaCursada a : asignaturasCursadas) {
                suma += a.getNotaFinal();
            }

            promedio = suma / asignaturasCursadas.size();
        }

        model.addAttribute(
                "promedio",
                promedio
        );

        // Retorna la plantilla que muestra el detalle del estudiante y sus matrículas.
        return "estudiante-detalle";
    }

    // Carga los datos de un estudiante existente para su edición.
    @GetMapping("/editar/{id}")
    public String editarEstudiante(
            @PathVariable Long id,
            Model model
    ) {

        Estudiante estudiante = estudianteService
                .obtenerPorId(id)
                .orElseThrow();

        model.addAttribute(
                "estudiante",
                estudiante
        );

        model.addAttribute(
                "programas",
                programaAcademicoRepository.findAll()
        );

        return "estudiante-form";
    }

    // Elimina un estudiante según su identificador.
    @GetMapping("/eliminar/{id}")
    public String eliminarEstudiante(
            @PathVariable Long id
    ) {

        estudianteService.eliminar(id);

        return "redirect:/estudiantes";
    }
}
