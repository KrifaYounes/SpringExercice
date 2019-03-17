package com.exercice.spring.demo.controller;

import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.dto.UserDto;
import com.exercice.spring.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private static final String INDEX_HTML = "index.html";

    /**
     * pour afficher la page quand l'utilisateur demande la ressource /
     * Rappel quand on tappe l'url http://localhost:8080/ la ressource demandé se nomme "/"
     * Spring appel donc la méthode avec l'annotation @GetMapping("/")
     * Puis on retourne la page index.html avec une liste d'utilisateur vide
     * car on n'a pas encore lancé la recherche avec le formulaire
     */
    @GetMapping("/")
    public ModelAndView displayIndex() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(INDEX_HTML);
        // par défaut quand on affiche la page le tableau est vide
        mv.addObject("users", new ArrayList<>());
        mv.addObject("searchAlreadyStart", false);

        return mv;
    }

    /**
     * Cette méthode est appelé par l'action "/searchUser" du formulaire
     * qui se trouve dans index.html (ICI on fait un GET) car method="GET"
     * dans le formulaire"
     */
    @GetMapping("/searchUser")
    public ModelAndView searchUser(UserDto userDto) {
        List<User> users = userService.searchUser(userDto);
        ModelAndView mv = new ModelAndView();
        mv.setViewName(INDEX_HTML); // on affiche le tableau sur la même page
        mv.addObject("users", users);
        mv.addObject("searchAlreadyStart", true);

        return mv;
    }

    @PostMapping("/saveUser")
    public ModelAndView saveUser(UserDto userDto) {
        userService.saveUser(userDto);
        //permet de changer l'url quand on va atterir sur la page de login
        return new ModelAndView(new RedirectView("login"));
        //Remarque: si on fait ça :
        //ModelAndView mv = new ModelAndView();
        //mv.setViewName("login");
        // on va montrer le formulaire de login avec une url = saveUser
    }
}
