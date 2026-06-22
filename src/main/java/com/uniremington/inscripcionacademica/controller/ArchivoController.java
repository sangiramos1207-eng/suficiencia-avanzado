package com.uniremington.inscripcionacademica.controller;

/*
 * Controlador para manejo de archivos.
 * - Muestra un formulario para subir archivos.
 * - Guarda el archivo en la carpeta 'uploads' dentro del directorio del proyecto.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/archivos")
public class ArchivoController {

    private static final Logger logger = LoggerFactory.getLogger(ArchivoController.class);

    // Carpeta donde se guardan los archivos subidos: <raíz-del-proyecto>/uploads
    private static final String UPLOAD_DIR =
            System.getProperty("user.dir")
                    + File.separator
                    + "uploads";

    /**
     * GET /archivos
     * Muestra el formulario para subir archivos.
     */
    @GetMapping
    public String formulario(Model model) {

        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            logger.warn("No se pudo crear el directorio de uploads: {}", uploadPath, e);
        }

        File directorio = uploadPath.toFile();
        String[] archivosArr = directorio.list();
        List<String> archivos = archivosArr == null ? List.of() : Arrays.asList(archivosArr);

        model.addAttribute("archivos", archivos);

        return "archivo-form";
    }

    /**
     * POST /archivos/subir
     * Recibe un archivo desde el formulario y lo guarda en disco.
     * Parámetros:
     *  - archivo: archivo subido (multipart)
     *  - model: para enviar mensajes a la vista
     */
    @PostMapping("/subir")
    public String subirArchivo(
            @RequestParam("archivo")
            MultipartFile archivo,
            Model model
    ) {

        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            Files.createDirectories(uploadPath);

            // Nombre de destino dentro del directorio de uploads
            Path destinoPath = uploadPath.resolve(Objects.requireNonNull(archivo.getOriginalFilename()));

            // Registrar ruta (logger)
            logger.info("Guardando archivo en: {}", destinoPath);

            // Copiar el contenido del multipart al archivo destino
            Files.copy(
                    archivo.getInputStream(),
                    destinoPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Mensaje de éxito para la vista
            model.addAttribute(
                    "mensaje",
                    "Archivo cargado correctamente"
            );

        } catch (IOException e) {

            // En caso de error de E/S, registrar y mostrar mensaje de error
            logger.error("Error al guardar el archivo", e);

            model.addAttribute(
                    "mensaje",
                    "Error al cargar archivo"
            );
        }

        String[] archivosArr = new File(UPLOAD_DIR).list();
        List<String> archivos = archivosArr == null ? List.of() : Arrays.asList(archivosArr);
        model.addAttribute("archivos", archivos);

        // Volver al formulario (mismo template) mostrando el mensaje
        return "archivo-form";
    }

    @GetMapping("/eliminar/{nombre}")
    public String eliminarArchivo(
            @PathVariable String nombre,
            Model model
    ) {

        File archivo = new File(UPLOAD_DIR, nombre);

        if (archivo.exists()) {
            archivo.delete();
            model.addAttribute(
                    "mensaje",
                    "Archivo eliminado correctamente."
            );
        } else {
            model.addAttribute(
                    "mensaje",
                    "El archivo no existe."
            );
        }

        String[] archivos =
                new File(UPLOAD_DIR).list();

        model.addAttribute(
                "archivos",
                archivos
        );

        return "archivo-form";
    }
}