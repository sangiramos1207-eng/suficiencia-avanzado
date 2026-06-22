package com.uniremington.inscripcionacademica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// Controlador simple para la página de inicio
// Redirige la raíz (/) al listado de estudiantes
public class HomeController {

    @GetMapping("/")
    // Maneja GET / : redirige al listado de estudiantes
    public String inicio() {
        return "redirect:/estudiantes";
    }
}