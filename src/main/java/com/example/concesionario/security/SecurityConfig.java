package com.example.concesionario.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           SessionAuthenticationFilter sessionFilter) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/login", "/css/**").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/modelos/crear",
                                "/modelos/editar/*",
                                "/marcas/crear",
                                "/marcas/editar/*",
                                "/extras/crear",
                                "/extras/editar/*",
                                "/vehiculos/crear",
                                "/vehiculos/editar/*",
                                "/concesionarios/crear",
                                "/concesionarios/editar/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/modelos/guardar",
                                "/modelos/eliminar/*",
                                "/marcas/guardar",
                                "/marcas/eliminar/*",
                                "/extras/guardar",
                                "/extras/eliminar/*",
                                "/vehiculos/guardar",
                                "/vehiculos/eliminar/*",
                                "/concesionarios/guardar",
                                "/concesionarios/eliminar/*"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
