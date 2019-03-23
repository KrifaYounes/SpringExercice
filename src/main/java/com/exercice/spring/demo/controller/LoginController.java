package com.exercice.spring.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;

@Controller
public class LoginController {

    // si l'utilisateur veut afficher la page login on vérifie si il est connecté
    // si il est connecté on le redirige directement sur la page admin
    // sans lui montrer la page login (car il est déjà connecté)
    @GetMapping("/login")
    public ModelAndView verifyLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // si l'utilisateur est authentifié alors on le redirige vers la page admin.html
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            // RedirectView permet de changer l'url
            return new ModelAndView(new RedirectView("admin"));
        } else {
            //sinon il reste sur la page de login + affiché message d'erreur
            ModelAndView model = new ModelAndView();
            model.setViewName("login");
            return model;
        }
    }

    @GetMapping("/admin")
    @Secured("ADMIN") // uniquement les utilisateurs avec le role ADMIN peuvent accèder à la page ADMIN
    public ModelAndView showAdminPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin");
        return model;
    }
}
