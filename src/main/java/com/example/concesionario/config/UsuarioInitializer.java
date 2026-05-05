package com.example.concesionario.config;

import com.example.concesionario.entity.Usuario;
import com.example.concesionario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioInitializer {

    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            crearUsuarioSiNoExiste(usuarioRepository, passwordEncoder, "admin@admin.com", "1234", "ADMIN");
            crearUsuarioSiNoExiste(usuarioRepository, passwordEncoder, "cliente@cliente.com", "1234", "CLIENTE");
        };
    }

    private void crearUsuarioSiNoExiste(UsuarioRepository usuarioRepository,
                                        PasswordEncoder passwordEncoder,
                                        String email,
                                        String rawPassword,
                                        String role) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRole(role);
        usuarioRepository.save(usuario);
    }
}
