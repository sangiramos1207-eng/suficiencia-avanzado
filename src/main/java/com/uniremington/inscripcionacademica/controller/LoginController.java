package com.uniremington.inscripcionacademica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// Controlador para mostrar la página de login
// GET /login devuelve la plantilla 'login.html'
public class LoginController {

    @GetMapping("/login")
    // Muestra el formulario de inicio de sesión
    // Spring Security procesará el POST a /login automáticamente
    public String login() {
        return "login";
    }

}