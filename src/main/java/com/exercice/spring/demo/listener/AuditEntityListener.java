package com.exercice.spring.demo.listener;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditEntityListener implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Pour plus tard récupérer de la session le nom de l'utilisateur connecté
        return Optional.of("UserNameConnected");
    }
}