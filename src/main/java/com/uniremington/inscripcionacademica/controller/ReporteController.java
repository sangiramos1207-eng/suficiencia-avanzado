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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.PageSize;
import java.awt.Color;
import java.text.DecimalFormat;
import com.uniremington.inscripcionacademica.entity.CursoMatriculado;
import com.uniremington.inscripcionacademica.service.CursoMatriculadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.uniremington.inscripcionacademica.entity.AsignaturaCursada;
import com.uniremington.inscripcionacademica.repository.AsignaturaCursadaRepository;

@Controller
public class ReporteController {

    private final EstudianteService estudianteService;
    private final CursoMatriculadoService cursoMatriculadoService;
    private final AsignaturaCursadaRepository asignaturaCursadaRepository;

    public ReporteController(
            EstudianteService estudianteService,
            CursoMatriculadoService cursoMatriculadoService,
            AsignaturaCursadaRepository asignaturaCursadaRepository
    ) {
        this.estudianteService = estudianteService;
        this.cursoMatriculadoService = cursoMatriculadoService;
        this.asignaturaCursadaRepository = asignaturaCursadaRepository;
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

        // Obtener asignaturas cursadas para cálculo de promedio y listado
        List<AsignaturaCursada> asignaturasCursadas =
                asignaturaCursadaRepository
                        .findByEstudiante(estudiante);

        double promedio = 0;

        if (!asignaturasCursadas.isEmpty()) {

            double suma = 0;

            for (AsignaturaCursada a : asignaturasCursadas) {
                suma += a.getNotaFinal();
            }

            promedio = suma / asignaturasCursadas.size();
        }

        // Preparar respuesta como PDF
        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=estudiante_" + id + ".pdf"
        );

        // Crear documento PDF con margen y tamaño A4
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);

        PdfWriter writer = PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        // Evento para footer con fecha y número de página
        class Footer extends PdfPageEventHelper {
            com.lowagie.text.Font footerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9, com.lowagie.text.Font.ITALIC);

            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                PdfContentByte cb = writer.getDirectContent();
                Phrase footer = new Phrase("Fecha: " + LocalDate.now() + "    Página " + writer.getPageNumber(), footerFont);
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() + document.left()) / 2, document.bottom() - 10, 0);
            }
        }

        writer.setPageEvent(new Footer());

        document.open();

        com.lowagie.text.Font tituloFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font seccionFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD);
        com.lowagie.text.Font normalFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD);

        Paragraph titulo = new Paragraph("REPORTE ACADÉMICO DEL ESTUDIANTE", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10f);
        document.add(titulo);

        // Datos del estudiante
        Paragraph datos = new Paragraph();
        datos.setFont(normalFont);
        datos.add(new Phrase("Nombre Completo: " , new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,11, com.lowagie.text.Font.BOLD)));
        datos.add(new Phrase(" " + estudiante.getNombres() + " " + estudiante.getApellidos() + "\n", normalFont));
        datos.add(new Phrase("Identificación: ", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,11, com.lowagie.text.Font.BOLD)));
        datos.add(new Phrase(" " + estudiante.getIdentificacion() + "\n", normalFont));
        datos.add(new Phrase("Programa Académico: ", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,11, com.lowagie.text.Font.BOLD)));
        datos.add(new Phrase(" " + estudiante.getProgramaAcademico().getNombrePrograma() + "\n", normalFont));
        datos.add(new Phrase("Semestre Actual: ", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA,11, com.lowagie.text.Font.BOLD)));
        datos.add(new Phrase(" " + estudiante.getSemestreActual() + "\n", normalFont));
        datos.setSpacingAfter(8f);
        document.add(datos);

        // Tabla Cursos Matriculados con estilos
        document.add(new Paragraph("CURSOS MATRICULADOS", seccionFont));
        document.add(new Paragraph(" "));

        PdfPTable tablaCursos = new PdfPTable(3);
        tablaCursos.setWidthPercentage(100);
        tablaCursos.setWidths(new float[]{4f, 4f, 1.5f});
        tablaCursos.setSpacingBefore(6f);
        tablaCursos.setSpacingAfter(6f);

        PdfPCell cell;
        // Header cells
        cell = new PdfPCell(new Phrase("Asignatura", headerFont));
        cell.setBackgroundColor(new Color(230,230,230)); cell.setPadding(6f);
        tablaCursos.addCell(cell);
        cell = new PdfPCell(new Phrase("Profesor", headerFont));
        cell.setBackgroundColor(new Color(230,230,230)); cell.setPadding(6f);
        tablaCursos.addCell(cell);
        cell = new PdfPCell(new Phrase("Créditos", headerFont));
        cell.setBackgroundColor(new Color(230,230,230)); cell.setPadding(6f); cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaCursos.addCell(cell);

        int totalCreditos = 0;
        int rowIndex = 0;
        for (CursoMatriculado matricula : matriculas) {
            Color bg = (rowIndex % 2 == 0) ? Color.WHITE : new Color(245,245,245);

            cell = new PdfPCell(new Phrase(matricula.getCurso().getAsignatura().getNombre(), normalFont));
            cell.setBackgroundColor(bg); cell.setPadding(5f);
            tablaCursos.addCell(cell);

            cell = new PdfPCell(new Phrase(matricula.getCurso().getProfesor().getNombres() + " " + matricula.getCurso().getProfesor().getApellidos(), normalFont));
            cell.setBackgroundColor(bg); cell.setPadding(5f);
            tablaCursos.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(matricula.getCurso().getAsignatura().getNumeroCreditos()), normalFont));
            cell.setBackgroundColor(bg); cell.setHorizontalAlignment(Element.ALIGN_RIGHT); cell.setPadding(5f);
            tablaCursos.addCell(cell);

            totalCreditos += matricula.getCurso().getAsignatura().getNumeroCreditos();
            rowIndex++;
        }

        document.add(tablaCursos);

        Paragraph totalCred = new Paragraph("TOTAL CRÉDITOS MATRICULADOS: " + totalCreditos, seccionFont);
        totalCred.setSpacingAfter(8f);
        document.add(totalCred);

        // Horario
        document.add(new Paragraph("HORARIO ACADÉMICO ACTUAL", seccionFont));
        document.add(new Paragraph(" "));

        PdfPTable tablaHorario = new PdfPTable(5);
        tablaHorario.setWidthPercentage(100);
        tablaHorario.setWidths(new float[]{3.5f,3.5f,1.5f,2.0f,1.0f});
        tablaHorario.setSpacingBefore(6f);
        tablaHorario.setSpacingAfter(6f);

        // Headers
        String[] headersHorario = new String[]{"Asignatura","Profesor","Aula","Horario","Créditos"};
        for (String h : headersHorario) {
            cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new Color(230,230,230)); cell.setPadding(6f);
            tablaHorario.addCell(cell);
        }

        rowIndex = 0;
        for (CursoMatriculado matricula : matriculas) {
            Color bg = (rowIndex % 2 == 0) ? Color.WHITE : new Color(245,245,245);

            cell = new PdfPCell(new Phrase(matricula.getCurso().getAsignatura().getNombre(), normalFont)); cell.setBackgroundColor(bg); cell.setPadding(5f);
            tablaHorario.addCell(cell);

            cell = new PdfPCell(new Phrase(matricula.getCurso().getProfesor().getNombres() + " " + matricula.getCurso().getProfesor().getApellidos(), normalFont)); cell.setBackgroundColor(bg); cell.setPadding(5f);
            tablaHorario.addCell(cell);

            cell = new PdfPCell(new Phrase(matricula.getCurso().getAula(), normalFont)); cell.setBackgroundColor(bg); cell.setPadding(5f);
            tablaHorario.addCell(cell);

            cell = new PdfPCell(new Phrase(matricula.getCurso().getHorario(), normalFont)); cell.setBackgroundColor(bg); cell.setPadding(5f);
            tablaHorario.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(matricula.getCurso().getAsignatura().getNumeroCreditos()), normalFont)); cell.setBackgroundColor(bg); cell.setHorizontalAlignment(Element.ALIGN_RIGHT); cell.setPadding(5f);
            tablaHorario.addCell(cell);

            rowIndex++;
        }

        document.add(tablaHorario);

        // Asignaturas cursadas
        document.add(new Paragraph("ASIGNATURAS CURSADAS", seccionFont));
        document.add(new Paragraph(" "));

        PdfPTable tablaHistorial = new PdfPTable(2);
        tablaHistorial.setWidthPercentage(100);
        tablaHistorial.setWidths(new float[]{5f,1.5f});
        tablaHistorial.setSpacingBefore(6f);
        tablaHistorial.setSpacingAfter(6f);

        cell = new PdfPCell(new Phrase("Asignatura", headerFont)); cell.setBackgroundColor(new Color(230,230,230)); cell.setPadding(6f); tablaHistorial.addCell(cell);
        cell = new PdfPCell(new Phrase("Nota Final", headerFont)); cell.setBackgroundColor(new Color(230,230,230)); cell.setPadding(6f); cell.setHorizontalAlignment(Element.ALIGN_RIGHT); tablaHistorial.addCell(cell);

        DecimalFormat df = new DecimalFormat("#.00");
        rowIndex = 0;
        for (AsignaturaCursada asignatura : asignaturasCursadas) {
            Color bg = (rowIndex % 2 == 0) ? Color.WHITE : new Color(245,245,245);

            cell = new PdfPCell(new Phrase(asignatura.getAsignatura().getNombre(), normalFont)); cell.setBackgroundColor(bg); cell.setPadding(5f); tablaHistorial.addCell(cell);
            cell = new PdfPCell(new Phrase(df.format(asignatura.getNotaFinal()), normalFont)); cell.setBackgroundColor(bg); cell.setHorizontalAlignment(Element.ALIGN_RIGHT); cell.setPadding(5f); tablaHistorial.addCell(cell);

            rowIndex++;
        }

        document.add(tablaHistorial);

        document.add(new Paragraph(" "));

        Paragraph promedioPar = new Paragraph("PROMEDIO ACUMULADO: " + df.format(promedio), seccionFont);
        promedioPar.setSpacingAfter(8f);
        document.add(promedioPar);

        // Fecha final (el footer también mostrará la fecha y página)
        document.add(new Paragraph(" "));
        // (footer will add date and page number)

        document.close();
    }
}
