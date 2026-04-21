package com.example.concesionario.config;

import com.example.concesionario.entity.Marca;
import com.example.concesionario.entity.Modelo;
import com.example.concesionario.entity.Concesionario;
import com.example.concesionario.entity.Extra;
import com.example.concesionario.entity.Vehiculo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        // Base path para todos los endpoints REST
        config.setBasePath("/api");

        // Exponer IDs de todas las entidades
        config.exposeIdsFor(
                Marca.class,
                Modelo.class,
                Concesionario.class,
                Extra.class,
                Vehiculo.class
        );

        // Configuración CORS
        cors.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
