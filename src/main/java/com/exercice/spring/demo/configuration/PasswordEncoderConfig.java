package com.exercice.spring.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean  // BCryptPasswordEncoder DEVIENT DISPONIBLE DANS LE CONTENEUR BEAN DE SPRING
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// Classe fournit par spring
    }
}
