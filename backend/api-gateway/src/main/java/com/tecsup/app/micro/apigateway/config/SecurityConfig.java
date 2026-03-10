package com.tecsup.app.micro.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Activa la seguridad para aplicaciones reactivas (WebFlux)
public class SecurityConfig {

    // Lista blanca: Rutas que NO requieren estar logueado (Swagger, métricas, etc.)
    private static final String[] PUBLIC_PATHS = {
            "/actuator/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // Deshabilitamos CSRF porque nuestra comunicación es vía APIs (Stateless) usando tokens, no cookies de sesión.
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Habilitamos CORS basándonos en la configuración global del application.yml
                .cors(Customizer.withDefaults())
                // Configuración de autorización de rutas
                .authorizeExchange(exchange -> exchange
                        // Permitimos las peticiones OPTIONS que hacen los navegadores (Preflight requests) para CORS
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Permitimos acceso libre a las rutas públicas (Swagger, Health checks)
                        .pathMatchers(PUBLIC_PATHS).permitAll()
                        // IMPORTANTE: Cualquier otra ruta requiere un token JWT válido
                        .anyExchange().authenticated() // Todo lo demás requiere token JWT
                )
                // Le decimos al Gateway que actuará como un "Resource Server" que valida tokens JWT emitidos por Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
