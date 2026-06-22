package com.uniremington.inscripcionacademica.config;

import com.uniremington.inscripcionacademica.entity.*;
import com.uniremington.inscripcionacademica.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Esta clase se encarga de cargar datos de prueba iniciales de forma automática en la base de datos al iniciar la aplicación.
// Implementa CommandLineRunner para ejecutar su código al momento del arranque.
@Component
public class DataInitializer implements CommandLineRunner {

    // Repositorios necesarios para guardar los datos de prueba en cada una de las tablas correspondientes.
    private final ProgramaAcademicoRepository programaAcademicoRepository;
    private final ProfesorRepository profesorRepository;
    private final PlanEstudioRepository planEstudioRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private final AsignaturaCursadaRepository asignaturaCursadaRepository;
    private final CursoMatriculadoRepository cursoMatriculadoRepository;

    // Constructor para inyectar de manera automática las dependencias de todos los repositorios.
    public DataInitializer(
            ProgramaAcademicoRepository programaAcademicoRepository,
            ProfesorRepository profesorRepository,
            PlanEstudioRepository planEstudioRepository,
            AsignaturaRepository asignaturaRepository,
            CursoRepository cursoRepository,
            EstudianteRepository estudianteRepository, AsignaturaCursadaRepository asignaturaCursadaRepository, CursoMatriculadoRepository cursoMatriculadoRepository
    ) {
        this.programaAcademicoRepository = programaAcademicoRepository;
        this.profesorRepository = profesorRepository;
        this.planEstudioRepository = planEstudioRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.asignaturaCursadaRepository = asignaturaCursadaRepository;
        this.cursoMatriculadoRepository = cursoMatriculadoRepository;
    }

    // Método principal que ejecuta la lógica de inicialización de los datos.
    @Override
    public void run(String... args) {

        // Si ya existen datos de programas académicos registrados, no volvemos a insertar nada para evitar duplicados.
        if (programaAcademicoRepository.count() > 0) {
            return;
        }

        // =====================================================
        // PROGRAMAS ACADÉMICOS
        // =====================================================

        ProgramaAcademico sistemas = ProgramaAcademico.builder()
                .duracion(10)
                .facultad("Ingeniería")
                .nombrePrograma("Ingeniería de Sistemas")
                .tituloOtorgado("Ingeniero de Sistemas")
                .build();

        ProgramaAcademico industrial = ProgramaAcademico.builder()
                .duracion(10)
                .facultad("Ingeniería")
                .nombrePrograma("Ingeniería Industrial")
                .tituloOtorgado("Ingeniero Industrial")
                .build();

        ProgramaAcademico administracion = ProgramaAcademico.builder()
                .duracion(8)
                .facultad("Ciencias Empresariales")
                .nombrePrograma("Administración de Empresas")
                .tituloOtorgado("Administrador de Empresas")
                .build();

        programaAcademicoRepository.save(sistemas);
        programaAcademicoRepository.save(industrial);
        programaAcademicoRepository.save(administracion);

        // =====================================================
        // PROFESORES
        // =====================================================

        Profesor profesor1 = Profesor.builder()
                .nombres("Carlos")
                .apellidos("Rodríguez")
                .departamento("Ingeniería")
                .especializacion("Desarrollo de Software")
                .build();

        Profesor profesor2 = Profesor.builder()
                .nombres("Ana")
                .apellidos("Gómez")
                .departamento("Ingeniería")
                .especializacion("Bases de Datos")
                .build();

        Profesor profesor3 = Profesor.builder()
                .nombres("Juan")
                .apellidos("Martínez")
                .departamento("Ingeniería")
                .especializacion("Arquitectura de Software")
                .build();

        Profesor profesor4 = Profesor.builder()
                .nombres("Laura")
                .apellidos("Castro")
                .departamento("Ingeniería")
                .especializacion("Sistemas Operativos")
                .build();

        Profesor profesor5 = Profesor.builder()
                .nombres("Miguel")
                .apellidos("Torres")
                .departamento("Ingeniería")
                .especializacion("Redes de Computadores")
                .build();

        profesorRepository.save(profesor1);
        profesorRepository.save(profesor2);
        profesorRepository.save(profesor3);
        profesorRepository.save(profesor4);
        profesorRepository.save(profesor5);

        // =====================================================
        // PLANES DE ESTUDIO
        // =====================================================

        PlanEstudio planSistemas = PlanEstudio.builder()
                .anioVigencia(2025)
                .descripcion("Plan Ingeniería de Sistemas")
                .programaAcademico(sistemas)
                .build();

        planEstudioRepository.save(planSistemas);

        // =====================================================
        // ASIGNATURAS
        // =====================================================

        Asignatura programacion1 = Asignatura.builder()
                .nombre("Programación I")
                .numeroCreditos(4)
                .semestreNivel(1)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .build();

        asignaturaRepository.save(programacion1);

        Asignatura programacion2 = Asignatura.builder()
                .nombre("Programación II")
                .numeroCreditos(4)
                .semestreNivel(2)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(programacion1)
                .build();

        Asignatura basesDatos = Asignatura.builder()
                .nombre("Bases de Datos")
                .numeroCreditos(3)
                .semestreNivel(3)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(programacion2)
                .build();

        Asignatura ingenieriaSoftware = Asignatura.builder()
                .nombre("Ingeniería de Software")
                .numeroCreditos(4)
                .semestreNivel(4)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(basesDatos)
                .build();

        Asignatura calculo1 = Asignatura.builder()
                .nombre("Cálculo I")
                .numeroCreditos(4)
                .semestreNivel(1)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .build();

        Asignatura introduccionIngenieria = Asignatura.builder()
                .nombre("Introducción a la Ingeniería")
                .numeroCreditos(2)
                .semestreNivel(1)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .build();

        Asignatura calculo2 = Asignatura.builder()
                .nombre("Cálculo II")
                .numeroCreditos(4)
                .semestreNivel(2)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(calculo1)
                .build();

        Asignatura fisica1 = Asignatura.builder()
                .nombre("Física I")
                .numeroCreditos(3)
                .semestreNivel(2)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .build();

        Asignatura estructurasDatos = Asignatura.builder()
                .nombre("Estructuras de Datos")
                .numeroCreditos(4)
                .semestreNivel(3)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(programacion2)
                .build();

        Asignatura sistemasOperativos = Asignatura.builder()
                .nombre("Sistemas Operativos")
                .numeroCreditos(4)
                .semestreNivel(4)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(estructurasDatos)
                .build();

        Asignatura redes = Asignatura.builder()
                .nombre("Redes de Computadores")
                .numeroCreditos(3)
                .semestreNivel(4)
                .departamento("Ingeniería")
                .planEstudio(planSistemas)
                .prerrequisito(sistemasOperativos)
                .build();

        Asignatura asignaturaPrueba23 = asignaturaRepository.save(
                Asignatura.builder()
                        .nombre("Asignatura de Prueba 23 Creditos")
                        .numeroCreditos(23)
                        .build()
        );

        asignaturaRepository.save(programacion2);
        asignaturaRepository.save(basesDatos);
        asignaturaRepository.save(ingenieriaSoftware);
        asignaturaRepository.save(calculo1);
        asignaturaRepository.save(introduccionIngenieria);
        asignaturaRepository.save(calculo2);
        asignaturaRepository.save(fisica1);
        asignaturaRepository.save(estructurasDatos);
        asignaturaRepository.save(sistemasOperativos);
        asignaturaRepository.save(redes);

        // =====================================================
        // CURSOS
        // =====================================================

        Curso curso1 = Curso.builder()
                .aula("B-201")
                .cupoMaximo(35)
                .horario("LUN-MIE 08:00-10:00")
                .periodo("2026-1")
                .asignatura(programacion1)
                .profesor(profesor1)
                .build();

        Curso curso2 = Curso.builder()
                .aula("B-202")
                .cupoMaximo(30)
                .horario("MAR-JUE 10:00-12:00")
                .periodo("2026-1")
                .asignatura(programacion2)
                .profesor(profesor2)
                .build();

        Curso curso3 = Curso.builder()
                .aula("B-203")
                .cupoMaximo(25)
                .horario("VIE 14:00-18:00")
                .periodo("2026-1")
                .asignatura(basesDatos)
                .profesor(profesor3)
                .build();

        Curso curso4 = Curso.builder()
                .aula("B-204")
                .cupoMaximo(30)
                .horario("LUN-MIE 10:00-12:00")
                .periodo("2026-1")
                .asignatura(calculo1)
                .profesor(profesor2)
                .build();

        Curso curso5 = Curso.builder()
                .aula("B-205")
                .cupoMaximo(30)
                .horario("MAR-JUE 08:00-10:00")
                .periodo("2026-1")
                .asignatura(calculo2)
                .profesor(profesor2)
                .build();

        Curso curso6 = Curso.builder()
                .aula("B-206")
                .cupoMaximo(35)
                .horario("VIE 08:00-12:00")
                .periodo("2026-1")
                .asignatura(introduccionIngenieria)
                .profesor(profesor1)
                .build();

        Curso curso7 = Curso.builder()
                .aula("B-207")
                .cupoMaximo(25)
                .horario("LUN-MIE 14:00-16:00")
                .periodo("2026-1")
                .asignatura(fisica1)
                .profesor(profesor3)
                .build();

        Curso curso8 = Curso.builder()
                .aula("B-208")
                .cupoMaximo(30)
                .horario("MAR-JUE 14:00-16:00")
                .periodo("2026-1")
                .asignatura(estructurasDatos)
                .profesor(profesor1)
                .build();

        Curso curso9 = Curso.builder()
                .aula("B-209")
                .cupoMaximo(30)
                .horario("LUN-MIE 16:00-18:00")
                .periodo("2026-1")
                .asignatura(sistemasOperativos)
                .profesor(profesor4)
                .build();

        Curso curso10 = Curso.builder()
                .aula("B-210")
                .cupoMaximo(30)
                .horario("MAR-JUE 16:00-18:00")
                .periodo("2026-1")
                .asignatura(redes)
                .profesor(profesor5)
                .build();

        Curso cursoPrueba23 = cursoRepository.save(
                Curso.builder()
                        .asignatura(asignaturaPrueba23)
                        .profesor(profesor1)
                        .aula("LAB-999")
                        .horario("Vie 18:00")
                        .build()
        );

        cursoRepository.save(curso1);
        cursoRepository.save(curso2);
        cursoRepository.save(curso3);
        cursoRepository.save(curso4);
        cursoRepository.save(curso5);
        cursoRepository.save(curso6);
        cursoRepository.save(curso7);
        cursoRepository.save(curso8);
        cursoRepository.save(curso9);
        cursoRepository.save(curso10);

        // =====================================================
        // ESTUDIANTES
        // =====================================================

        Estudiante estudiante1 = Estudiante.builder()
                .nombres("Santiago")
                .apellidos("Ramos")
                .identificacion("1001001001")
                .semestreActual(3)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante2 = Estudiante.builder()
                .nombres("Pedro")
                .apellidos("Pérez")
                .identificacion("1001001002")
                .semestreActual(2)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante3 = Estudiante.builder()
                .nombres("María")
                .apellidos("López")
                .identificacion("1001001003")
                .semestreActual(4)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante4 = Estudiante.builder()
                .identificacion("1001001004")
                .nombres("Juan")
                .apellidos("Gómez")
                .semestreActual(1)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante5 = Estudiante.builder()
                .identificacion("1001001005")
                .nombres("Laura")
                .apellidos("Díaz")
                .semestreActual(2)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante6 = Estudiante.builder()
                .identificacion("1001001006")
                .nombres("Carlos")
                .apellidos("Torres")
                .semestreActual(3)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante7 = Estudiante.builder()
                .identificacion("1001001007")
                .nombres("Ana")
                .apellidos("Martínez")
                .semestreActual(4)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante8 = Estudiante.builder()
                .identificacion("1001001008")
                .nombres("Luis")
                .apellidos("Castro")
                .semestreActual(5)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante9 = Estudiante.builder()
                .identificacion("1001001009")
                .nombres("Valentina")
                .apellidos("Ruiz")
                .semestreActual(3)
                .programaAcademico(sistemas)
                .build();

        Estudiante estudiante10 = Estudiante.builder()
                .identificacion("1001001010")
                .nombres("Andrés")
                .apellidos("Herrera")
                .semestreActual(2)
                .programaAcademico(sistemas)
                .build();

        estudianteRepository.save(estudiante1);
        estudianteRepository.save(estudiante2);
        estudianteRepository.save(estudiante3);
        estudianteRepository.save(estudiante4);
        estudianteRepository.save(estudiante5);
        estudianteRepository.save(estudiante6);
        estudianteRepository.save(estudiante7);
        estudianteRepository.save(estudiante8);
        estudianteRepository.save(estudiante9);
        estudianteRepository.save(estudiante10);

        // =====================================================
        // ASIGNATURAS CURSADAS
        // =====================================================

        AsignaturaCursada ac1 = AsignaturaCursada.builder()
                .estudiante(estudiante6)
                .asignatura(programacion1)
                .notaFinal(4.5)
                .build();

        AsignaturaCursada ac2 = AsignaturaCursada.builder()
                .estudiante(estudiante7)
                .asignatura(programacion1)
                .notaFinal(4.0)
                .build();

        AsignaturaCursada ac3 = AsignaturaCursada.builder()
                .estudiante(estudiante7)
                .asignatura(programacion2)
                .notaFinal(4.2)
                .build();

        AsignaturaCursada ac4 = AsignaturaCursada.builder()
                .estudiante(estudiante8)
                .asignatura(programacion1)
                .notaFinal(2.5)
                .build();

        AsignaturaCursada ac5 = AsignaturaCursada.builder()
                .estudiante(estudiante9)
                .asignatura(programacion1)
                .notaFinal(3.8)
                .build();

        asignaturaCursadaRepository.save(ac1);
        asignaturaCursadaRepository.save(ac2);
        asignaturaCursadaRepository.save(ac3);
        asignaturaCursadaRepository.save(ac4);
        asignaturaCursadaRepository.save(ac5);

        // =====================================================
        // CURSOS MATRICULADOS
        // =====================================================

        CursoMatriculado cm1 = CursoMatriculado.builder()
                .estudiante(estudiante1)
                .curso(curso1)
                .periodo("2026-1")
                .estadoCurso("INSCRITO")
                .notaFinal(0.0)
                .build();

        CursoMatriculado cm2 = CursoMatriculado.builder()
                .estudiante(estudiante1)
                .curso(curso4)
                .periodo("2026-1")
                .estadoCurso("INSCRITO")
                .notaFinal(0.0)
                .build();

        CursoMatriculado cm3 = CursoMatriculado.builder()
                .estudiante(estudiante2)
                .curso(curso2)
                .periodo("2026-1")
                .estadoCurso("INSCRITO")
                .notaFinal(0.0)
                .build();

        CursoMatriculado cm4 = CursoMatriculado.builder()
                .estudiante(estudiante2)
                .curso(curso7)
                .periodo("2026-1")
                .estadoCurso("INSCRITO")
                .notaFinal(0.0)
                .build();

        CursoMatriculado cm5 = CursoMatriculado.builder()
                .estudiante(estudiante3)
                .curso(curso3)
                .periodo("2026-1")
                .estadoCurso("INSCRITO")
                .notaFinal(0.0)
                .build();

        CursoMatriculado cm6 = CursoMatriculado.builder()
                .estudiante(estudiante6)
                .curso(curso8)
                .periodo("2026-1")
                .estadoCurso("INSCRITO")
                .notaFinal(0.0)
                .build();

        cursoMatriculadoRepository.save(cm1);
        cursoMatriculadoRepository.save(cm2);
        cursoMatriculadoRepository.save(cm3);
        cursoMatriculadoRepository.save(cm4);
        cursoMatriculadoRepository.save(cm5);
        cursoMatriculadoRepository.save(cm6);

        System.out.println("DATOS INICIALES CARGADOS CORRECTAMENTE");
    }
}