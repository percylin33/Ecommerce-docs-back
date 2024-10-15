package com.carpetadigital.ecommerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Ajusta el path y orígenes según tus necesidades
                registry.addMapping("/api/v1/**") // Específica el path de tus endpoints
                        .allowedOrigins("http://localhost:8080") // Cambia esto al origen que necesites
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                        .allowedHeaders("*") // Permitir todos los encabezados
                        .allowCredentials(true); // Si necesitas enviar cookies o headers de autenticación
            }
        };
    }
}
