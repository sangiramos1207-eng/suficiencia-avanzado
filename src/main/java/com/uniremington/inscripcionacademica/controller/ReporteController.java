package com.uniremington.inscripcionacademica.controller;

/*
 * ReporteController
 * Controlador para exportar reportes de estudiantes.
 * Comentarios simplificados en español latino para cada sección.
 * NOTA: Se agregó en templates/estudiantes.html un div justo debajo del <h1>
 * que muestra un mensaje al usuario (verde y en negrita). El snippet es:
 * <div th:if="${mensaje}" style="color: green; font-weight: bold; margin-bottom: 15px;">
 *     <span th:text="${mensaje}"></span>
 * </div>
 * - exportarExcel: genera un XLSX con la lista de estudiantes.
 * - exportarPdf: genera un PDF con info del estudiante y sus matrículas.
 */

import com.lowagie.text.pdf.PdfPTable;
import com.uniremington.inscripcionacademica.entity.Estudiante;
import com.uniremington.inscripcionacademica.service.EstudianteService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.uniremington.inscripcionacademica.entity.CursoMatriculado;
import com.uniremington.inscripcionacademica.service.CursoMatriculadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReporteController {

    private final EstudianteService estudianteService;
    private final CursoMatriculadoService cursoMatriculadoService;

    public ReporteController(
            EstudianteService estudianteService,
            CursoMatriculadoService cursoMatriculadoService
    ) {
        this.estudianteService = estudianteService;
        this.cursoMatriculadoService = cursoMatriculadoService;
    }

    /**
         * Ruta GET /reportes/estudiantes/excel
         * Genera y descarga un archivo Excel (.xlsx) con todos los estudiantes.
         * Comentario simple: prepara la respuesta, crea un workbook, llena filas y envía el archivo.
     */
    @GetMapping("/reportes/estudiantes/excel")
    public void exportarExcel(
            HttpServletResponse response
    ) throws IOException {

        // Preparar respuesta para descarga de archivo
        response.setContentType(
                "application/octet-stream"
        );

        // Indicar que la respuesta es un archivo adjunto con nombre estudiantes.xlsx
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=estudiantes.xlsx"
        );

        // Obtener todos los estudiantes desde el servicio
        List<Estudiante> estudiantes =
                estudianteService.obtenerTodos();

        // Crear libro de Excel en memoria
        Workbook workbook =
                new XSSFWorkbook();

        // Crear hoja y nombrarla
        Sheet sheet =
                workbook.createSheet(
                        "Estudiantes"
                );

        // Fila de encabezados (primera fila)
        Row header =
                sheet.createRow(0);

        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Identificación");
        header.createCell(2).setCellValue("Nombres");
        header.createCell(3).setCellValue("Apellidos");
        header.createCell(4).setCellValue("Semestre");
        header.createCell(5).setCellValue("Programa");

        int fila = 1;

        // Iterar estudiantes y llenar filas con sus datos
        for (Estudiante estudiante : estudiantes) {

            Row row =
                    sheet.createRow(fila++);

            row.createCell(0)
                    .setCellValue(
                            estudiante.getId()
                    );

            row.createCell(1)
                    .setCellValue(
                            estudiante.getIdentificacion()
                    );

            row.createCell(2)
                    .setCellValue(
                            estudiante.getNombres()
                    );

            row.createCell(3)
                    .setCellValue(
                            estudiante.getApellidos()
                    );

            row.createCell(4)
                    .setCellValue(
                            estudiante.getSemestreActual()
                    );

            row.createCell(5)
                    .setCellValue(
                            estudiante
                                    .getProgramaAcademico()
                                    .getNombrePrograma()
                    );
        }

        // Escribir el libro al stream de la respuesta
        workbook.write(
                response.getOutputStream()
        );

        workbook.close();
    }

    /**
         * Ruta GET /reportes/estudiante/{id}/pdf
         * Genera y descarga un PDF con datos del estudiante y sus cursos matriculados.
         * Comentario simple: busca estudiante, obtiene matrículas y asignaturas cursadas, arma el PDF y lo envía.
     */
    @GetMapping("/reportes/estudiante/{id}/pdf")
    public void exportarPdf(
            @PathVariable Long id,
            HttpServletResponse response
    ) throws Exception {

        // Buscar el estudiante por id (lanzará excepción si no existe)
        Estudiante estudiante =
                estudianteService
                        .obtenerPorId(id)
                        .orElseThrow();

        // Obtener las matrículas/cursos del estudiante
        List<CursoMatriculado> matriculas =
                cursoMatriculadoService
                        .obtenerPorEstudiante(estudiante);

        // Preparar respuesta como PDF
        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=estudiante_" + id + ".pdf"
        );

        // Crear documento PDF y asociar el writer al stream de respuesta
        Document document =
                new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        document.open();

        com.lowagie.text.Font tituloFont =
                new com.lowagie.text.Font(
                        com.lowagie.text.Font.HELVETICA,
                        18,
                        com.lowagie.text.Font.BOLD
                );

        com.lowagie.text.Font seccionFont =
                new com.lowagie.text.Font(
                        com.lowagie.text.Font.HELVETICA,
                        14,
                        com.lowagie.text.Font.BOLD
                );

        Paragraph titulo = new Paragraph(
                "REPORTE ACADÉMICO DEL ESTUDIANTE",
                tituloFont
        );

        titulo.setAlignment(Element.ALIGN_CENTER);

        document.add(titulo);
        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Nombre Completo: "
                        + estudiante.getNombres()
                        + " "
                        + estudiante.getApellidos()
        ));

        document.add(new Paragraph(
                "Identificación: "
                        + estudiante.getIdentificacion()
        ));

        document.add(new Paragraph(
                "Programa Académico: "
                        + estudiante.getProgramaAcademico().getNombrePrograma()
        ));

        document.add(new Paragraph(
                "Semestre Actual: "
                        + estudiante.getSemestreActual()
        ));

        document.add(new Paragraph(" "));
        document.add(new Paragraph(
                "CURSOS MATRICULADOS",
                seccionFont
        ));

        document.add(new Paragraph(" "));

        PdfPTable tablaCursos = new PdfPTable(3);

        tablaCursos.addCell("Asignatura");
        tablaCursos.addCell("Profesor");
        tablaCursos.addCell("Créditos");

        int totalCreditos = 0;

        for (CursoMatriculado matricula : matriculas) {

            tablaCursos.addCell(
                    matricula.getCurso()
                            .getAsignatura()
                            .getNombre()
            );

            tablaCursos.addCell(
                    matricula.getCurso()
                            .getProfesor()
                            .getNombres()
                            + " "
                            + matricula.getCurso()
                            .getProfesor()
                            .getApellidos()
            );

            tablaCursos.addCell(
                    String.valueOf(
                            matricula.getCurso()
                                    .getAsignatura()
                                    .getNumeroCreditos()
                    )
            );

            totalCreditos +=
                    matricula.getCurso()
                            .getAsignatura()
                            .getNumeroCreditos();
        }

        document.add(tablaCursos);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(
                "TOTAL CRÉDITOS MATRICULADOS: "
                        + totalCreditos
        ));

        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "HORARIO ACADÉMICO ACTUAL",
                seccionFont
        ));

        document.add(new Paragraph(" "));

        PdfPTable tablaHorario = new PdfPTable(5);

        tablaHorario.addCell("Asignatura");
        tablaHorario.addCell("Profesor");
        tablaHorario.addCell("Aula");
        tablaHorario.addCell("Horario");
        tablaHorario.addCell("Créditos");

        for (CursoMatriculado matricula : matriculas) {

            tablaHorario.addCell(
                    matricula.getCurso()
                            .getAsignatura()
                            .getNombre()
            );

            tablaHorario.addCell(
                    matricula.getCurso()
                            .getProfesor()
                            .getNombres()
                            + " "
                            + matricula.getCurso()
                            .getProfesor()
                            .getApellidos()
            );

            tablaHorario.addCell(
                    matricula.getCurso()
                            .getAula()
            );

            tablaHorario.addCell(
                    matricula.getCurso()
                            .getHorario()
            );

            tablaHorario.addCell(
                    String.valueOf(
                            matricula.getCurso()
                                    .getAsignatura()
                                    .getNumeroCreditos()
                    )
            );
        }

        document.add(tablaHorario);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(
                "Fecha de generación: "
                        + LocalDate.now()
        ));

        document.close();
    }
}