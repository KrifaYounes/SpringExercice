package com.exercice.spring.demo.configuration;

import com.exercice.spring.demo.listener.AuditEntityListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * @Configuration : classe de configuration
 * Avant avec spring on effectué la configuration d'un projet avec des fichiers xml
 * Aujourd'hui avec spring boot on peut utilisé le fichier application.properties ou utilisé les
 * annotation @Configuration
 */
@Configuration

/**
 * @EnableJpaAuditing : permet d'activer la fonctionnalité d'audit de la base de données
 * le param auditorAwareRef contient auditorAware
 * auditorAware est le nom de la méthode qui va retourné l'interface AuditorAware<?>
 *     Dans notre cas on retourne AuditorAware<String>
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfiguration {

    /*
    Une méthode qui comporte l'annotation @Bean est lancé par Spring
    Afin de rendre disponible l'objet retourné par la méthode dans le conteneur des Bean
    Spring contient un conteneur de Bean.
    Dans notre exemple l'objet AuditorAware<String> est disponible dans notre conteneur (mode singleton)
    et peut être utilisé par notre programme utltériement lorsqu'on le souhaite.
    Il peut aussi être utilisé par notre programme de manière transparente.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditEntityListener();
    }
}