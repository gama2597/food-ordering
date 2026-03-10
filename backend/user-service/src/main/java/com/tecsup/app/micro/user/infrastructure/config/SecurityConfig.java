package com.tecsup.app.micro.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Configuración central de Seguridad del Microservicio de Usuarios.
 * Actúa como un Resource Server de OAuth2 que confía en Keycloak.
 */
@Configuration
@EnableWebSecurity
// Habilita la seguridad a nivel de métodos. Permite usar anotaciones como @PreAuthorize("hasRole('ADMIN')") en los controladores.
@EnableMethodSecurity 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque nuestras APIs son REST (Stateless) y no usan sesiones de navegador.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Definimos nuestra "Lista Blanca" interna. Swagger y Actuator (métricas) son de libre acceso.
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/actuator/**")
                        .permitAll()
                        // Cualquier otra petición a este microservicio requiere un token JWT válido.
                        .anyRequest().authenticated())
                // Configuramos este microservicio como un "Resource Server" (Servidor de Recursos).
                .oauth2ResourceServer(oauth2 -> oauth2
                        // Le decimos a Spring: "Cuando recibas un JWT, usa este convertidor personalizado 
                        // para extraer los roles antes de autorizar la petición".
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    /**
     * Puente entre Keycloak y Spring Security.
     * Spring Security espera los roles en una lista plana. Keycloak los envía anidados dentro de un objeto JSON.
     */
    @Bean
    @SuppressWarnings("unchecked")
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // Asignamos nuestro método personalizado de extracción de roles.
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return jwtAuthenticationConverter;
    }

    /**
     * Lógica "quirúrgica" para abrir el token JWT y sacar los roles de Keycloak.
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // 1. Buscamos el nodo "realm_access" dentro del cuerpo (payload) del JWT.
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return Collections.emptyList(); // Si no hay nodo, no hay roles.
        }

        // 2. Dentro de "realm_access", buscamos el arreglo llamado "roles".
        Object rolesObject = realmAccess.get("roles");
        if (!(rolesObject instanceof Collection<?> rolesCollection)) {
            return Collections.emptyList();
        }

        // 3. Transformamos la lista de roles de Keycloak al formato que entiende Spring Security.
        return rolesCollection.stream()
                .filter(String.class::isInstance) // Aseguramos que el rol sea un texto
                .map(String.class::cast)
                .map(this::normalizeRole)         // Le agregamos el prefijo "ROLE_"
                .map(SimpleGrantedAuthority::new) // Lo convertimos en una Autoridad de Spring
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    /**
     * Estandariza los nombres de los roles.
     * Spring Security requiere por convención que todos los roles empiecen con "ROLE_".
     * Ej: Keycloak envía "admin" -> Esto lo convierte a "ROLE_ADMIN".
     */
    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "ROLE_USER"; // Rol por defecto por seguridad
        }
        // Si ya tiene el prefijo, lo deja igual; si no, se lo agrega en mayúsculas.
        return role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
    }
}