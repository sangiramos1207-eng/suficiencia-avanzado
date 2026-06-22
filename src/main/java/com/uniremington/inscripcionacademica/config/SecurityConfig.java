package com.uniremington.inscripcionacademica.config;

/*
 * Configuración de seguridad de la aplicación (versión simplificada).
 * - Crea un usuario 'admin' en memoria para desarrollo y pruebas locales.
 * - Controla acceso HTTP: /login queda público y el resto requiere autenticación.
 * - CSRF está desactivado aquí (revisar antes de exponer una API en producción).
 * - Usa formulario de login y logout que redirige a /login.
 * NOTA: En producción mover usuarios a una base de datos y usar un PasswordEncoder fuerte (ej. BCrypt).
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    /**
     * Usuario en memoria: 'admin' / '1234'.
     * Propósito: pruebas y desarrollo local.
     * NO usar en producción: mover usuarios a BD y usar BCryptPasswordEncoder.
     */
    public InMemoryUserDetailsManager userDetailsService() {

        // Usuario administrador en memoria (username: admin, password: 1234)
        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("1234")
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * - Desactiva CSRF (ajustar según requisitos de la API).
     * - Permite el acceso público a /login y requiere autenticación para el resto.
     * - Habilita login por formulario y define la URL de éxito al cerrar sesión.
     */
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // Deshabilitar CSRF (si se expone API pública, revisar esta opción)
                .csrf(csrf -> csrf.disable())

                // Reglas de autorización por URL
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )

                // Habilita formulario de login
                .formLogin(form -> form
                        // Página de login personalizada (GET /login muestra el formulario)
                        .loginPage("/login")
                        // Al iniciar sesión correctamente, ir a /estudiantes siempre (true)
                        .defaultSuccessUrl("/estudiantes", true)
                        .permitAll()
                )

                // Configuración de logout: cerrar sesión y redirigir a /login
                .logout(logout -> logout
                        // Después del logout redirige a la página de login
                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }
}